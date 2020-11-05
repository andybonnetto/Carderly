package com.example.carddisplaytest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();
    private Profile userProfile = null;
    private static final int REGISTER_PROFILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button rButton = findViewById(R.id.RegisterButton);
        rButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEditProfile = new Intent(LoginActivity.this, EditProfileActivity.class);
                startActivityForResult(intentEditProfile, REGISTER_PROFILE);

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

    public void clickedLoginButtonXmlCallback(View view) {
        TextView mTextView = findViewById(R.id.LoginMessage);
        if (userProfile != null) {
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("userProfileWelcome",userProfile);
            writeDB(3,"Profile");
            startActivity(intent);
        } else {
            mTextView.setText("You are not registered yet!");
            mTextView.setTextColor(Color.RED);
        }
    }

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

}