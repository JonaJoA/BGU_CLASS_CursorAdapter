package com.jonajoapps.caculatortest;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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

    private TextView result;

    private SQLAdapter myAdapter;
    private DBOpenHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getSharedPreferences("settings", MODE_PRIVATE);
        db = new DBOpenHelper(getApplicationContext());

        sp.edit().putString("shay", "shay test").apply();

        //View
        ListView listView = (ListView) findViewById(R.id.myList);
        Button add = (Button) findViewById(R.id.addButton);
        final EditText input = (EditText) findViewById(R.id.inputLine);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = input.getText().toString();
                if (str.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Cannot add empty string", Toast.LENGTH_SHORT).show();
                    return;
                }

                LineObject obj = new LineObject(str);
                db.insertLine(obj);

                myAdapter.changeCursor(db.getAllRows(0));
            }
        });

        //Model
        myAdapter = new SQLAdapter(getApplicationContext(), db.getAllRows(0), false);

        listView.setAdapter(myAdapter);
    }

    private class SQLAdapter extends CursorAdapter {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());

        public SQLAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View convertView = layoutInflater.inflate(R.layout.singel_line, parent, false);
            return convertView;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView title = (TextView) view.findViewById(R.id.title);
            TextView date = (TextView) view.findViewById(R.id.date);

            title.setText(cursor.getString(cursor.getColumnIndex(DBOpenHelper.KEY_TITLE)));
            long aLong = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.KEY_DATE));
            date.setText(new Date(aLong).toString());
        }
    }


//    private class CustomAdapter extends BaseAdapter {
//
//        private final LayoutInflater layoutInflater;
//
//        public CustomAdapter() {
//            layoutInflater = LayoutInflater.from(getApplicationContext());
//        }
//
//        @Override
//        public int getCount() {
//            return model.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            Log.e("GetView", "position " + position);
//            if (convertView == null) {
//                convertView = layoutInflater.inflate(R.layout.singel_line, parent, false);
//            }
//
//            TextView title = (TextView) convertView.findViewById(R.id.title);
//            TextView date = (TextView) convertView.findViewById(R.id.date);
//
//            title.setText(model.get(position).title);
//            date.setText(model.get(position).date.toString());
//
//            return convertView;
//        }
//    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("Config changed", newConfig.toString());
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        } else {

        }

    }
}
