package com.jonajoapps.caculatortest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SHAY on 11/30/2017.
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    public static final String DATA_BASE = "MyDataBase";
    public static final int VERSION = 1;
    public static final String KEY_DATE = "Date";
    public static final String KEY_TITLE = "Title";
    public static final String TABLE_NAME = "notes";

    public DBOpenHelper(Context ctx) {
        super(ctx, DATA_BASE, null, VERSION);
    }

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATA_BASE, factory, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE " + TABLE_NAME);
        sql.append("(");
        sql.append("_id" + " INTEGER PRIMARY KEY,");
        sql.append(KEY_DATE + " BIGINT,");
        sql.append(KEY_TITLE + " TEXT");

        sql.append(")");
        db.execSQL(sql.toString());
    }

    public void insertLine(LineObject obj) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_DATE, obj.date.getTime());
        cv.put(KEY_TITLE, obj.title);
        getWritableDatabase().insert(TABLE_NAME, null, cv);
    }

    public Cursor getAllRows(long fromDate) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + KEY_DATE + ">" + fromDate + " order by " + KEY_DATE, null);
        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
