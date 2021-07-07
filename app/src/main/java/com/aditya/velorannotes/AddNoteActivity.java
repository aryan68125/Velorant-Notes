package com.aditya.velorannotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {

    EditText Note_title_input;
    EditText note_content_input;
    String folder_id_String;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Note_title_input = findViewById(R.id.Note_title_input);
        note_content_input = findViewById(R.id.note_content_input);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        //get folderId from ViewFolderContentsActivity
        getFolderId();

    }

    //this function will get folder id from FolderViewContentsActivity
    public void getFolderId()
    {
        if(getIntent().hasExtra("folder_id"))
        {
            //get data from CustomAdapter class via Intent
            folder_id_String = getIntent().getStringExtra("folder_id");


            //setting data to the editText
            Log.i("folder_id",folder_id_String);
        }
        else
        {
            Toast.makeText(this,"No data Err A113 000x2273821",Toast.LENGTH_SHORT).show();
        }
    }

    //this method handles back button of android os
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out_right);

    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_note_update_menu, menu);
        return true;
    }

    //it will handle the back button in the tool bar
    // this event will enable the back activity action bar back button
    // function to the button on press activity action bar back button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //android.R.id.home = default id for back button
        if(item.getItemId()==android.R.id.home){
            this.finish();
            overridePendingTransition(R.anim.nothing, R.anim.slide_out_right);
            return true;
        }
        else if(item.getItemId() == R.id.saveNote)
        {
            //here save note in database
            //create MyDatabaseHelper class object
            MyNotesDatabaseHelper MyNotesDB = new MyNotesDatabaseHelper(AddNoteActivity.this);

            //getting data from our three editText
            String Note_name_AddActivity = Note_title_input.getText().toString();
            String Note_content_AddActivity = note_content_input.getText().toString();
            int Folder_id = Integer.parseInt(folder_id_String);

            //now we can call addPassword method from our MyDatabaseHelper class
            MyNotesDB.addNote(Folder_id,Note_name_AddActivity, Note_content_AddActivity);
            Note_title_input.getText().clear();
            note_content_input.getText().clear();
        }
        return super.onOptionsItemSelected(item);
    }
}