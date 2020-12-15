package com.example.carderly;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    //DATA INITIALIZATION
    private final String TAG = this.getClass().getName();
    private Profile userProfile = null;
    private static final int REGISTER_PROFILE = 1;
    private String userID;
    EditText editText;
    Button logButton;

    //DATABASE INITIALIZATION


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Prepares the room "Andy"
        databaseSetup("/Andy");

        //REGISTER CALLBACK (to editProfileActivity)
        Button rButton = findViewById(R.id.RegisterButton);
        rButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEditProfile = new Intent(Login.this, EditProfileActivity.class);
                startActivityForResult(intentEditProfile, REGISTER_PROFILE);
            }
        });



        //LOGIN CALLBACK (To RoomActivity)
        editText = findViewById(R.id.username);

        logButton = findViewById(R.id.LoginButton);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference profileRef = database.getReference("profiles");

                final TextView mTextView = findViewById(R.id.LoginMessage);
                final String usernameInput = ((EditText) findViewById(R.id.username)).getText().toString();
                final String passwordInput = ((EditText) findViewById(R.id.password)).getText().toString();

                profileRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean notMember = true;
                        for (final DataSnapshot user : dataSnapshot.getChildren()) {
                            String usernameDatabase = user.child("username")
                                    .getValue(String.class);
                            String passwordDatabase = user.child("password")
                                    .getValue(String.class);
                            if (usernameInput.equals(usernameDatabase)
                                    && passwordInput.equals(passwordDatabase)) {
                                notMember = false;
                                break;
                            }
                        }
                        if (notMember) {
                            mTextView.setText("You are not registered yet!");
                        } else {
                            Intent intent = new Intent(Login.this, RoomChoice.class);
                            intent.putExtra("username", usernameInput);
                            startActivity(intent);


                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
    }

    //Result of the EditProfileActivity : save userProfile in the database
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTER_PROFILE && resultCode == RESULT_OK) {
            userProfile = (Profile) data.getSerializableExtra("userProfile");
            if (userProfile != null) {
                TextView username = findViewById(R.id.username);
                username.setText(userProfile.username);
                TextView password = findViewById(R.id.password);
                password.setText(userProfile.password);
            }
        }
    }

    public void databaseSetup(String room_name) {
        // Delete from the database
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference count_players = mDatabase.getReference("rooms/" + room_name + "/CountPlayer");
        count_players.setValue(1);
        DatabaseReference childRef1 = mDatabase.getReference("rooms/" + room_name + "/Cards");
        DatabaseReference childRef2 = mDatabase.getReference("rooms/" + room_name + "/Current to play");
        DatabaseReference childRef3 = mDatabase.getReference("rooms/" + room_name + "/First turn");
        DatabaseReference childRef4 = mDatabase.getReference("rooms/" + room_name + "/GameEnd");
        DatabaseReference childRef5 = mDatabase.getReference("rooms/" + room_name + "/OldPersonTrump");
        DatabaseReference childRef6 = mDatabase.getReference("rooms/" + room_name + "/PlayedCard");
        DatabaseReference childRef7 = mDatabase.getReference("rooms/" + room_name + "/Player 2");
        DatabaseReference childRef8 = mDatabase.getReference("rooms/" + room_name + "/Player 3");
        DatabaseReference childRef9 = mDatabase.getReference("rooms/" + room_name + "/Player 4");
        DatabaseReference childRef10 = mDatabase.getReference("rooms/" + room_name + "/Trump");
        DatabaseReference childRef11 = mDatabase.getReference("rooms/" + room_name + "/Turn end");
        DatabaseReference childRef12 = mDatabase.getReference("rooms/" + room_name + "/Player 1" + "/Card 1");
        childRef12.setValue(0);
        DatabaseReference childRef13 = mDatabase.getReference("rooms/" + room_name + "/Player 1" + "/Card 2");
        childRef13.setValue(0);
        DatabaseReference childRef14 = mDatabase.getReference("rooms/" + room_name + "/Player 1" + "/Card 3");
        childRef14.setValue(0);
        DatabaseReference childRef15 = mDatabase.getReference("rooms/" + room_name + "/Player 1" + "/Card 4");
        childRef15.setValue(0);
        DatabaseReference childRef16 = mDatabase.getReference("rooms/" + room_name + "/Player 1" + "/Card 5");
        childRef16.setValue(0);
        DatabaseReference childRef17 = mDatabase.getReference("rooms/" + room_name + "/Player 1" + "/Card 6");
        childRef17.setValue(0);
        DatabaseReference childRef18 = mDatabase.getReference("rooms/" + room_name + "/Player 1" + "/Card 7");
        childRef18.setValue(0);
        DatabaseReference childRef19 = mDatabase.getReference("rooms/" + room_name + "/Player 1" + "/Card 8");
        childRef19.setValue(0);
        DatabaseReference childRef20 = mDatabase.getReference("rooms/" + room_name + "/Player 1" + "/Card played");
        childRef20.setValue(0);

        childRef1.removeValue();
        childRef2.removeValue();
        childRef3.removeValue();
        childRef4.removeValue();
        childRef5.removeValue();
        childRef6.removeValue();
        childRef7.removeValue();
        childRef8.removeValue();
        childRef9.removeValue();
        childRef10.removeValue();
        childRef11.removeValue();
        childRef12.removeValue();
        childRef13.removeValue();
        childRef14.removeValue();
        childRef15.removeValue();
        childRef16.removeValue();
        childRef17.removeValue();
        childRef18.removeValue();
        childRef19.removeValue();
        childRef20.removeValue();
    }

}