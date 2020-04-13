package com.example.foreignlanguagepractice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * database manager
 */
public class DB extends SQLiteOpenHelper {

    private static final String DB_NAME = "Translator.db";
    private static final String OFFLINE_DB_NAME = "db";
    private static final String PHRASES_TABLE = "PhraseList_table";
    private static final String PHRASE = "Phrase";
    private static final String LANGUAGES_TABLE = "languages";
    private static final String OFFLINE_TABLE = "offline";
    private static String DB_PATH;
    private Context context1;
    private SQLiteDatabase languagesDB;

    private SQLiteDatabase db = this.getWritableDatabase();

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + PHRASES_TABLE + " (Phrase TEXT PRIMARY KEY)");
        db.execSQL("CREATE TABLE " + OFFLINE_TABLE + " (Phrase TEXT, Translation TEXT, Lang TEXT, " +
                "PRIMARY KEY (Phrase, Translation))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PHRASES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + OFFLINE_TABLE);
        onCreate(db);
    }

    DB(Context context) {
        super(context, DB_NAME, null, 1);
        context1 = context;
        DB_PATH = context1.getExternalFilesDir(null).getPath() + "/";

        boolean dbexist = checkDatabase();
        if (dbexist) {
            System.out.println(" Database exists.");
        } else {
            this.getReadableDatabase();
            try {
                // copy the database to external storage
                copyDatabase();

            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
        openDatabase();
    }

    private boolean checkDatabase() {

        boolean checkdb = false;
        try {
            String myPath = DB_PATH + OFFLINE_DB_NAME;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        } catch (SQLiteException e) {
            System.out.println("Database doesn't exist");
        }
        return checkdb;
    }

    /**
     * copy prebuilt database to the external storage
     * @throws IOException
     */
    private void copyDatabase() throws IOException {
        //Open local db as the input stream
        InputStream myinput = context1.getAssets().open(OFFLINE_DB_NAME);

        // Path to the just created empty db
        String outfilename = DB_PATH + OFFLINE_DB_NAME;

        //Open the empty db as the output stream
        OutputStream fileOutputStream = new FileOutputStream(outfilename);

        // transfer byte to output file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer)) > 0) {
            fileOutputStream.write(buffer, 0, length);
        }

        //Close the streams
        fileOutputStream.flush();
        fileOutputStream.close();
        myinput.close();
    }

    private void openDatabase() throws SQLException {
        String mypath = DB_PATH + OFFLINE_DB_NAME;
        languagesDB = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    boolean insertData(String phrase) {
        long result = 0;
        String Query = "Select * from " + PHRASES_TABLE + " WHERE Phrase ='" + phrase + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            ContentValues contentValues = new ContentValues();
            contentValues.put(PHRASE, phrase);

            db.insert(PHRASES_TABLE, null, contentValues);
            result = 1;
        }
        cursor.close();
        return result != 0;
    }

    void updateSubscription(String id, int subscribed) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("subscribed", subscribed);

        languagesDB.update(LANGUAGES_TABLE, contentValues, "rowid = ?", new String[]{id});
    }

    Cursor getAllLangs() {
        Cursor res = languagesDB.rawQuery("SELECT rowid, * FROM " + LANGUAGES_TABLE, null);
        return res;
    }

    boolean updateData(String phrase, String id) {
        long result = 0;
        String Query = "Select * from " + PHRASES_TABLE + " WHERE Phrase ='" + phrase + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            ContentValues contentValues = new ContentValues();
            contentValues.put(PHRASE, phrase);

            db.update(PHRASES_TABLE, contentValues, "rowid = ?", new String[]{id});
            result = 1;
        }

        cursor.close();
        return result != 0;
    }

    // external storage database methods
    Cursor getAllData() {
        Cursor res = db.rawQuery("SELECT rowid, * FROM " + PHRASES_TABLE +
                " ORDER BY Phrase COLLATE NOCASE ", null);
        return res;
    }

    Cursor getOfflineData(String lang) {
        Cursor res = db.rawQuery("SELECT rowid, * FROM " + OFFLINE_TABLE +
                " WHERE Lang = '" + lang + "'", null);
        return res;
    }

    void insertOfflineData(String phrase, String translation, String lang) {
        String Query = "Select * from " + OFFLINE_TABLE + " WHERE Phrase ='" + phrase +
                "' AND Lang = '" + lang + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            ContentValues contentValues = new ContentValues();
            contentValues.put(PHRASE, phrase);
            contentValues.put("Translation", translation);
            contentValues.put("Lang", lang);

            db.insert(OFFLINE_TABLE, null, contentValues);
        }
        cursor.close();
    }
}
