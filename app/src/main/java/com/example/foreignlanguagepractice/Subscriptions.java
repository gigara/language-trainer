package com.example.foreignlanguagepractice;

import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import static com.example.foreignlanguagepractice.MainActivity.phrasesDB;

/**
 * Language Subscriptions selection view
 */
public class Subscriptions extends AppCompatActivity {

    ListView languageList;
    List<String> languages = new ArrayList<>();
    List<Integer> subscriptions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        languageList = findViewById(R.id.lang_list);
        languageList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        Cursor res = phrasesDB.getAllLangs();

        if (res.getCount() != 0) {
            while (res.moveToNext()) {
                String phrase = res.getString(1);
                languages.add(phrase);

                int isSubscribed = res.getInt(3);
                subscriptions.add(isSubscribed);
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, languages);
            languageList.setAdapter(arrayAdapter);

            for (int i = 0; i < subscriptions.size(); i++) {
                if (subscriptions.get(i) == 1) {
                    languageList.setItemChecked(i, true);
                }
            }

        }
    }

    public void update(View view) {
        SparseBooleanArray arr = languageList.getCheckedItemPositions();
        for (int i = 0; i < languageList.getCount(); i++) {
            int subscribed = arr.get(i) ? 1 : 0;
            subscriptions.set(i, subscribed);
            phrasesDB.updateSubscription(String.valueOf(i + 1), subscribed);
        }
        Toast.makeText(this, "Subscriptions Updated Successfully", Toast.LENGTH_LONG).show();
    }
}