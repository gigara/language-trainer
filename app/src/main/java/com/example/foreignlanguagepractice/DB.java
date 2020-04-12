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

    private static final String DATABASE_NAME = "Translator.db";
    private static final String PHRASE_LIST_TABLE = "PhraseList_table";
    private static final String PHRASE = "Phrase";
    private static final String LANGUAGES_TABLE = "languages";
    private static final String OFFLINE_TABLE = "offline";
    private static String DB_NAME = "db";
    private static String DB_PATH;
    private Context context1;
    private SQLiteDatabase languagesDB;

    private SQLiteDatabase db = this.getWritableDatabase();

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + PHRASE_LIST_TABLE + " (Phrase TEXT PRIMARY KEY)");
        db.execSQL("CREATE TABLE " + OFFLINE_TABLE + " (Phrase TEXT, Translation TEXT, Lang TEXT, " +
                "PRIMARY KEY (Phrase, Translation))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PHRASE_LIST_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + OFFLINE_TABLE);
        onCreate(db);
    }

    DB(Context context) {
        super(context, DATABASE_NAME, null, 1);
        context1 = context;
        DB_PATH = context1.getExternalFilesDir(null).getPath() + "/";

        boolean dbexist = checkdatabase();
        if (dbexist) {
            System.out.println(" Database exists.");
        } else {
            this.getReadableDatabase();
            try {
                copydatabase();

            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
        opendatabase();
    }

    private boolean checkdatabase() {

        boolean checkdb = false;
        try {
            String myPath = DB_PATH + DB_NAME;
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
    private void copydatabase() throws IOException {
        //Open local db as the input stream
        InputStream myinput = context1.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outfilename = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myoutput = new FileOutputStream(outfilename);

        // transfer byte to inputfile to outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer)) > 0) {
            myoutput.write(buffer, 0, length);
        }

        //Close the streams
        myoutput.flush();
        myoutput.close();
        myinput.close();
    }

    private void opendatabase() throws SQLException {
        String mypath = DB_PATH + DB_NAME;
        languagesDB = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    boolean insertData(String phrase) {
        long result = 0;
        String Query = "Select * from " + PHRASE_LIST_TABLE + " WHERE Phrase ='" + phrase + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            ContentValues contentValues = new ContentValues();
            contentValues.put(PHRASE, phrase);

            db.insert(PHRASE_LIST_TABLE, null, contentValues);
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
        String Query = "Select * from " + PHRASE_LIST_TABLE + " WHERE Phrase ='" + phrase + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            ContentValues contentValues = new ContentValues();
            contentValues.put(PHRASE, phrase);

            db.update(PHRASE_LIST_TABLE, contentValues, "rowid = ?", new String[]{id});
            result = 1;
        }

        cursor.close();
        return result != 0;
    }

    // external storage database methods
    Cursor getAllData() {
        Cursor res = db.rawQuery("SELECT rowid, * FROM " + PHRASE_LIST_TABLE +
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
