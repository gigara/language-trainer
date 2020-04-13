package com.example.foreignlanguagepractice;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * display saved phrases
 */
public class ViewAll extends AppCompatActivity {
    Button btnDisplayPhrase;
    ListView phraseList;
    public DB phrasesDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phrases);
        phrasesDB = new DB(this);

        btnDisplayPhrase = findViewById(R.id.btnDisplay);
        phraseList = findViewById(R.id.phrase_list);
    }

    public void viewAll(View view) {
        Cursor res = phrasesDB.getAllData();
        ArrayList<String> phrases = new ArrayList<>();

        if (res.getCount() == 0) {
            Toast.makeText(ViewAll.this, "Nothing to show", Toast.LENGTH_LONG).show();
        } else {
            while (res.moveToNext()) {
                String name = res.getString(1);
                phrases.add(name);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, phrases);
            phraseList.setAdapter(adapter);
        }
    }
}
