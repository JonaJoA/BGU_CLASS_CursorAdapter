package com.jonajoapps.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jonajoapps.caculatortest.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class MainActivity extends Activity {

    public static final String TAG = "MainActivity";
    public static final int REQUEST_CODE = 1;

    enum ActivityState {
        UNKNOWN,
        START,
        STOP
    }

    public static ActivityState getmCurrentState() {
        return mCurrentState;
    }

    private static ActivityState mCurrentState = ActivityState.UNKNOWN;


    /**
     * the adapter that will connect our SQL DB to the listview
     */
    private SQLAdapter myAdapter;

    /**
     * The model
     * in this case we store our objects in SQL DataBase
     */
    private DBOpenHelper db;
    private int index = 0;
    private BroadcastReceiver receiver;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View
        ListView listView = (ListView) findViewById(R.id.myList);
        Button add = (Button) findViewById(R.id.addButton);
        final EditText input = (EditText) findViewById(R.id.inputLine);


        Button startService = (Button) findViewById(R.id.startService);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(9);
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), MyIntentService.class);

                startService(in);
            }
        });
        //Model SQL Local DataBase
        db = DBOpenHelper.getInstance(getApplicationContext());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Here we handle a list view item click

                //we create an intent to open EditActivity
                //and we pass the row id parameter
                Log.e(TAG, "ListView Item position:" + position + " was clicked");
                Intent in = new Intent(getApplicationContext(), EditActivity.class);
                in.putExtra(EditActivity.ROW_ID, id);

                startActivityForResult(in, REQUEST_CODE);
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
                    Toast.makeText(getApplicationContext(), "Cannot add empty title", Toast.LENGTH_SHORT).show();

                    index++;
                    return;
                }

                //Create an object
                LineObject obj = new LineObject(str);
                obj.imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRmtR0UO-4S6W3YhU96Lt-BY1plpfWNQ7HXh2S9hFWit460fhur";
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

        /**
         *shared prefernces example
         * if there was no value set in firstStart key then show this dialog every time
         */
        final SharedPreferences sp = getSharedPreferences("Settings", MODE_PRIVATE);
        final String key = "firstStart";

        long first = sp.getLong(key, 0);
        if (first == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("First launch");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sp.edit().putLong(key, System.currentTimeMillis()).apply();
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("Keep show me this dialog", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCurrentState = ActivityState.START;

        IntentFilter filter = new IntentFilter();
        filter.addAction(MyIntentService.FILE_DOWNLOAD_Q_ACTION);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int index = intent.getIntExtra(MyIntentService.CURRENT, 0);
                progressBar.setProgress(index);
                if (index == 9) {
                    Toast.makeText(getApplicationContext(), "DONE Download", Toast.LENGTH_LONG).show();
                }
            }
        };

        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCurrentState = ActivityState.STOP;

        unregisterReceiver(receiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            //this will get called when EditActivity will finish working correctly
            myAdapter.changeCursor(db.getAllRows(0));
        }
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
            ImageView imageView = (ImageView) findViewById(R.id.image);

            /**
             * Here we pull the info from single object stored in the DB
             */
            String titleString = cursor.getString(cursor.getColumnIndex(DBOpenHelper.KEY_TITLE));
            final String imageUrl = cursor.getString(cursor.getColumnIndex(DBOpenHelper.KEY_IMAGE));
            long aLong = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.KEY_DATE));

            /**
             * Attach the model to the view
             */
            title.setText(titleString);
            date.setText(new Date(aLong).toString());

            getImageFromNetwork(imageView, imageUrl);
        }

        private void getImageFromNetwork(final ImageView imageView, final String imageUrl) {
            new Thread("ImageThread") {
                @Override
                public void run() {
                    try {
                        URL url = new URL(imageUrl);
                        final Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(image);
                            }
                        });
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            }.start();
        }
    }
}
