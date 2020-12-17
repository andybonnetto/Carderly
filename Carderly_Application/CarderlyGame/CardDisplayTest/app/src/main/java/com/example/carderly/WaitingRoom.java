package com.example.carderly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.jar.Attributes;

public class WaitingRoom extends AppCompatActivity {


    ListView listView;
    Button  button;
    TextView textView;
    public static String roomName ="";
    public static int player_ID;
    ArrayList<String> playersList;
    ArrayAdapter<String> adapter;
    ArrayList<Integer> cards;
    private final String TAG = this.getClass().getName();

    FirebaseDatabase database;
    DatabaseReference roomRef;
    DatabaseReference playGameRef;
    private ValueEventListener Listener;
    private ValueEventListener playGameListener;
    int counter_nb_players = 0;
    boolean cards_shuffled = false;
    int play_game = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        //button = (Button) findViewById(R.id.button_startgame);
        textView = (TextView) findViewById(R.id.textView_room);
        listView = (ListView) findViewById(R.id.ListPlayers);
        playersList = new ArrayList<String>();
        adapter = new ArrayAdapter<>(WaitingRoom.this, R.layout.waiting_room_players_listview, playersList);
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        roomName = (String) intent.getSerializableExtra("roomName");
        player_ID = (int) intent.getSerializableExtra("playerID");

        database = FirebaseDatabase.getInstance();
        textView.setText("Room name :" + roomName);

        readStartGameDB();
        newPlayersEventListener();
    }

    // Needed to remove the listener (otherwise it interferes with the game). onDestroy is called by the line finish()
    public void onDestroy() {
        super.onDestroy();
        roomRef.removeEventListener(Listener);
        playGameRef.removeEventListener(playGameListener);
    }

    private void newPlayersEventListener() {
        roomRef = database.getReference("rooms/" + roomName);
        Listener = roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                playersList.clear();
                for (DataSnapshot childrenSnapshot: dataSnapshot.getChildren()) {
                    String children_name = childrenSnapshot.getKey();
                    if((children_name.equals("Player 1")) || (children_name.equals("Player 2")) || (children_name.equals("Player 3")) || (children_name.equals("Player 4")))
                        playersList.add(childrenSnapshot.child("Name").getValue(String.class));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(WaitingRoom.this, R.layout.waiting_room_players_listview, playersList);
                listView.setAdapter(adapter);
                counter_nb_players = playersList.size();
                writeIntDB(counter_nb_players,"rooms/" + roomName + "/CountPlayer");
                if((counter_nb_players == 2) && (cards_shuffled == false)){ // Only player 1 shuffles the cards and just once
                    cards = new ArrayList<>();
                    // All cards are stored in an array, and each card has an ID number of 3 digits:
                    // 1st one for the colour, and the 2 others for the value
                    // When a card is distributed, its value in the array is set to 0
                    cards.add(107); // 7 of clubs
                    cards.add(108); // 8 of clubs
                    cards.add(109); // 9 of clubs
                    cards.add(110); // 10 of clubs
                    cards.add(111); // jester of clubs
                    cards.add(112); // queen of clubs
                    cards.add(113); // king of clubs
                    cards.add(114); // ace of clubs
                    cards.add(207); // 7 of spades
                    cards.add(208); // 8 of spades
                    cards.add(209); // 9 of spades
                    cards.add(210); // 10 of spades
                    cards.add(211); // jester of spades
                    cards.add(212); // queen of spades
                    cards.add(213); // king of spades
                    cards.add(214); // ace of spades
                    cards.add(307); // 7 of diamonds
                    cards.add(308); // 8 of diamonds
                    cards.add(309); // 9 of diamonds
                    cards.add(310); // 10 of diamonds
                    cards.add(311); // jester of diamonds
                    cards.add(312); // queen of diamonds
                    cards.add(313); // king of diamonds
                    cards.add(314); // ace of diamonds
                    cards.add(407); // 7 of hearts
                    cards.add(408); // 8 of hearts
                    cards.add(409); // 9 of hearts
                    cards.add(410); // 10 of hearts
                    cards.add(411); // jester of hearts
                    cards.add(412); // queen of hearts
                    cards.add(413); // king of hearts
                    cards.add(414); // ace of hearts
                    Collections.shuffle(cards); // Cards are shuffled before being uploaded on the database
                    writeListDB(cards,"rooms/" + roomName + "/Cards");
                    cards_shuffled = true;
                    writeIntDB(cards.get(0),"rooms/" + roomName + "/Player 1/Card 1");
                    writeIntDB(cards.get(1),"rooms/" + roomName + "/Player 1/Card 2");
                    writeIntDB(cards.get(2),"rooms/" + roomName + "/Player 1/Card 3");
                    writeIntDB(cards.get(3),"rooms/" + roomName + "/Player 1/Card 4");
                    writeIntDB(cards.get(4),"rooms/" + roomName + "/Player 1/Card 5");
                    writeIntDB(cards.get(5),"rooms/" + roomName + "/Player 1/Card 6");
                    writeIntDB(cards.get(6),"rooms/" + roomName + "/Player 1/Card 7");
                    writeIntDB(cards.get(7),"rooms/" + roomName + "/Player 1/Card 8");
                }
                if ((counter_nb_players == 4) && (play_game == 1)){
                    Intent intent = new Intent(WaitingRoom.this, MainActivity.class);
                    intent.putExtra("listofPlayers",playersList);
                    intent.putExtra("playerID",player_ID);
                    intent.putExtra("roomName",roomName);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    // Write to the database
    public void writeListDB(ArrayList<Integer> cards, String location) {
        // Write to the database
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDbRef = mDatabase.getReference(location);
        mDbRef.setValue(cards).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) { // Error msg in Logcat in case the writing procedure fails
                Log.d(TAG, e.getLocalizedMessage());
            }
        });
    }

    // Write to the database
    public void writeIntDB(int id, String location) {
        // Write to the database
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDbRef = mDatabase.getReference(location);
        mDbRef.setValue(id).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) { // Error msg in Logcat in case the writing procedure fails
                Log.d(TAG, e.getLocalizedMessage());
            }
        });
    }

    private void readStartGameDB() {
        playGameRef = database.getReference("rooms/" + roomName + "/PlayGame");
        playGameListener = playGameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                play_game = dataSnapshot.getValue(int.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

}

