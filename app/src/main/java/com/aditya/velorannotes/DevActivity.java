package com.aditya.velorannotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DevActivity extends AppCompatActivity {
    ListView listView;
    TextView textView4;

    MediaPlayer mediaPlayer; //will play sound when something is selected from the listView


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev);

        textView4 = findViewById(R.id.textView4);
        listView = findViewById(R.id.listView);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        String [] info ={"Name","Branch","College Name", "college code", "Roll number", "course"};
        ListAdapter items = new ArrayAdapter<String>(this, R.layout.row, info);
        listView.setAdapter(items);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    textView4.setText("Aditya Kumar");
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.name);
                    mediaPlayer.start();
                }
                else if(position==1)
                {
                    textView4.setText("Computer science");
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.branch);
                    mediaPlayer.start();
                }
                else if(position==2)
                {
                    textView4.setText("Saroj Institute of Technology");
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.college_name);
                    mediaPlayer.start();
                }
                else if(position == 3)
                {
                    textView4.setText("123");
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.college_code);
                    mediaPlayer.start();
                }
                else if(position==4)
                {
                    textView4.setText("1901230100001");
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.roll_number);
                    mediaPlayer.start();
                }
                else if(position==5)
                {
                    textView4.setText("B.tech");
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.course);
                    mediaPlayer.start();
                }
            }
        });

    }


    // this event will enable the back activity action bar back button
    // function to the button on press activity action bar back button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //this method handles back button of android os
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

}