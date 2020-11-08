package com.example.carderly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView card1, card2, card3, card4, card5, card6, card7, card8, opponent_left, opponent_right, ally, played_card, opponent_left_played_card, opponent_right_played_card, ally_played_card;
    TextView name_player, name_opponent_left, name_opponent_right, name_ally;
    Button game_button;
    ArrayList<Integer> cards;
    ArrayList<String> strings_DB;
    int player_id; // ID of the player
    int[] player_cards_id = {0,0,0,0,0,0,0,0}; // ID of the cards in hand of the player 1
    int[] other_players_card_played_id = {0,0,0}; // ID of the cards played by the other players : 1st the opponent left, 2nd the ally and 3rd the right opponent
    private final String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link the variables modified in the java file to the Views in the UI
        game_button = (Button) findViewById(R.id.game_button);
        card1 = (ImageView) findViewById(R.id.card1);
        card2 = (ImageView) findViewById(R.id.card2);
        card3 = (ImageView) findViewById(R.id.card3);
        card4 = (ImageView) findViewById(R.id.card4);
        card5 = (ImageView) findViewById(R.id.card5);
        card6 = (ImageView) findViewById(R.id.card6);
        card7 = (ImageView) findViewById(R.id.card7);
        card8 = (ImageView) findViewById(R.id.card8);
        opponent_left = (ImageView) findViewById(R.id.opponent_left);
        opponent_left_played_card = (ImageView) findViewById(R.id.opponent_left_played_card);
        opponent_right = (ImageView) findViewById(R.id.opponent_right);
        opponent_right_played_card = (ImageView) findViewById(R.id.opponent_right_played_card);
        ally = (ImageView) findViewById(R.id.ally);
        ally_played_card = (ImageView) findViewById(R.id.ally_played_card);
        played_card = (ImageView) findViewById(R.id.played_card);
        name_player = (TextView) findViewById(R.id.name_player);
        name_opponent_left = (TextView) findViewById(R.id.name_opponent_left);
        name_opponent_right = (TextView) findViewById(R.id.name_opponent_right);
        name_ally = (TextView) findViewById(R.id.name_ally);

        // Get through the intent that started the activity the ID of the player
        //Intent intent = getIntent();
        //String player_ID = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        player_id = 3;
        strings_DB = new ArrayList<>();
        switch(player_id) { // Locations where the values will be stored in the database
            case 1:
                strings_DB.add("Player 1/Card 1");
                strings_DB.add("Player 1/Card 2");
                strings_DB.add("Player 1/Card 3");
                strings_DB.add("Player 1/Card 4");
                strings_DB.add("Player 1/Card 5");
                strings_DB.add("Player 1/Card 6");
                strings_DB.add("Player 1/Card 7");
                strings_DB.add("Player 1/Card 8");
                strings_DB.add("Player 1/Card played");
                strings_DB.add("Player 3/Card played");
                strings_DB.add("Player 2/Card played");
                strings_DB.add("Player 4/Card played");
                name_player.setText("Player 1");
                name_opponent_left.setText("Player 3");
                name_ally.setText("Player 2");
                name_opponent_right.setText("Player 4");
                break;
            case 2:
                strings_DB.add("Player 2/Card 1");
                strings_DB.add("Player 2/Card 2");
                strings_DB.add("Player 2/Card 3");
                strings_DB.add("Player 2/Card 4");
                strings_DB.add("Player 2/Card 5");
                strings_DB.add("Player 2/Card 6");
                strings_DB.add("Player 2/Card 7");
                strings_DB.add("Player 2/Card 8");
                strings_DB.add("Player 2/Card played");
                strings_DB.add("Player 4/Card played");
                strings_DB.add("Player 1/Card played");
                strings_DB.add("Player 3/Card played");
                name_player.setText("Player 2");
                name_opponent_left.setText("Player 4");
                name_ally.setText("Player 1");
                name_opponent_right.setText("Player 3");
                break;
            case 3:
                strings_DB.add("Player 3/Card 1");
                strings_DB.add("Player 3/Card 2");
                strings_DB.add("Player 3/Card 3");
                strings_DB.add("Player 3/Card 4");
                strings_DB.add("Player 3/Card 5");
                strings_DB.add("Player 3/Card 6");
                strings_DB.add("Player 3/Card 7");
                strings_DB.add("Player 3/Card 8");
                strings_DB.add("Player 3/Card played");
                strings_DB.add("Player 2/Card played");
                strings_DB.add("Player 4/Card played");
                strings_DB.add("Player 1/Card played");
                name_player.setText("Player 3");
                name_opponent_left.setText("Player 2");
                name_ally.setText("Player 4");
                name_opponent_right.setText("Player 1");
                break;
            case 4:
                strings_DB.add("Player 4/Card 1");
                strings_DB.add("Player 4/Card 2");
                strings_DB.add("Player 4/Card 3");
                strings_DB.add("Player 4/Card 4");
                strings_DB.add("Player 4/Card 5");
                strings_DB.add("Player 4/Card 6");
                strings_DB.add("Player 4/Card 7");
                strings_DB.add("Player 4/Card 8");
                strings_DB.add("Player 4/Card played");
                strings_DB.add("Player 1/Card played");
                strings_DB.add("Player 3/Card played");
                strings_DB.add("Player 2/Card played");
                name_player.setText("Player 4");
                name_opponent_left.setText("Player 1");
                name_ally.setText("Player 3");
                name_opponent_right.setText("Player 2");
                break;
        }

        // Callback called when the game button is clicked on : a hand of 8 random cards is dealt
        game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Make the names visible
                name_player.setVisibility(View.VISIBLE);
                name_opponent_left.setVisibility(View.VISIBLE);
                name_opponent_right.setVisibility(View.VISIBLE);
                name_ally.setVisibility(View.VISIBLE);
                // All cards are stored in an array, and each card has an ID number of 3 digits:
                // 1st one for the colour, and the 2 others for the value
                // When a card is distributed, its value in the array is set to 0
                cards = new ArrayList<>();
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

                // Delete the card played in the DB as there is none when the cards are distributed
                deleteDB(strings_DB.get(8));

                // Make the cards clickable again in case the game has been restarted by clicking the game button
                card1.setClickable(true);
                card2.setClickable(true);
                card3.setClickable(true);
                card4.setClickable(true);
                card5.setClickable(true);
                card6.setClickable(true);
                card7.setClickable(true);
                card8.setClickable(true);

                // Cards are randomly distributed
                int[] random_id = {0,0,0,0,0,0,0,0};
                int i = 0;
                while(i < random_id.length) {
                    random_id[i] = (int) (Math.random() * cards.size());
                    if(cards.get(random_id[i]) != 0) { // If the card is already distributed, then another loop turn is done
                        player_cards_id[i] = cards.get(random_id[i]); // Store the card ID before setting to 0 to use it in assignCard function
                        cards.set(random_id[i],0); // Set to 0 in the card array the corresponding card distributed
                        i++;
                    }
                }
                //System.out.println("random_id 0: " + random_id[0]);
                //System.out.println("random_id 1: " + random_id[1]);
                //System.out.println("random_id 2: " + random_id[2]);
                //System.out.println("random_id 3: " + random_id[3]);
                //System.out.println("random_id 4: " + random_id[4]);
                //System.out.println("random_id 5: " + random_id[5]);
                //System.out.println("random_id 6: " + random_id[6]);
                //System.out.println("random_id 7: " + random_id[7]);

                // Make the cards of the principal player appear and send them to the database
                assignCard(player_cards_id[0],card1);
                writeDB(player_cards_id[0],strings_DB.get(0));
                assignCard(player_cards_id[1],card2);
                writeDB(player_cards_id[1],strings_DB.get(1));
                assignCard(player_cards_id[2],card3);
                writeDB(player_cards_id[2],strings_DB.get(2));
                assignCard(player_cards_id[3],card4);
                writeDB(player_cards_id[3],strings_DB.get(3));
                assignCard(player_cards_id[4],card5);
                writeDB(player_cards_id[4],strings_DB.get(4));
                assignCard(player_cards_id[5],card6);
                writeDB(player_cards_id[5],strings_DB.get(5));
                assignCard(player_cards_id[6],card7);
                writeDB(player_cards_id[6],strings_DB.get(6));
                assignCard(player_cards_id[7],card8);
                writeDB(player_cards_id[7],strings_DB.get(7));

                // Make cards of the 3 other players appear
                opponent_left.setImageResource(R.drawable.back_cards);
                opponent_right.setImageResource(R.drawable.back_cards);
                ally.setImageResource(R.drawable.back_cards);

                // Make the played cards and the button disappear
                played_card.setImageResource(0);
                game_button.setVisibility(View.GONE);
            }
        });


        // Callbacks called when a card of the 1st player is clicked on : Deal the card in front of the player and send it to the database
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //assignCard(player_cards_id[0],opponent_left_played_card);
                //assignCard(player_cards_id[0],opponent_right_played_card);
                //assignCard(player_cards_id[0],ally_played_card);
                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                DatabaseReference mDbRef_opp_left = mDatabase.getReference(strings_DB.get(9));
                DatabaseReference mDbRef_ally = mDatabase.getReference(strings_DB.get(10));
                DatabaseReference mDbRef_opp_right = mDatabase.getReference(strings_DB.get(11));
                mDbRef_opp_left.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated
                        int value = dataSnapshot.getValue(int.class);
                        assignCard(value,opponent_left_played_card);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
                mDbRef_ally.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated
                        int value = dataSnapshot.getValue(int.class);
                        assignCard(value,ally_played_card);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
                mDbRef_opp_right.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated
                        int value = dataSnapshot.getValue(int.class);
                        assignCard(value,opponent_right_played_card);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });

                assignCard(player_cards_id[0],played_card);
                writeDB(player_cards_id[0],strings_DB.get(8));
                writeDB(0,strings_DB.get(0));
                card1.setImageResource(0);
                card1.setClickable(false); // Once the card is dealt, it is no more clickable
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignCard(player_cards_id[1],played_card);
                writeDB(player_cards_id[1],strings_DB.get(8));
                writeDB(0,strings_DB.get(1));
                card2.setImageResource(0);
                card2.setClickable(false);
            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignCard(player_cards_id[2],played_card);
                writeDB(player_cards_id[2],strings_DB.get(8));
                writeDB(0,strings_DB.get(2));
                card3.setImageResource(0);
                card3.setClickable(false);
            }
        });
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignCard(player_cards_id[3],played_card);
                writeDB(player_cards_id[3],strings_DB.get(8));
                writeDB(0,strings_DB.get(3));
                card4.setImageResource(0);
                card4.setClickable(false);
            }
        });
        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignCard(player_cards_id[4],played_card);
                writeDB(player_cards_id[4],strings_DB.get(8));
                writeDB(0,strings_DB.get(4));
                card5.setImageResource(0);
                card5.setClickable(false);
            }
        });
        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignCard(player_cards_id[5],played_card);
                writeDB(player_cards_id[5],strings_DB.get(8));
                writeDB(0,strings_DB.get(5));
                card6.setImageResource(0);
                card6.setClickable(false);
            }
        });
        card7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignCard(player_cards_id[6],played_card);
                writeDB(player_cards_id[6],strings_DB.get(8));
                writeDB(0,strings_DB.get(6));
                card7.setImageResource(0);
                card7.setClickable(false);
            }
        });
        card8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignCard(player_cards_id[7],played_card);
                writeDB(player_cards_id[7],strings_DB.get(8));
                writeDB(0,strings_DB.get(7));
                card8.setImageResource(0);
                card8.setClickable(false);
            }
        });
    }

    // Read from the database
    public void readDB(String location) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDbRef = mDatabase.getReference(location);
        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated
                int value = dataSnapshot.getValue(int.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    // Write to the database
    public void writeDB(int id, String location) {
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

    // Delete from the database
    public void deleteDB(String location) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDbRef = mDatabase.getReference(location);
        mDbRef.removeValue();
    }

    // Huge switch case to make the card whose ID is given in parameter appear on the ImageView
    public void assignCard(int card_id,ImageView card) {
        switch(card_id){
            case 107:
                card.setImageResource(R.drawable.clubs_7);
                break;
            case 108:
                card.setImageResource(R.drawable.clubs_8);
                break;
            case 109:
                card.setImageResource(R.drawable.clubs_9);
                break;
            case 110:
                card.setImageResource(R.drawable.clubs_10);
                break;
            case 111:
                card.setImageResource(R.drawable.clubs_j);
                break;
            case 112:
                card.setImageResource(R.drawable.clubs_q);
                break;
            case 113:
                card.setImageResource(R.drawable.clubs_k);
                break;
            case 114:
                card.setImageResource(R.drawable.clubs_a);
                break;
            case 207:
                card.setImageResource(R.drawable.spades_7);
                break;
            case 208:
                card.setImageResource(R.drawable.spades_8);
                break;
            case 209:
                card.setImageResource(R.drawable.spades_9);
                break;
            case 210:
                card.setImageResource(R.drawable.spades_10);
                break;
            case 211:
                card.setImageResource(R.drawable.spades_j);
                break;
            case 212:
                card.setImageResource(R.drawable.spades_q);
                break;
            case 213:
                card.setImageResource(R.drawable.spades_k);
                break;
            case 214:
                card.setImageResource(R.drawable.spades_a);
                break;
            case 307:
                card.setImageResource(R.drawable.diamonds_7);
                break;
            case 308:
                card.setImageResource(R.drawable.diamonds_8);
                break;
            case 309:
                card.setImageResource(R.drawable.diamonds_9);
                break;
            case 310:
                card.setImageResource(R.drawable.diamonds_10);
                break;
            case 311:
                card.setImageResource(R.drawable.diamonds_j);
                break;
            case 312:
                card.setImageResource(R.drawable.diamonds_q);
                break;
            case 313:
                card.setImageResource(R.drawable.diamonds_k);
                break;
            case 314:
                card.setImageResource(R.drawable.diamonds_a);
                break;
            case 407:
                card.setImageResource(R.drawable.hearts_7);
                break;
            case 408:
                card.setImageResource(R.drawable.hearts_8);
                break;
            case 409:
                card.setImageResource(R.drawable.hearts_9);
                break;
            case 410:
                card.setImageResource(R.drawable.hearts_10);
                break;
            case 411:
                card.setImageResource(R.drawable.hearts_j);
                break;
            case 412:
                card.setImageResource(R.drawable.hearts_q);
                break;
            case 413:
                card.setImageResource(R.drawable.hearts_k);
                break;
            case 414:
                card.setImageResource(R.drawable.hearts_a);
                break;
        }
    }
}

