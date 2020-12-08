package com.example.carderly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Attributes;

public class WaitingRoom extends AppCompatActivity {


    ListView listView;
    Button  button;
    TextView textView;
    public static String roomName ="";
    ArrayList<String> playersList;
    ArrayAdapter<String> adapter;

    FirebaseDatabase database;
    DatabaseReference roomRef;
    int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        button = (Button) findViewById(R.id.button_startgame);
        textView = (TextView) findViewById(R.id.textView_room);
        listView = (ListView) findViewById(R.id.ListPlayers);
        playersList = new ArrayList<String>();
        adapter = new ArrayAdapter<>(WaitingRoom.this, R.layout.waiting_room_players_listview, playersList);
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        roomName = (String) intent.getSerializableExtra("roomName");

        database = FirebaseDatabase.getInstance();
        textView.setText("Room name :" + roomName);

        newPlayersEventListener();



    }

    private void newPlayersEventListener() {
        roomRef = database.getReference("rooms/" + roomName);
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                playersList.clear();
                for (DataSnapshot childrenSnapshot: dataSnapshot.getChildren()) {
                    playersList.add(childrenSnapshot.child("Name").getValue(String.class));
                }
                System.out.println(Arrays.toString(playersList.toArray()));
                ArrayAdapter<String> adapter = new ArrayAdapter<>(WaitingRoom.this, R.layout.waiting_room_players_listview, playersList);
                listView.setAdapter(adapter);
                counter = playersList.size();
                if (counter == 4){
                    Intent intent = new Intent(WaitingRoom.this, MainActivity.class);
                    intent.putExtra("listofPlayers",playersList);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

}

