package com.example.foreignlanguagepractice;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import static com.example.foreignlanguagepractice.MainActivity.phraseDatabase;

/**
 * edit phrase activity
 */
public class EditPhrases extends AppCompatActivity {
    ArrayList<String> items = new ArrayList<>();
    ArrayList<Integer> itemIds = new ArrayList<>();
    ListView checkList;
    ArrayList<String> selectedItems;
    EditText etEditPhrase;
    Button btnEditPhraseEdit;
    int selectedItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phrases);
        checkList = findViewById(R.id.checkable_list);
        etEditPhrase = findViewById(R.id.etEditPhrase);
        btnEditPhraseEdit = findViewById(R.id.btnEditPhraseEdit);
        checkList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        selectedItems = new ArrayList<>();

        Cursor res = phraseDatabase.getAllData();

        if (res.getCount() == 0) {
            Toast.makeText(EditPhrases.this, "Nothing to show", Toast.LENGTH_LONG).show();
        } else {
            while (res.moveToNext()) {
                String phrase = res.getString(1);
                items.add(phrase);

                int itemId = res.getInt(0);
                itemIds.add(itemId);
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_single_choice, items);
            checkList.setAdapter(arrayAdapter);
            checkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // selected item
                    selectedItem = position;
                }
            });
        }

    }

    /**
     * edit the selected phrase
     * @param view selected phrase
     */
    public void edit(View view) {
        if (selectedItem != -1) {
            etEditPhrase.setText(items.get(selectedItem));
        } else {
            Toast.makeText(EditPhrases.this, "Please Select a Phrase to edit",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * update & save to the database
     * @param v text
     */
    public void update(View v){
        if (selectedItem == -1) {
            Toast.makeText(EditPhrases.this, "Please Select a Phrase to edit",
                    Toast.LENGTH_LONG).show();
            return;
        }
        boolean isUpdate = phraseDatabase.updateData(etEditPhrase.getText().toString(),
                itemIds.get(selectedItem).toString());
        if (isUpdate) {
            Toast.makeText(EditPhrases.this, "Updated", Toast.LENGTH_LONG).show();

            // refresh the page
            Intent intent = getIntent();
            finish();
            startActivity(intent);

        } else {
            Toast.makeText(EditPhrases.this, "Not Updated", Toast.LENGTH_LONG).show();

        }
    }

}
