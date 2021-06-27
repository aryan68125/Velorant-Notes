package com.aditya.velorannotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import static com.aditya.velorannotes.MyNotesDatabaseHelper.COLUMN_FOLDER_ID;
import static com.aditya.velorannotes.MyNotesDatabaseHelper.TABLE_NAME;

public class FolderUpdateNameActivity extends AppCompatActivity {

    Button update_folder;
    Button delete_folder;
    EditText folder_name_edit_input;

    //creating an object of MyDatabaseHelper Class
    MyDatabaseHelper mydb;

    String folder_id_String,folder_name_String,folder_content_String;

    ArrayList<String> note_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_update_name);
        update_folder = findViewById(R.id.update_folder);
        delete_folder = findViewById(R.id.delete_folder);
        folder_name_edit_input = findViewById(R.id.folder_name_edit_input);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        //First call this
        getIntentDataFromCustomAdapter();

        note_id =  new ArrayList<>();
        //getting notes_id that is to be deleted
        StoreDataInArrays();

        //now we can update the folder name when the user clicks update folder name button
        //set onclick Listener on our save changed button
        update_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Second initialize the class and call update() function from that class
                //initiating MyDatabaseHelper class
                mydb = new MyDatabaseHelper(FolderUpdateNameActivity.this);
                //get text from editText
                String folder_name_edit_input_temp_editText = folder_name_edit_input.getText().toString();
                mydb.UpdateFolderName(folder_id_String,folder_name_edit_input_temp_editText);
            }
        });

// here when deleting a folder delete the list of notes containing id of the deleted folder
        delete_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //now before you call deleteConfirmDialogBox() function which will delete the folder from folder.db
                //you need to delete all the notes containing the id of the deleted folder from notes.db

                //now you can delete the folder by calling deleteConfirmDialogBox() from folder.db
                deleteConfirmDialogBox();
            }
        });

    }

    //this functioon will create a delete confirm dialog box
    void deleteConfirmDialogBox()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.danger);
        builder.setTitle("Delete "+ folder_name_String + " ?");
        builder.setMessage("Are you sure you want to delete "+ folder_name_String+ " folder and containing notes ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //here we want to delete the all the notes containing the deleted folder folder_id
                MyNotesDatabaseHelper myNotesdb = new MyNotesDatabaseHelper(FolderUpdateNameActivity.this);
                myNotesdb.deleteAllNotesContainingTheIdOfTheFolderTobeDeleted(folder_id_String);
                //finish(); will destroy our update activity and open the main Activity
                finish();
                overridePendingTransition(R.anim.nothing, R.anim.slide_out_right);

                //the code below will delete the folder from folder.db
                //create MyDatabaseHelper Class object so that we can use the function deleteOneRowFromTheTableOfDatabase(String row_id)
                //from MyDatabaseHelper class
                MyDatabaseHelper mydb = new MyDatabaseHelper(FolderUpdateNameActivity.this);
                mydb.deleteOneFolderRowFromTheTableOfDatabase(folder_id_String);
                //finish(); will destroy our update activity and open the main Activity
                finish();
                overridePendingTransition(R.anim.nothing, R.anim.slide_out_right);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing if no is pressed by the user in the delete dialogue box
            }
        });
        builder.create().show();
    }

    //reading notes from database having same folder id as the selected folder in main activity
    public Cursor call_notes_via_folder_id()
    {
        //create MyDatabaseHelper class object
        MyNotesDatabaseHelper MyNotesDB = new MyNotesDatabaseHelper(FolderUpdateNameActivity.this);
        //getting data from
        String Folder_ID = folder_id_String;
        //selecting all the data from our database table
        String query = "SELECT * FROM "+ TABLE_NAME + " WHERE " +COLUMN_FOLDER_ID+ " = "+Folder_ID;
        //creaing an sql database object and get readable database this time
        SQLiteDatabase db = MyNotesDB.getReadableDatabase();
        //creating a cursor object
        Cursor cursor = null;
        if(db!=null)
        {
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    void StoreDataInArrays()
    {
        //here save note in database

        //calling ReadAllData() method or function from MyDatabaseHelper class
        Cursor cursor = call_notes_via_folder_id();
        if(cursor.getCount() == 0) //that means there is no data
        {
            Toast.makeText(this,"Error occured while retrieving data",Toast.LENGTH_SHORT).show();
        }
        else
        {
            //move the cursor to the next position in the table
            //adding data from our database to our arrays
            while(cursor.moveToNext())
            {
                //here we will get all the notes containing the id of the folder to be deleted
                note_id.add(cursor.getString(0));
            }
        }
    }// DisplayData function closed

    //get data from the CustomAdapter Layout via intent
    void getIntentDataFromCustomAdapter()
    {
        if(getIntent().hasExtra("id_folder_update") && getIntent().hasExtra("folder_name_folder_update") && getIntent().hasExtra("folder_content_folder_update"))
        {
            //get data from CustomAdapter class via Intent
            folder_id_String = getIntent().getStringExtra("id_folder_update");
            folder_name_String = getIntent().getStringExtra("folder_name_folder_update");
            folder_content_String = getIntent().getStringExtra("folder_content_folder_update");

            //setting data to the editText
            folder_name_edit_input.setText(folder_name_String);
            Log.i("folder_id",folder_id_String);
            Log.i("folder_name",folder_name_String);
            Log.i("folder_contents",folder_content_String);
        }
        else
        {
            Toast.makeText(this,"No data Err A113 000x2273821",Toast.LENGTH_SHORT).show();
        }
    }
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