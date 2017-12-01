package com.jonajoapps.caculatortest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class represent our DataBase model
 * <p>
 * Created by SHAY on 11/30/2017.
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    //The database name as it will be saved
    public static final String DATA_BASE = "MyDataBase";

    //The version of the DB
    //when we want to update the DB we will set the version 1 up
    public static final int VERSION = 1;

    //Table name
    public static final String TABLE_NAME = "notes";

    //the column names
    public static final String KEY_DATE = "Date";
    public static final String KEY_TITLE = "Title";

    public DBOpenHelper(Context ctx) {
        super(ctx, DATA_BASE, null, VERSION);
    }

    /**
     * Here we make the create table call
     * this will be called when the database will need to be created
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE " + TABLE_NAME);
        sql.append("(");
        sql.append("_id" + " INTEGER PRIMARY KEY,");
        sql.append(KEY_DATE + " BIGINT,");
        sql.append(KEY_TITLE + " TEXT");

        sql.append(")");


        //Final SQL query     =>     create table notes(_id integer primary key, Date bigint, Title text)

        db.execSQL(sql.toString());
    }

    /**
     * Helper method that will help us to add items into the Database table
     *
     * @param obj
     */
    public void insertLine(LineObject obj) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_DATE, obj.date.getTime());
        cv.put(KEY_TITLE, obj.title);
        getWritableDatabase().insert(TABLE_NAME, null, cv);
    }

    /**
     * Helper method that will get us all related rows
     * here we added filter on data
     * and order by date
     *
     * @param fromDate
     * @return
     */
    public Cursor getAllRows(long fromDate) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + KEY_DATE + ">" + fromDate + " order by " + KEY_DATE, null);
        return cursor;
    }

    /**
     * will be called when the database will need to be updated
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
