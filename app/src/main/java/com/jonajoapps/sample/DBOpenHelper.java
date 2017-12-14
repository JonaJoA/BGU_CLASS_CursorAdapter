package com.jonajoapps.sample;

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
    public static final int VERSION = 2;

    //Table name
    public static final String TABLE_NAME = "notes";

    //the column names
    public static final String KEY_ID = "_id";
    public static final String KEY_DATE = "Date";
    public static final String KEY_TITLE = "Title";
    public static final String KEY_IMAGE = "Image";

    //Singelton implementation
    private static DBOpenHelper sInstance;

    public static DBOpenHelper getInstance(Context ctx) {
        if (sInstance == null) {
            sInstance = new DBOpenHelper(ctx);
        }
        return sInstance;
    }

    private DBOpenHelper(Context ctx) {
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
        sql.append(KEY_ID + " INTEGER PRIMARY KEY,");
        sql.append(KEY_DATE + " BIGINT,");
        sql.append(KEY_IMAGE + " TEXT,");
        sql.append(KEY_TITLE + " TEXT");

        sql.append(")");


        //Final SQL query     =>     create table notes(_id integer primary key, Date bigint, Title text)

        db.execSQL(sql.toString());
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
// If you need to add a column
        if (newVersion == 2 && oldVersion == 1) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + KEY_IMAGE + " TEXT");
        }
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
        cv.put(KEY_IMAGE, obj.imageUrl);

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
     * get specific row from the DB
     *
     * @param rowId
     * @return
     */
    public LineObject getRow(long rowId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + KEY_ID + "=" + rowId, null);

        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex(KEY_TITLE));
            String imageUrl = cursor.getString(cursor.getColumnIndex(KEY_IMAGE));
            long date = cursor.getLong(cursor.getColumnIndex(KEY_DATE));

            LineObject lo = new LineObject(title, date, imageUrl);

            return lo;
        }

        return null;
    }

    public int updateRow(String stringTitle, long rowId) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_TITLE, stringTitle);

        return getWritableDatabase().update(TABLE_NAME, cv, KEY_ID + "=" + rowId, null);
    }
}
