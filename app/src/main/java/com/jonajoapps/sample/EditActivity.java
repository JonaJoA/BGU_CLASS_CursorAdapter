package com.jonajoapps.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jonajoapps.caculatortest.R;

public class EditActivity extends AppCompatActivity {
    public static final String ROW_ID = "rowId";
    DBOpenHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        TextView title = (TextView) findViewById(R.id.rowTitle);
        TextView date = (TextView) findViewById(R.id.rowDate);
        final EditText editTitle = (EditText) findViewById(R.id.editRowTitle);
        Button editButton = (Button) findViewById(R.id.editButton);

        Intent in = getIntent();
        final long rowId = in.getLongExtra(ROW_ID, -1);


        if (rowId == -1) {
            finish();
            return;
        }

        db = DBOpenHelper.getInstance(getApplicationContext());

        LineObject obj = db.getRow(rowId);

        title.setText(obj.title);
        date.setText(obj.date.toString());

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringTitle = editTitle.getText().toString();

                if (!stringTitle.isEmpty()) {
                    db.updateRow(stringTitle, rowId);
                    setResult(RESULT_OK);
                    finish();
                    return;
                }

                Toast.makeText(getApplicationContext(), "Title string cannot be empty", Toast.LENGTH_LONG).show();
            }
        });
    }
}
