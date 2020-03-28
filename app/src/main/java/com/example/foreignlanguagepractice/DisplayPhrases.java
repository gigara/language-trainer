package com.example.foreignlanguagepractice;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DisplayPhrases extends AppCompatActivity {
    Button btnDisplayPhrase;
    ListView phraseList;
    public static DatabaseManager phrasesDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phrases);
        phrasesDatabase = new DatabaseManager(this);

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
                String name = res.getString(0);
                items.add(name);
                ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
                phraseList.setAdapter(aa);
                phraseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> p1, View p2, int i, long p4) {
                        Intent intent = new Intent();
                        finish();
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
