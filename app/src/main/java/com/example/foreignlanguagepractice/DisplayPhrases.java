package com.example.foreignlanguagepractice;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * display saved phrases
 */
public class DisplayPhrases extends AppCompatActivity {
    Button btnDisplayPhrase;
    ListView phraseList;
    public DB phrasesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phrases);
        phrasesDatabase = new DB(this);

        btnDisplayPhrase = findViewById(R.id.btnDisplayPhrase);
        phraseList = findViewById(R.id.phrase_list);
    }

    public void viewAll(View view) {
        Cursor res = phrasesDatabase.getAllData();
        ArrayList<String> items = new ArrayList<>();

        if (res.getCount() == 0) {
            Toast.makeText(DisplayPhrases.this, "Nothing to show", Toast.LENGTH_LONG).show();
        } else {
            while (res.moveToNext()) {
                String name = res.getString(1);
                items.add(name);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
            phraseList.setAdapter(adapter);
        }
    }
}
