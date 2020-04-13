package com.example.foreignlanguagepractice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static DB phrasesDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phrasesDB = new DB(this);

        Button btnAddPhrases = findViewById(R.id.addPhrases);
        Button btnDisplayPhrases = findViewById(R.id.displayPhrases);
        Button btnEditPhrases = findViewById(R.id.editPhrases);
        Button btnLanguageSubscription = findViewById(R.id.subscriptions);
        Button btnTranslate = findViewById(R.id.translate);
        Button btnOffline = findViewById(R.id.downloads);


        btnAddPhrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), AddPhrases.class);
                startActivity(startIntent);
            }
        });

        btnDisplayPhrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), ViewAll.class);
                startActivity(startIntent);
            }
        });

        btnEditPhrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), EditPhrases.class);
                startActivity(startIntent);
            }
        });

        btnLanguageSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), Subscriptions.class);
                startActivity(startIntent);
            }
        });

        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), Translate.class);
                startActivity(startIntent);
            }
        });

        btnOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), Download.class);
                startActivity(startIntent);
            }
        });
    }
}
