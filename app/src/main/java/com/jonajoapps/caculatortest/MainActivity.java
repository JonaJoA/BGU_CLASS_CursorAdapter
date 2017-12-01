package com.jonajoapps.caculatortest;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends Activity {

    public static final String TAG = "MainActivity";

    /**
     * the adapter that will connect our SQL DB to the listview
     */
    private SQLAdapter myAdapter;

    /**
     * The model
     * in this case we store our objects in SQL DataBase
     */
    private DBOpenHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View
        ListView listView = (ListView) findViewById(R.id.myList);
        Button add = (Button) findViewById(R.id.addButton);
        final EditText input = (EditText) findViewById(R.id.inputLine);

        //Model SQL Local DataBase
        db = new DBOpenHelper(getApplicationContext());


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Here we handle a list view item click
                Log.e(TAG, "ListView Item position:" + position + " was clicked");
            }
        });

        /**
         * Here is where we add new item to the SQL database
         */
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = input.getText().toString();
                if (str.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Cannot add empty string", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Create an object
                LineObject obj = new LineObject(str);

                //add the object to the SQLModel
                db.insertLine(obj);

                //Tell the adapter to refresh the view
                myAdapter.changeCursor(db.getAllRows(0));
            }
        });


        //Create the adapter
        myAdapter = new SQLAdapter(getApplicationContext(), db.getAllRows(0), false);

        //connect the adapter to the ListView
        listView.setAdapter(myAdapter);
    }

    /**
     * Cursor adapter
     */
    private class SQLAdapter extends CursorAdapter {

        //Helper to create view
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());

        public SQLAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        /**
         * Here we create the view that each line will be look like
         *
         * @param context
         * @param cursor
         * @param parent
         * @return the view for each line
         */
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = layoutInflater.inflate(R.layout.singel_line, parent, false);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            TextView title = (TextView) view.findViewById(R.id.title);
            TextView date = (TextView) view.findViewById(R.id.date);

            /**
             * Here we pull the info from single object stored in the DB
             */
            String titleString = cursor.getString(cursor.getColumnIndex(DBOpenHelper.KEY_TITLE));
            long aLong = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.KEY_DATE));

            /**
             * Attach the model to the view
             */
            title.setText(titleString);
            date.setText(new Date(aLong).toString());
        }
    }
}
