package com.example.foreignlanguagepractice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Translator.db";
    private static final String TABLE_NAME = "PhraseList_table";
    private static final String COL_1 = "Phrase";
    SQLiteDatabase db = this.getWritableDatabase();

    DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (Phrase TEXT PRIMARY KEY)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    boolean insertData(String phrase){
        long result = 0;
        String Query = "Select * from " + TABLE_NAME + " WHERE Phrase ='" + phrase + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_1,phrase);

            db.insert(TABLE_NAME,null,contentValues);
            result = 1;
        }
        cursor.close();
        if(result==0)
            return false;
        else
            return true;
    }

    Cursor getAllData(){
        Cursor res = db.rawQuery("SELECT rowid, * FROM "+TABLE_NAME+" ORDER BY Phrase COLLATE NOCASE ",null);
        return res;
    }

    boolean updateData(String phrase, String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,phrase);

        db.update(TABLE_NAME, contentValues, "rowid = ?",new String[]{id});
        return true;
    }

    public Cursor search(String phrase){
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE Phrase LIKE "+phrase,null);
        return res;
    }
}