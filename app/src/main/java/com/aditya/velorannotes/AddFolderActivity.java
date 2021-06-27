package com.aditya.velorannotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddFolderActivity extends AppCompatActivity {

    EditText folder_name_input;
    Button create_folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_folder);
        folder_name_input = findViewById(R.id.folder_name_input);
        create_folder = findViewById(R.id.create_folder);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        //set onclick Listener on our create button folder
        create_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create MyDatabaseHelper class object
                MyDatabaseHelper MyDB = new MyDatabaseHelper(AddFolderActivity.this);

                //getting data from our three editText
                String folder_name_AddActivity = folder_name_input.getText().toString();

                //now we can call addPassword method from our MyDatabaseHelper class
                MyDB.addFolder(folder_name_AddActivity);
                folder_name_input.getText().clear();
            }
        });

    }//oncreate function closed

    // this event will enable the back activity action bar back button
    // function to the button on press activity action bar back button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                overridePendingTransition(R.anim.nothing, R.anim.slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //this method handles back button of android os
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out_right);

    }
}