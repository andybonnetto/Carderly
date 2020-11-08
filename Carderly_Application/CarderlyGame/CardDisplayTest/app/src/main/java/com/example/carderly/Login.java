package com.example.carderly;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    //DATA INITIALIZATION
    private final String TAG = this.getClass().getName();
    private Profile userProfile = null;
    private static final int REGISTER_PROFILE = 1;
    EditText editText;
    Button logButton;

    //DATABASE INITIALIZATION
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference profileRef = database.getReference("profiles");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                            Intent intent = new Intent(Login.this, Room.class);
                            intent.putExtra("Username", usernameInput);
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
                addProfileToFirebaseDB();
            }
        }
    }
    //Write in the Database
    private void addProfileToFirebaseDB() {
        profileRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData
                                                            mutableData) {
                mutableData.child("username").setValue(userProfile.username);
                mutableData.child("password").setValue(userProfile.password);
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(@Nullable DatabaseError databaseError,
                                   boolean b, @Nullable DataSnapshot
                                           dataSnapshot) {
            }
        });
    }
}