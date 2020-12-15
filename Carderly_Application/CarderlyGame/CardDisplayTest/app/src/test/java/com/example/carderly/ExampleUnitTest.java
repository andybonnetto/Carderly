package com.example.carderly;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void databaseSetup() {
        // Delete from the database
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference childRef1 = mDatabase.getReference("rooms/" + "/Andy" + "/Cards");
        DatabaseReference childRef2 = mDatabase.getReference("rooms/" + "/Andy" + "/Current to play");
        DatabaseReference childRef3 = mDatabase.getReference("rooms/" + "/Andy" + "/First turn");
        DatabaseReference childRef4 = mDatabase.getReference("rooms/" + "/Andy" + "/GameEnd");
        DatabaseReference childRef5 = mDatabase.getReference("rooms/" + "/Andy" + "/OldPersonTrump");
        DatabaseReference childRef6 = mDatabase.getReference("rooms/" + "/Andy" + "/PlayedCard");
        DatabaseReference childRef7 = mDatabase.getReference("rooms/" + "/Andy" + "/Player 2");
        DatabaseReference childRef8 = mDatabase.getReference("rooms/" + "/Andy" + "/Player 3");
        DatabaseReference childRef9 = mDatabase.getReference("rooms/" + "/Andy" + "/Player 4");
        DatabaseReference childRef10 = mDatabase.getReference("rooms/" + "/Andy" + "/Trump");
        DatabaseReference childRef11 = mDatabase.getReference("rooms/" + "/Andy" + "/Turn end");
        DatabaseReference childRef12 = mDatabase.getReference("rooms/" + "/Andy" + "/Player 1" + "/Card 1");
        DatabaseReference childRef13 = mDatabase.getReference("rooms/" + "/Andy" + "/Player 1" + "/Card 2");
        DatabaseReference childRef14 = mDatabase.getReference("rooms/" + "/Andy" + "/Player 1" + "/Card 3");
        DatabaseReference childRef15 = mDatabase.getReference("rooms/" + "/Andy" + "/Player 1" + "/Card 4");
        DatabaseReference childRef16 = mDatabase.getReference("rooms/" + "/Andy" + "/Player 1" + "/Card 5");
        DatabaseReference childRef17 = mDatabase.getReference("rooms/" + "/Andy" + "/Player 1" + "/Card 6");
        DatabaseReference childRef18 = mDatabase.getReference("rooms/" + "/Andy" + "/Player 1" + "/Card 7");
        DatabaseReference childRef19 = mDatabase.getReference("rooms/" + "/Andy" + "/Player 1" + "/Card 8");
        DatabaseReference childRef20 = mDatabase.getReference("rooms/" + "/Andy" + "/Player 1" + "/Card played");

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