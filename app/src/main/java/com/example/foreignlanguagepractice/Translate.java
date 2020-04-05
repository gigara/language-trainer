package com.example.foreignlanguagepractice;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Translate extends AppCompatActivity {

    private ListView phrase;
    public static DatabaseManager phrasesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phrasesDatabase = new DatabaseManager(this);
        setContentView(R.layout.activity_translate);
        phrase = findViewById(R.id.phrase_list);
        viewAll();
    }

    public void viewAll() {
        Cursor res = phrasesDatabase.getAllData();
        ArrayList<String> items = new ArrayList<>();

        if (res.getCount() == 0) {
            Toast.makeText(Translate.this, "Nothing to show", Toast.LENGTH_LONG).show();
        } else {
            while (res.moveToNext()) {
                String name = res.getString(0);
                items.add(name);
                ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
                phrase.setAdapter(aa);
                phrase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> p1, View p2, int i, long p4) {
                        Intent intent = new Intent();
                    }
                });
            }
        }
    }
}
