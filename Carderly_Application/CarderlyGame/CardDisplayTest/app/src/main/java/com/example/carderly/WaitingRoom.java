package com.example.carderly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.jar.Attributes;

public class WaitingRoom extends AppCompatActivity {


    ListView listView;
    Button  button;
    TextView textView;
    public static String roomName ="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        button = findViewById(R.id.button_startgame);
        textView = findViewById(R.id.textView_room);

        Intent intent = getIntent();
        roomName = (String) intent.getSerializableExtra("roomName");
        textView.setText("Room name :" + roomName);

        //button.setOnClickListener(new View.OnClickListener() {
           // @Override
           // public void onClick(View v) {
                //start game
               // button.setText("Starting game");
               // button.setEnabled(true);



            //}

        //});





    }



}

