package com.aditya.velorannotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
}