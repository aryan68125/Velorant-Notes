package com.aditya.velorannotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //initialize MyDatabaseHelper class
    MyDatabaseHelper myDB;

    //custom Adapter object of CustomAdapter java class
    CustomAdapter customAdapter;

    //now create an array list to hold the list of id,titleOfBooks,Author_Names,NumberOf_Pages from our Database
    ArrayList<String> folder_id,folder_title,folder_contents;

    FloatingActionButton Add_Button ;
    RecyclerView recylerView;

    ImageView empty_imageView2;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recylerView = findViewById(R.id.recylerView);

        Add_Button = (FloatingActionButton) findViewById(R.id.Add_Button);

        textView = findViewById(R.id.textView);
        //making the textView invisible
        textView.setVisibility(View.INVISIBLE);

        empty_imageView2 = findViewById(R.id.empty_imageView2);
        //making th imageView invisible
        empty_imageView2.setVisibility(View.INVISIBLE);

        //now setting up an onclick listener for our floationg button
        Add_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here we will open a new activity add activity
                Intent intent = new Intent(MainActivity.this, AddFolderActivity.class);
                startActivity(intent);
                //activity transition animation
                overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);
            }
        });

        myDB = new MyDatabaseHelper(MainActivity.this);
        folder_id =  new ArrayList<>();
        folder_title =  new ArrayList<>();
        folder_contents =  new ArrayList<>();

        //calling StoreDataInArrays() function
        StoreDataInArrays();

        //initialize our custom Adapter object
        /*
        customAdapter = new CustomAdapter(activity: MainActivity.this,context: this,book_id,book_title,book_author,book_page);
        the above line will refresh the recylerView in our main Activity by updating the incoming data from CustomAdapter class
         */
        customAdapter = new CustomAdapter(MainActivity.this,this,folder_id,folder_title,folder_contents);
        recylerView.setAdapter(customAdapter);
        recylerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        //........Item swipe implementation on a recylerView items..........//
        //attach SimpleCallback To our recyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simplecallback);
        itemTouchHelper.attachToRecyclerView(recylerView);
    }

    //refreshing the recylerView in our main activity with new data when mainActivity restarts
    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

    //refreshing the RecylerView in our main activity with new data
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1)
        {   //no finally this is the method that refreshes our main activity
            recreate();
        }
    }

    void StoreDataInArrays()
    {
        //calling ReadAllData() method or function from MyDatabaseHelper class
        Cursor cursor = myDB.ReadAllData();
        if(cursor.getCount() == 0) //that means there is no data
        {
            empty_imageView2.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
        else
        {
            //move the cursor to the next position in the table
            //adding data from our database to our arrays
            while(cursor.moveToNext())
            {
                folder_id.add(cursor.getString(0));
                folder_title.add(cursor.getString(1));
                folder_contents.add(cursor.getString(2));
            }
        }
    }// DisplayData function closed

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    //it will handle the back button in the tool bar
    // this event will enable the back activity action bar back button
    // function to the button on press activity action bar back button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.Dev_info)
        {
            Intent intent = new Intent(MainActivity.this,DevActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        return super.onOptionsItemSelected(item);
    }

    //........Item swipe implementation on a recylerView items..........//
    //these variables will hold the data incase if user press undo button
    String deleted_folder_id=null;
    String Deleted_folder_title=null;
    int position;
    //flag for refreshing the recylerView when user plress no in delete dialog box
    int flag = 0; //0 for no is not pressed //1 = no button is pressed in dialogue box

    //for swiping operation always put drag Dirs - 0
    //Enable left swipe to edit
    //Enable right swipe to Delete
    ItemTouchHelper.SimpleCallback simplecallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT)
    {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            //this method is only needed when you want to rearrange the rows inside the recyclerView
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            //getting the position of the row of the recyclerView
            position = viewHolder.getAdapterPosition();

            //this method is used for handeling the swipe
            switch(direction)
            {
                case ItemTouchHelper.RIGHT:
                    //add delete note logic
                    //open a delete dialog box
                    //add material library in gradel file so that we can use Snackbar inorder to give the user undo button option
                    //implementation 'com.google.android.material:material:<version>'

                    //save the data inside the backup variable before the delete function takes place
                    //getting data from ArrayList
                    deleted_folder_id = folder_id.get(position);
                    Deleted_folder_title = folder_title.get(position);

                    //now call te delete function from notesDatabaseHelper class
                    deleteConfirmDialogBox();

                    //now perform delete operation from the database
                    folder_id.remove(position);
                    folder_title.remove(position);
                    folder_contents.remove(position);

                    customAdapter.notifyItemRemoved(position);
                    //send notification to the user

                    Log.i("swipe","Right swipe done");
                    break;
            }

        }
    };

    //this functioon will create a delete confirm dialog box
    void deleteConfirmDialogBox()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.danger);
        builder.setTitle("Delete "+ Deleted_folder_title + " ?");
        builder.setMessage("Are you sure you want to delete "+ Deleted_folder_title+ " folder and containing notes ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //here we want to delete the all the notes containing the deleted folder folder_id from notes.db
                MyNotesDatabaseHelper myNotesdb = new MyNotesDatabaseHelper(MainActivity.this);
                myNotesdb.deleteAllNotesContainingTheIdOfTheFolderTobeDeleted(deleted_folder_id);

                //the code below will delete the folder from folder.db
                //create MyDatabaseHelper Class object so that we can use the function deleteOneRowFromTheTableOfDatabase(String row_id)
                //from MyDatabaseHelper class
                MyDatabaseHelper mydb = new MyDatabaseHelper(MainActivity.this);
                mydb.deleteOneFolderRowFromTheTableOfDatabase(deleted_folder_id);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing if no is pressed by the user in the delete dialogue box
                //recreate(); instead of recreate() use the code below to make the user experience a whole lot better
                folder_id.add(position,deleted_folder_id);
                folder_title.add(position,Deleted_folder_title);
                customAdapter.notifyItemInserted(position);
                recylerView.scrollToPosition(position);
            }
        });
        builder.create().show();
    }

}