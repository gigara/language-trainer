package com.example.foreignlanguagepractice;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayPhrases extends AppCompatActivity {
    public static DatabaseManager phrasesDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phrases);
        phrasesDatabase = new DatabaseManager(this);
    }
}
