package com.example.carderly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoomChoice extends AppCompatActivity {

    ListView listView;
    Button button;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    public static String usernameInput="";
    private final String TAG = this.getClass().getName();

    List<String> roomsList;
    String playerName = "";
    String roomName = "";

    FirebaseDatabase database;
    DatabaseReference roomRef;
    DatabaseReference roomsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_choice);


        listView = findViewById(R.id.ListRoom);
        button = (Button) findViewById(R.id.button_createRoom);
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(RoomChoice.this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        usernameInput = (String) intent.getSerializableExtra("username");


        playerName = usernameInput;
        roomName = playerName;

        listView = findViewById(R.id.ListRoom);
        button = findViewById(R.id.button_createRoom);

        // all existing available rooms
        roomsList = new ArrayList<>();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create room and add yourself as player1
                button.setText("CREATING ROOM");
                button.setEnabled(false);
                roomName = playerName;
                roomRef = database.getReference("rooms/" + roomName + "/player1");
                addRoomEventListener();
                roomRef.setValue(playerName);

            }

        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //join existing room and add yourself
                roomName = roomsList.get(position);
                roomRef = database.getReference("rooms/" + roomName);
                addRoomEventListener();
                roomRef.setValue(playerName);
            }
        });
        //show if new room is available
        addRoomsEventListener();

    }


    private void addRoomEventListener() {
        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //join the room
                button.setText("CREATE ROOM");
                button.setEnabled(true);
                long count = snapshot.child(roomName).getChildrenCount();
                if(count ==1){
                    roomRef = database.getReference("rooms/" + roomName + "/player2");
                    roomRef.setValue(playerName);
                }else if (count ==2){
                    roomRef = database.getReference("rooms/" + roomName + "/player3");
                    roomRef.setValue(playerName);
                }else if (count ==3){
                    roomRef = database.getReference("rooms/" + roomName + "/player4");
                    roomRef.setValue(playerName);
                }else if (count > 3){
                    Message.message(getApplicationContext(), "Player 1 doesn't want to play with you");
                }
                Intent intent = new Intent(getApplicationContext(), WaitingRoom.class);
                intent.putExtra("roomName", roomName);
                startActivity(intent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //error
                button.setText("CREATE ROOM");
                button.setEnabled(true);
                Toast.makeText(RoomChoice.this, "Error!", Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void addRoomsEventListener() {
        roomsRef = database.getReference("rooms");
        roomsRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //show list of rooms
                roomsList.clear();
                Iterable<DataSnapshot> rooms = dataSnapshot.getChildren();
                for (DataSnapshot snapshot : rooms) {
                    roomsList.add(snapshot.getKey());

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(RoomChoice.this,
                            android.R.layout.simple_list_item_1, roomsList);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //error - nothing
            }
        });
    }



}






