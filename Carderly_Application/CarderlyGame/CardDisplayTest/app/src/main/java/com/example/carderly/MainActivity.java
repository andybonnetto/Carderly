package com.example.carderly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ImageView card1, card2, card3, card4, card5, card6, card7, card8, opponent_left, opponent_right, ally, played_card, opponent_left_played_card, opponent_right_played_card, ally_played_card, trump_view;
    TextView name_player, name_opponent_left, name_opponent_right, name_ally;
    Button game_button;
    ArrayList<Integer> cards;
    ArrayList<String> strings_DB;
    int player_id = 1;
    int end_turn = 0; // Incremented each time a card is played
    int game_end = 0;
    int trump = 0;
    int suit = 0;
    int current_to_play = 0;
    int card_out = 1;
    int[] first_digit = {0,0,0,0};
    int[] last_two_digits = {0,0,0,0};
    int[] player_cards_id = {0,0,0,0,0,0,0,0}; // ID of the cards in hand of the player 1
    private final String TAG = this.getClass().getName();
    String room_name;
    ArrayList<String> players_name;

    // Variables related to the popup window for the trump selection
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    ImageButton clubs, spades, diamonds, hearts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get info from the intent of the waiting room
        player_id = (int) getIntent().getSerializableExtra("playerID");
        players_name = (ArrayList<String>) getIntent().getSerializableExtra("listofPlayers");
        room_name = (String) getIntent().getSerializableExtra("roomName");

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
        trump_view = (ImageView) findViewById(R.id.trump_view);
        name_player = (TextView) findViewById(R.id.name_player);
        name_opponent_left = (TextView) findViewById(R.id.name_opponent_left);
        name_opponent_right = (TextView) findViewById(R.id.name_opponent_right);
        name_ally = (TextView) findViewById(R.id.name_ally);
        Random rand = new Random();
        //int first_player = rand.nextInt(4) + 1;
        //writeIntDB(first_player,"First turn");
        // Initialize all fields of the database
        writeIntDB(game_end,"rooms/" + room_name + "/GameEnd");
        writeIntDB(1,"rooms/" + room_name + "/First turn");
        writeIntDB(1,"rooms/" + room_name + "/Current to play");
        current_to_play = 1;
        writeIntDB(0,"rooms/" + room_name + "/Turn end");
        writeIntDB(0,"rooms/" + room_name + "/Trump");
        writeIntDB(1,"rooms/" + room_name + "/OldPersonTrump");
        writeIntDB(1,"rooms/" + room_name + "/CardOut");
        // Played cards are set to 0 at the start
        writeIntDB(0,"rooms/" + room_name + "/PlayedCard");
        writeIntDB(0,"rooms/" + room_name + "/Player 1/Card played");
        writeIntDB(0,"rooms/" + room_name + "/Player 2/Card played");
        writeIntDB(0,"rooms/" + room_name + "/Player 3/Card played");
        writeIntDB(0,"rooms/" + room_name + "/Player 4/Card played");

        // Get the all the cards in the array "cards" from the DB
        cards = new ArrayList<Integer>();
        getCardListDB();

        strings_DB = new ArrayList<>();
        switch(player_id) { // Locations where the values will be stored in the database
            case 1:
                strings_DB.add("rooms/" + room_name + "/Player 1/Card 1");
                strings_DB.add("rooms/" + room_name + "/Player 1/Card 2");
                strings_DB.add("rooms/" + room_name + "/Player 1/Card 3");
                strings_DB.add("rooms/" + room_name + "/Player 1/Card 4");
                strings_DB.add("rooms/" + room_name + "/Player 1/Card 5");
                strings_DB.add("rooms/" + room_name + "/Player 1/Card 6");
                strings_DB.add("rooms/" + room_name + "/Player 1/Card 7");
                strings_DB.add("rooms/" + room_name + "/Player 1/Card 8");
                strings_DB.add("rooms/" + room_name + "/Player 1/Card played");
                strings_DB.add("rooms/" + room_name + "/Player 3/Card played");
                strings_DB.add("rooms/" + room_name + "/Player 2/Card played");
                strings_DB.add("rooms/" + room_name + "/Player 4/Card played");
                name_player.setText(players_name.get(0));
                name_opponent_left.setText(players_name.get(2));
                name_ally.setText(players_name.get(1));
                name_opponent_right.setText(players_name.get(3));
                break;
            case 2:
                strings_DB.add("rooms/" + room_name + "/Player 2/Card 1");
                strings_DB.add("rooms/" + room_name + "/Player 2/Card 2");
                strings_DB.add("rooms/" + room_name + "/Player 2/Card 3");
                strings_DB.add("rooms/" + room_name + "/Player 2/Card 4");
                strings_DB.add("rooms/" + room_name + "/Player 2/Card 5");
                strings_DB.add("rooms/" + room_name + "/Player 2/Card 6");
                strings_DB.add("rooms/" + room_name + "/Player 2/Card 7");
                strings_DB.add("rooms/" + room_name + "/Player 2/Card 8");
                strings_DB.add("rooms/" + room_name + "/Player 2/Card played");
                strings_DB.add("rooms/" + room_name + "/Player 4/Card played");
                strings_DB.add("rooms/" + room_name + "/Player 1/Card played");
                strings_DB.add("rooms/" + room_name + "/Player 3/Card played");
                name_player.setText(players_name.get(1));
                name_opponent_left.setText(players_name.get(3));
                name_ally.setText(players_name.get(0));
                name_opponent_right.setText(players_name.get(2));
                break;
            case 3:
                strings_DB.add("rooms/" + room_name + "/Player 3/Card 1");
                strings_DB.add("rooms/" + room_name + "/Player 3/Card 2");
                strings_DB.add("rooms/" + room_name + "/Player 3/Card 3");
                strings_DB.add("rooms/" + room_name + "/Player 3/Card 4");
                strings_DB.add("rooms/" + room_name + "/Player 3/Card 5");
                strings_DB.add("rooms/" + room_name + "/Player 3/Card 6");
                strings_DB.add("rooms/" + room_name + "/Player 3/Card 7");
                strings_DB.add("rooms/" + room_name + "/Player 3/Card 8");
                strings_DB.add("rooms/" + room_name + "/Player 3/Card played");
                strings_DB.add("rooms/" + room_name + "/Player 2/Card played");
                strings_DB.add("rooms/" + room_name + "/Player 4/Card played");
                strings_DB.add("rooms/" + room_name + "/Player 1/Card played");
                name_player.setText(players_name.get(2));
                name_opponent_left.setText(players_name.get(1));
                name_ally.setText(players_name.get(3));
                name_opponent_right.setText(players_name.get(0));
                break;
            case 4:
                strings_DB.add("rooms/" + room_name + "/Player 4/Card 1");
                strings_DB.add("rooms/" + room_name + "/Player 4/Card 2");
                strings_DB.add("rooms/" + room_name + "/Player 4/Card 3");
                strings_DB.add("rooms/" + room_name + "/Player 4/Card 4");
                strings_DB.add("rooms/" + room_name + "/Player 4/Card 5");
                strings_DB.add("rooms/" + room_name + "/Player 4/Card 6");
                strings_DB.add("rooms/" + room_name + "/Player 4/Card 7");
                strings_DB.add("rooms/" + room_name + "/Player 4/Card 8");
                strings_DB.add("rooms/" + room_name + "/Player 4/Card played");
                strings_DB.add("rooms/" + room_name + "/Player 1/Card played");
                strings_DB.add("rooms/" + room_name + "/Player 3/Card played");
                strings_DB.add("rooms/" + room_name + "/Player 2/Card played");
                name_player.setText(players_name.get(3));
                name_opponent_left.setText(players_name.get(0));
                name_ally.setText(players_name.get(2));
                name_opponent_right.setText(players_name.get(1));
                break;
        }

        // Callback called when the game button is clicked on : a hand of 8 random cards is dealt
        game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Popup window to select the trump
                getCardValueDB(new CardValueCallback() {
                    @Override
                    public void onCallback(int first_turn) {
                        boolean game_start = true;
                        for (int i = 0; i < player_cards_id.length; i++) {
                            if(player_cards_id[i] == 0){ // If any card has been played then the game has already started => No need to choose the trump again
                                game_start = false;
                                break;
                            }
                        }
                        if((player_id == first_turn) && (game_start == true)) { // Only select trump for the first player and at the start of the game
                            if (player_id == 1)
                                writeIntDB(1, "rooms/" + room_name + "/OldPersonTrump");
                            trumpSelectionDialog();
                        }
                    }
                },"rooms/" + room_name + "/First turn");
                // Listener for the trump (for players who do not select it)
                getCardValueDB(new CardValueCallback() {
                    @Override
                    public void onCallback(int trumpDB) {
                        trump = trumpDB;
                        if(trump == 1){
                            trump_view.setImageResource(R.drawable.clubs);
                            trump_view.setBackgroundResource(R.drawable.trump_button_border);
                        } else if(trump == 2) {
                            trump_view.setImageResource(R.drawable.spades);
                            trump_view.setBackgroundResource(R.drawable.trump_button_border);
                        } else if(trump == 3) {
                            trump_view.setImageResource(R.drawable.diamonds);
                            trump_view.setBackgroundResource(R.drawable.trump_button_border);
                        } else if(trump == 4) {
                            trump_view.setImageResource(R.drawable.hearts);
                            trump_view.setBackgroundResource(R.drawable.trump_button_border);
                        } else {
                            trump_view.setImageResource(0);
                            trump_view.setBackgroundResource(0);
                        }
                    }
                },"rooms/" + room_name + "/Trump");
                // Make the names visible
                name_player.setVisibility(View.VISIBLE);
                name_opponent_left.setVisibility(View.VISIBLE);
                name_opponent_right.setVisibility(View.VISIBLE);
                name_ally.setVisibility(View.VISIBLE);

                // Make the cards clickable for the first player for the very first turn of the game
                if(player_id == 1) {
                    card1.setClickable(true);
                    card2.setClickable(true);
                    card3.setClickable(true);
                    card4.setClickable(true);
                    card5.setClickable(true);
                    card6.setClickable(true);
                    card7.setClickable(true);
                    card8.setClickable(true);
                } else {
                    card1.setClickable(false);
                    card2.setClickable(false);
                    card3.setClickable(false);
                    card4.setClickable(false);
                    card5.setClickable(false);
                    card6.setClickable(false);
                    card7.setClickable(false);
                    card8.setClickable(false);
                }


                // Card distribution : player 1 gets the first 8 cards, player 2 the next 8, etc.
                if(player_id == 1) {
                    for(int i = 0; i < 8; i++) {
                        player_cards_id[i] = cards.get(i);
                    }
                } else if(player_id == 2) {
                    for(int i = 0; i < 8; i++) {
                        player_cards_id[i] = cards.get(i+8);
                    }
                } else if(player_id == 3) {
                    for(int i = 0; i < 8; i++) {
                        player_cards_id[i] = cards.get(i+16);
                    }
                } else if(player_id == 4) {
                    for(int i = 0; i < 8; i++) {
                        player_cards_id[i] = cards.get(i+24);
                    }
                }

                // Make the cards of the principal player appear and send them to the database
                assignCard(player_cards_id[0],card1);
                writeIntDB(player_cards_id[0],strings_DB.get(0));
                assignCard(player_cards_id[1],card2);
                writeIntDB(player_cards_id[1],strings_DB.get(1));
                assignCard(player_cards_id[2],card3);
                writeIntDB(player_cards_id[2],strings_DB.get(2));
                assignCard(player_cards_id[3],card4);
                writeIntDB(player_cards_id[3],strings_DB.get(3));
                assignCard(player_cards_id[4],card5);
                writeIntDB(player_cards_id[4],strings_DB.get(4));
                assignCard(player_cards_id[5],card6);
                writeIntDB(player_cards_id[5],strings_DB.get(5));
                assignCard(player_cards_id[6],card7);
                writeIntDB(player_cards_id[6],strings_DB.get(6));
                assignCard(player_cards_id[7],card8);
                writeIntDB(player_cards_id[7],strings_DB.get(7));

                // Make cards of the 3 other players appear
                opponent_left.setImageResource(R.drawable.back_cards);
                opponent_right.setImageResource(R.drawable.back_cards);
                ally.setImageResource(R.drawable.back_cards);

                // Make the played cards and the button disappear
                //played_card.setImageResource(0);
                game_button.setVisibility(View.GONE);

                getCardValueDB(new CardValueCallback() {
                    @Override
                    public void onCallback(int card_out_DB) {
                        card_out = card_out_DB;
                        System.out.println("Card Out: " + card_out);
                    }
                },"rooms/" + room_name + "/CardOut");

                // Display cards played when they are updated in the database
                getCardValueDB(new CardValueCallback() {
                    @Override
                    public void onCallback(int card_value) {
                        assignCard(card_value,played_card);

                        if(card_value != 0) { // card_value = 0 means the played card has been taken off the board, so no particular action to do in that case
                            if (player_id == 1) {
                                getDigits(card_value, 1);
                                card1.setClickable(false);
                                card2.setClickable(false);
                                card3.setClickable(false);
                                card4.setClickable(false);
                                card5.setClickable(false);
                                card6.setClickable(false);
                                card7.setClickable(false);
                                card8.setClickable(false);
                                name_player.setTextColor(Color.WHITE);
                                name_opponent_left.setTextColor(Color.RED);
                                writeIntDB(3, "rooms/" + room_name + "/Current to play");
                                current_to_play = 3;
                            }
                            if (player_id == 2) {
                                getDigits(card_value, 2);
                                card1.setClickable(false);
                                card2.setClickable(false);
                                card3.setClickable(false);
                                card4.setClickable(false);
                                card5.setClickable(false);
                                card6.setClickable(false);
                                card7.setClickable(false);
                                card8.setClickable(false);
                                name_player.setTextColor(Color.WHITE);
                                name_opponent_left.setTextColor(Color.RED);
                                writeIntDB(4,"rooms/" + room_name + "/Current to play");
                                current_to_play = 4;
                            }
                            if (player_id == 3) {
                                getDigits(card_value,3);
                                card1.setClickable(false);
                                card2.setClickable(false);
                                card3.setClickable(false);
                                card4.setClickable(false);
                                card5.setClickable(false);
                                card6.setClickable(false);
                                card7.setClickable(false);
                                card8.setClickable(false);
                                name_player.setTextColor(Color.WHITE);
                                name_opponent_left.setTextColor(Color.RED);
                                writeIntDB(2,"rooms/" + room_name + "/Current to play");
                                current_to_play = 2;
                            }
                            if (player_id == 4) {
                                getDigits(card_value, 4);
                                card1.setClickable(false);
                                card2.setClickable(false);
                                card3.setClickable(false);
                                card4.setClickable(false);
                                card5.setClickable(false);
                                card6.setClickable(false);
                                card7.setClickable(false);
                                card8.setClickable(false);
                                name_player.setTextColor(Color.WHITE);
                                name_opponent_left.setTextColor(Color.RED);
                                writeIntDB(1,"rooms/" + room_name + "/Current to play");
                                current_to_play = 1;
                            }
                            end_turn++;
                            System.out.println(end_turn);
                            if (end_turn == 1) { // 1st player of the turn defines the suit
                                String number = String.valueOf(card_value);
                                suit = Character.digit(number.charAt(0), 10); // 1st digit of card value
                            }
                            if (end_turn == 4) {
                                endTurn();
                            }
                        }
                    }
                },strings_DB.get(8));
                getCardValueDB(new CardValueCallback() {
                    @Override
                    public void onCallback(int card_value) {
                        assignCard(card_value,opponent_left_played_card);
                        if(card_value != 0) { // card_value = 0 means the played card has been taken off the board, so no particular action to do in that case
                            if (player_id == 1) {
                                getDigits(card_value, 3);
                                name_opponent_left.setTextColor(Color.WHITE);
                                name_ally.setTextColor(Color.RED);
                                writeIntDB(2, "rooms/" + room_name + "/Current to play");
                                current_to_play = 2;
                            }
                            if (player_id == 2) {
                                getDigits(card_value, 4);
                                name_opponent_left.setTextColor(Color.WHITE);
                                name_ally.setTextColor(Color.RED);
                                writeIntDB(1,"rooms/" + room_name + "/Current to play");
                                current_to_play = 1;
                            }
                            if (player_id == 3) {
                                getDigits(card_value,2);
                                name_opponent_left.setTextColor(Color.WHITE);
                                name_ally.setTextColor(Color.RED);
                                writeIntDB(4,"rooms/" + room_name + "/Current to play");
                                current_to_play = 4;
                            }
                            if (player_id == 4) {
                                getDigits(card_value, 1);
                                name_opponent_left.setTextColor(Color.WHITE);
                                name_ally.setTextColor(Color.RED);
                                writeIntDB(3,"rooms/" + room_name + "/Current to play");
                                current_to_play = 3;
                            }
                            end_turn++;
                            System.out.println(end_turn);
                            if (end_turn == 1) { // 1st player of the turn defines the suit
                                String number = String.valueOf(card_value);
                                suit = Character.digit(number.charAt(0), 10); // 1st digit of card value
                            }
                            if (end_turn == 4) {
                                endTurn();
                            }
                        }
                    }
                },strings_DB.get(9));
                getCardValueDB(new CardValueCallback() {
                    @Override
                    public void onCallback(int card_value) {
                        assignCard(card_value,ally_played_card);
                        if(card_value != 0) { // card_value = 0 means the played card has been taken off the board, so no particular action to do in that case
                            if (player_id == 1) {
                                getDigits(card_value, 2);
                                name_ally.setTextColor(Color.WHITE);
                                name_opponent_right.setTextColor(Color.RED);
                                writeIntDB(4, "rooms/" + room_name + "/Current to play");
                                current_to_play = 4;
                            }
                            if (player_id == 2) {
                                getDigits(card_value, 1);
                                name_ally.setTextColor(Color.WHITE);
                                name_opponent_right.setTextColor(Color.RED);
                                writeIntDB(3,"rooms/" + room_name + "/Current to play");
                                current_to_play = 3;
                            }
                            if (player_id == 3) {
                                getDigits(card_value,4);
                                name_ally.setTextColor(Color.WHITE);
                                name_opponent_right.setTextColor(Color.RED);
                                writeIntDB(1,"rooms/" + room_name + "/Current to play");
                                current_to_play = 1;
                            }
                            if (player_id == 4) {
                                getDigits(card_value, 3);
                                name_ally.setTextColor(Color.WHITE);
                                name_opponent_right.setTextColor(Color.RED);
                                writeIntDB(2,"rooms/" + room_name + "/Current to play");
                                current_to_play = 2;
                            }
                            end_turn++;
                            System.out.println(end_turn);
                            if (end_turn == 1) { // 1st player of the turn defines the suit
                                String number = String.valueOf(card_value);
                                suit = Character.digit(number.charAt(0), 10); // 1st digit of card value
                            }
                            if (end_turn == 4) {
                                endTurn();
                            }
                        }
                    }
                },strings_DB.get(10));
                getCardValueDB(new CardValueCallback() {
                    @Override
                    public void onCallback(int card_value) {
                        assignCard(card_value,opponent_right_played_card);
                        if(card_value != 0) { // card_value = 0 means the played card has been taken off the board, so no particular action to do in that case
                            if (player_id == 1) {
                                getDigits(card_value, 4);
                                if(player_cards_id[0] != 0)
                                    card1.setClickable(true);
                                if(player_cards_id[1] != 0)
                                    card2.setClickable(true);
                                if(player_cards_id[2] != 0)
                                    card3.setClickable(true);
                                if(player_cards_id[3] != 0)
                                    card4.setClickable(true);
                                if(player_cards_id[4] != 0)
                                    card5.setClickable(true);
                                if(player_cards_id[5] != 0)
                                    card6.setClickable(true);
                                if(player_cards_id[6] != 0)
                                    card7.setClickable(true);
                                if(player_cards_id[7] != 0)
                                    card8.setClickable(true);
                                name_opponent_right.setTextColor(Color.WHITE);
                                name_player.setTextColor(Color.RED);
                                writeIntDB(1, "rooms/" + room_name + "/Current to play");
                                current_to_play = 1;
                            }
                            if (player_id == 2) {
                                getDigits(card_value, 3);
                                if(player_cards_id[0] != 0)
                                    card1.setClickable(true);
                                if(player_cards_id[1] != 0)
                                    card2.setClickable(true);
                                if(player_cards_id[2] != 0)
                                    card3.setClickable(true);
                                if(player_cards_id[3] != 0)
                                    card4.setClickable(true);
                                if(player_cards_id[4] != 0)
                                    card5.setClickable(true);
                                if(player_cards_id[5] != 0)
                                    card6.setClickable(true);
                                if(player_cards_id[6] != 0)
                                    card7.setClickable(true);
                                if(player_cards_id[7] != 0)
                                    card8.setClickable(true);
                                name_opponent_right.setTextColor(Color.WHITE);
                                name_player.setTextColor(Color.RED);
                                writeIntDB(2,"rooms/" + room_name + "/Current to play");
                                current_to_play = 2;
                            }
                            if (player_id == 3) {
                                getDigits(card_value,1);
                                if(player_cards_id[0] != 0)
                                    card1.setClickable(true);
                                if(player_cards_id[1] != 0)
                                    card2.setClickable(true);
                                if(player_cards_id[2] != 0)
                                    card3.setClickable(true);
                                if(player_cards_id[3] != 0)
                                    card4.setClickable(true);
                                if(player_cards_id[4] != 0)
                                    card5.setClickable(true);
                                if(player_cards_id[5] != 0)
                                    card6.setClickable(true);
                                if(player_cards_id[6] != 0)
                                    card7.setClickable(true);
                                if(player_cards_id[7] != 0)
                                    card8.setClickable(true);
                                name_opponent_right.setTextColor(Color.WHITE);
                                name_player.setTextColor(Color.RED);
                                writeIntDB(3,"rooms/" + room_name + "/Current to play");
                                current_to_play = 3;
                            }
                            if (player_id == 4) {
                                getDigits(card_value, 2);
                                if(player_cards_id[0] != 0)
                                    card1.setClickable(true);
                                if(player_cards_id[1] != 0)
                                    card2.setClickable(true);
                                if(player_cards_id[2] != 0)
                                    card3.setClickable(true);
                                if(player_cards_id[3] != 0)
                                    card4.setClickable(true);
                                if(player_cards_id[4] != 0)
                                    card5.setClickable(true);
                                if(player_cards_id[5] != 0)
                                    card6.setClickable(true);
                                if(player_cards_id[6] != 0)
                                    card7.setClickable(true);
                                if(player_cards_id[7] != 0)
                                    card8.setClickable(true);
                                name_opponent_right.setTextColor(Color.WHITE);
                                name_player.setTextColor(Color.RED);
                                writeIntDB(4,"rooms/" + room_name + "/Current to play");
                                current_to_play = 4;
                            }
                            end_turn++;
                            System.out.println(end_turn);
                            if (end_turn == 1) { // 1st player of the turn defines the suit
                                String number = String.valueOf(card_value);
                                suit = Character.digit(number.charAt(0), 10); // 1st digit of card value
                            }
                            if (end_turn == 4) {
                                endTurn();
                            }
                        }
                    }
                },strings_DB.get(11));
            }
        });

        // Callbacks called when a card of the 1st player is clicked on : Deal the card in front of the player and send it to the database
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((current_to_play == player_id) && (card_out == 1)){
                    writeIntDB(player_cards_id[0],strings_DB.get(8));
                    writeIntDB(player_cards_id[0],"rooms/" + room_name + "/PlayedCard");
                    writeIntDB(0,strings_DB.get(0));
                    card1.setImageResource(0);
                    player_cards_id[0] = 0;
                    card1.setClickable(false);
                }
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((current_to_play == player_id) && (card_out == 1)){
                    writeIntDB(player_cards_id[1],strings_DB.get(8));
                    writeIntDB(player_cards_id[1],"rooms/" + room_name + "/PlayedCard");
                    writeIntDB(0,strings_DB.get(1));
                    card2.setImageResource(0);
                    player_cards_id[1] = 0;
                    card2.setClickable(false);
                }
            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((current_to_play == player_id) && (card_out == 1)){
                    writeIntDB(player_cards_id[2],strings_DB.get(8));
                    writeIntDB(player_cards_id[2],"rooms/" + room_name + "/PlayedCard");
                    writeIntDB(0,strings_DB.get(2));
                    card3.setImageResource(0);
                    player_cards_id[2] = 0;
                    card3.setClickable(false);
                }
            }
        });
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((current_to_play == player_id) && (card_out == 1)){
                    writeIntDB(player_cards_id[3],strings_DB.get(8));
                    writeIntDB(player_cards_id[3],"rooms/" + room_name + "/PlayedCard");
                    writeIntDB(0,strings_DB.get(3));
                    card4.setImageResource(0);
                    player_cards_id[3] = 0;
                    card4.setClickable(false);
                }
            }
        });
        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((current_to_play == player_id) && (card_out == 1)){
                    writeIntDB(player_cards_id[4],strings_DB.get(8));
                    writeIntDB(player_cards_id[4],"rooms/" + room_name + "/PlayedCard");
                    writeIntDB(0,strings_DB.get(4));
                    card5.setImageResource(0);
                    player_cards_id[4] = 0;
                    card5.setClickable(false);
                }
            }
        });
        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((current_to_play == player_id) && (card_out == 1)){
                    writeIntDB(player_cards_id[5],strings_DB.get(8));
                    writeIntDB(player_cards_id[5],"rooms/" + room_name + "/PlayedCard");
                    writeIntDB(0,strings_DB.get(5));
                    card6.setImageResource(0);
                    player_cards_id[5] = 0;
                    card6.setClickable(false);
                }
            }
        });
        card7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((current_to_play == player_id) && (card_out == 1)){
                    writeIntDB(player_cards_id[6],strings_DB.get(8));
                    writeIntDB(player_cards_id[6],"rooms/" + room_name + "/PlayedCard");
                    writeIntDB(0,strings_DB.get(6));
                    card7.setImageResource(0);
                    player_cards_id[6] = 0;
                    card7.setClickable(false);
                }
            }
        });
        card8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((current_to_play == player_id) && (card_out == 1)){
                    writeIntDB(player_cards_id[7],strings_DB.get(8));
                    writeIntDB(player_cards_id[7],"rooms/" + room_name + "/PlayedCard");
                    writeIntDB(0,strings_DB.get(7));
                    card8.setImageResource(0);
                    player_cards_id[7] = 0;
                    card8.setClickable(false);
                }
            }
        });
    }

    // Callback needed to retrieve data from the DB
    public interface CardListCallback {
        void onCallback(ArrayList<Integer> value);
    }

    // Callback needed to retrieve data from the DB
    public interface CardValueCallback {
        void onCallback(int value);
    }

    // Get list of cards from the database
    public void getCardListDB() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDbRef = mDatabase.getReference("rooms/" + room_name + "/Cards");
        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childrenSnapshot: dataSnapshot.getChildren()) {
                    cards.add(childrenSnapshot.getValue(int.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    // Get card value from the database
    public void getCardValueDB(final CardValueCallback myCallback, String location) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDbRef = mDatabase.getReference(location);
        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int card_value = dataSnapshot.getValue(int.class);
                myCallback.onCallback(card_value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
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

    // Delete from the database
    public void deleteDB(String location) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDbRef = mDatabase.getReference(location);
        mDbRef.removeValue();
    }

    // Create the popup to choose the trump at the beginning of the game
    public void trumpSelectionDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View trumpPopupView = getLayoutInflater().inflate(R.layout.trump_selection,null);

        clubs = (ImageButton) trumpPopupView.findViewById(R.id.clubs);
        spades = (ImageButton) trumpPopupView.findViewById(R.id.spades);
        diamonds = (ImageButton) trumpPopupView.findViewById(R.id.diamonds);
        hearts = (ImageButton) trumpPopupView.findViewById(R.id.hearts);

        dialogBuilder.setView(trumpPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        clubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trump = 1;
                writeIntDB(trump,"rooms/" + room_name + "/Trump");
                dialog.dismiss();
            }
        });
        spades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trump = 2;
                writeIntDB(trump,"rooms/" + room_name + "/Trump");
                dialog.dismiss();
            }
        });
        diamonds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trump = 3;
                writeIntDB(trump,"rooms/" + room_name + "/Trump");
                dialog.dismiss();
            }
        });
        hearts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trump = 4;
                writeIntDB(trump,"rooms/" + room_name + "/Trump");
                dialog.dismiss();
            }
        });
    }

    // Get in arrays the digits of the card played to analyse them in endTurn (only the player 1 calls this method)
    public void getDigits(int num,int player_id) {
        String number = String.valueOf(num);
        first_digit[player_id-1] = Character.digit(number.charAt(0), 10);
        last_two_digits[player_id-1] = num % 100;
    }

    // The winning card of the turn is highlighted in green or back to its normal color
    public void setColorWinningCard(int round_winner, int color_intensity) {
        switch(player_id) {
            case 1:
                if(round_winner == 1) {
                    played_card.setColorFilter(color_intensity);
                    name_player.setTextColor(Color.RED);
                }
                else if(round_winner == 2) {
                    ally_played_card.setColorFilter(color_intensity);
                    name_ally.setTextColor(Color.RED);
                }
                else if(round_winner == 3) {
                    opponent_left_played_card.setColorFilter(color_intensity);
                    name_opponent_left.setTextColor(Color.RED);
                }
                else if(round_winner == 4) {
                    opponent_right_played_card.setColorFilter(color_intensity);
                    name_opponent_right.setTextColor(Color.RED);
                }
                break;
            case 2:
                if(round_winner == 1) {
                    ally_played_card.setColorFilter(color_intensity);
                    name_ally.setTextColor(Color.RED);
                }
                else if(round_winner == 2) {
                    played_card.setColorFilter(color_intensity);
                    name_player.setTextColor(Color.RED);
                }
                else if(round_winner == 3) {
                    opponent_right_played_card.setColorFilter(color_intensity);
                    name_opponent_right.setTextColor(Color.RED);
                }
                else if(round_winner == 4) {
                    opponent_left_played_card.setColorFilter(color_intensity);
                    name_opponent_left.setTextColor(Color.RED);
                }
                break;
            case 3:
                if(round_winner == 1) {
                    opponent_right_played_card.setColorFilter(color_intensity);
                    name_opponent_right.setTextColor(Color.RED);
                }
                else if(round_winner == 2) {
                    opponent_left_played_card.setColorFilter(color_intensity);
                    name_opponent_left.setTextColor(Color.RED);
                }
                else if(round_winner == 3) {
                    played_card.setColorFilter(color_intensity);
                    name_player.setTextColor(Color.RED);
                }
                else if(round_winner == 4) {
                    ally_played_card.setColorFilter(color_intensity);
                    name_ally.setTextColor(Color.RED);
                }
                break;
            case 4:
                if(round_winner == 1) {
                    opponent_left_played_card.setColorFilter(color_intensity);
                    name_opponent_left.setTextColor(Color.RED);
                    System.out.println("OPP LEFT COLOR");
                }
                else if(round_winner == 2) {
                    opponent_right_played_card.setColorFilter(color_intensity);
                    name_opponent_right.setTextColor(Color.RED);
                    System.out.println("OPP RIGHT COLOR");
                }
                else if(round_winner == 3) {
                    ally_played_card.setColorFilter(color_intensity);
                    name_ally.setTextColor(Color.RED);
                    System.out.println("ALLY COLOR");
                }
                else if(round_winner == 4) {
                    played_card.setColorFilter(color_intensity);
                    name_player.setTextColor(Color.RED);
                    System.out.println("PLAYER COLOR");
                }
                break;
        }
    }

    // Turn finished -> Find who has won the turn and give him the lead for the next
    public void endTurn() {
        card1.setClickable(false);
        card2.setClickable(false);
        card3.setClickable(false);
        card4.setClickable(false);
        card5.setClickable(false);
        card6.setClickable(false);
        card7.setClickable(false);
        card8.setClickable(false);
        name_player.setTextColor(Color.WHITE);
        name_ally.setTextColor(Color.WHITE);
        name_opponent_left.setTextColor(Color.WHITE);
        name_opponent_right.setTextColor(Color.WHITE);
        int round_winner = 0;
        int best_card = 0;
        boolean best_card_is_trump = false;
        for(int i = 0; i < first_digit.length; i++){
            if(first_digit[i] == trump){
                if(last_two_digits[i] == 11) { // Strongest card of the game => Automatic win of the turn
                    round_winner = i+1;
                    break;
                }
                else if(last_two_digits[i] == 9) {
                    best_card = last_two_digits[i];
                    best_card_is_trump = true;
                    round_winner = i+1;
                }
                // 2 cases : If the actual best card isn't trump, then this card is automatically superior.
                // If it is trump, then we need to check that it isn't a 9 as well (particular case)
                else if(((last_two_digits[i] > best_card) && (best_card != 9)) || (best_card_is_trump == false)) {
                    best_card = last_two_digits[i];
                    best_card_is_trump = true;
                    round_winner = i+1;
                }
            }
            else if((best_card_is_trump == false) && (first_digit[i] == suit)) { // Useless to go in that condition if the best card is trump already
                if(last_two_digits[i] > best_card) {
                    best_card = last_two_digits[i];
                    round_winner = i+1;
                }
            }
        }
        /*System.out.println("Trump: " + trump);
        System.out.println("Suit: " + suit);
        System.out.println("Player cards ID: " + Arrays.toString(player_cards_id));
        System.out.println("Round winner: " + round_winner);
        System.out.println("First digit: " + Arrays.toString(first_digit));
        System.out.println("Last two digits: " + Arrays.toString(last_two_digits));*/
        setColorWinningCard(round_winner,Color.argb(100, 0, 200, 0));

        // Click anywhere on the screen to finish the turn and begin the next one
        ConstraintLayout clayout = (ConstraintLayout) findViewById(R.id.constraintlayout);
        clayout.setClickable(true);
        int finalRound_winner = round_winner;
        clayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeIntDB(1,"rooms/" + room_name + "/Turn end");
                clayout.setClickable(false);
            }
        });
        getCardValueDB(new CardValueCallback() {
            @Override
            public void onCallback(int turn_end) {
                if(turn_end == 1) {
                    card1.setClickable(true);
                    card2.setClickable(true);
                    card3.setClickable(true);
                    card4.setClickable(true);
                    card5.setClickable(true);
                    card6.setClickable(true);
                    card7.setClickable(true);
                    card8.setClickable(true);
                    writeIntDB(0, strings_DB.get(8));
                    writeIntDB(0, strings_DB.get(9));
                    writeIntDB(0, strings_DB.get(10));
                    writeIntDB(0, strings_DB.get(11));
                    System.out.println("Final round winner: " + finalRound_winner);
                    setColorWinningCard(finalRound_winner, 0);
                    writeIntDB(finalRound_winner, "rooms/" + room_name + "/First turn");
                    writeIntDB(finalRound_winner, "rooms/" + room_name + "/Current to play");
                    current_to_play = finalRound_winner;
                    end_turn = 0;
                    suit = 0;
                    for(int i = 0; i < 3; i++){
                        first_digit[i] = 0;
                        last_two_digits[i] = 0;
                    }

                    // Check if the game is finished
                    game_end = 1;
                    for (int i = 0; i < player_cards_id.length; i++) {
                        if (player_cards_id[i] != 0) { // If all cards have been played => End of the game
                            game_end = 0;
                            break;
                        }
                    }
                    if (game_end == 1) {
                        game_button.performClick();
                        writeIntDB(game_end, "rooms/" + room_name + "/GameEnd");
                    }
                    writeIntDB(0,"rooms/" + room_name + "/Turn end");
                }
            }
        },"rooms/" + room_name + "/Turn end");
    }

    // Huge switch case to make the card whose ID is given in parameter appear on the ImageView
    public void assignCard(int card_id,ImageView card) {
        switch(card_id){
            case 0: // Hide the card
                card.setImageResource(0);
                break;
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
            default:
        }
    }
}

