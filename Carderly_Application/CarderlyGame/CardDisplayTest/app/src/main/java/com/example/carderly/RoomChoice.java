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
    DatabaseReference roomsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_choice);


        listView = (ListView) findViewById(R.id.ListRoom);
        button = (Button) findViewById(R.id.button_createRoom);
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(RoomChoice.this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        usernameInput = (String) intent.getSerializableExtra("username");


        playerName = usernameInput;
        // boolean used to avoid a player joining an existing room to be inside the room multiple times
        final boolean[] player_joined = {false};
        roomName = playerName;

        // all existing available rooms
        roomsList = new ArrayList<>();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create room and add yourself as player1
                button.setText("CREATING ROOM");
                button.setEnabled(false);
                writeStringDB(playerName,"rooms/" + playerName + "/player1" + "/Name");
                Intent intent = new Intent(getApplicationContext(), WaitingRoom.class);
                intent.putExtra("roomName", roomName);
                intent.putExtra("playerID",1);
                startActivity(intent);
            }

        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //join existing room and add yourself
                roomName = roomsList.get(position);
                // Adding the player to an existing room: he will be player 2,3, or 4 depending on how many players already joined the room
                getRoomNbPlayersDB(new RoomChoice.RoomNbPlayersCallback() {
                    @Override
                    public void onCallback(long room_nb_players) {
                        // Once the player has joined the room, this code will be run again as the DB has been modified
                        // To avoid the player being registered inside the room multiple times, we check the boolean
                        if((room_nb_players ==1) && (player_joined[0] == false)){
                            writeStringDB(playerName,"rooms/" + roomName + "/player2" + "/Name");
                            player_joined[0] = true; // The boolean must be an array to work inside this callback (no idea why?)
                            Intent intent = new Intent(getApplicationContext(), WaitingRoom.class);
                            intent.putExtra("roomName", roomName);
                            intent.putExtra("playerID",2);
                            startActivity(intent);
                        }else if ((room_nb_players ==2) && (player_joined[0] == false)){
                            writeStringDB(playerName,"rooms/" + roomName + "/player3" + "/Name");
                            player_joined[0] = true;
                            Intent intent = new Intent(getApplicationContext(), WaitingRoom.class);
                            intent.putExtra("roomName", roomName);
                            intent.putExtra("playerID",3);
                            startActivity(intent);
                        }else if ((room_nb_players ==3) && (player_joined[0] == false)){
                            writeStringDB(playerName,"rooms/" + roomName + "/player4" + "/Name");
                            player_joined[0] = true;
                            Intent intent = new Intent(getApplicationContext(), WaitingRoom.class);
                            intent.putExtra("roomName", roomName);
                            intent.putExtra("playerID",4);
                            startActivity(intent);
                        }else {
                            Message.message(getApplicationContext(), "Player 1 doesn't want to play with you");
                        }
                    }
                },"rooms/" + roomName);
            }
        });
        //show if new room is available
        addRoomsEventListener();

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

    // Write a string to the database
    public void writeStringDB(String string, String location) {
        // Write to the database
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDbRef = mDatabase.getReference(location);
        mDbRef.setValue(string).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) { // Error msg in Logcat in case the writing procedure fails
                Log.d(TAG, e.getLocalizedMessage());
            }
        });
    }

    // Callback needed to retrieve data from the DB
    public interface RoomNbPlayersCallback {
        void onCallback(long value);
    }

    // Get nb of players already in a selected room from the database
    public void getRoomNbPlayersDB(final RoomNbPlayersCallback myCallback, String location) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDbRef = mDatabase.getReference(location);
        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long room_nb_players = dataSnapshot.getChildrenCount();
                myCallback.onCallback(room_nb_players);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

}






