package com.example.demo_application.openelective;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demo_application.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userDetails extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("students");
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details);
        textView = findViewById(R.id.userDetails);
        textView.setText("data loading...\n Please wait");
        getDetails(getIntent().getStringExtra("username"));
    }

    private void getDetails(String username) {
        myRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                        String name = snapshot.child("name").getValue(String.class);
                        String branch = snapshot.child("branch").getValue(String.class);
                        String year = snapshot.child("year").getValue(String.class);
                        String section = snapshot.child("sem").getValue(String.class);
                        textView.setText("Name: " + name + "\nBranch: " + branch + "\nYear: " + year + "\nSem: " + section);
                        Toast.makeText(userDetails.this, "Name: " + name + "\nBranch: " + branch + "\nYear: " + year + "\nSection: " + section, Toast.LENGTH_SHORT).show();
                    }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Error occurred
                Toast.makeText(userDetails.this, "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
