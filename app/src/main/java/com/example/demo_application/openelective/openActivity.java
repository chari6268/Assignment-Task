package com.example.demo_application.openelective;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.demo_application.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class openActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private Button loginButton;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("students");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);

        // Initialize UI Elements
        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v ->{
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            if(username.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "All input fields required", Toast.LENGTH_SHORT).show();
                // Show error message
            }else{
                // Perform login from firebase
                myRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            // User exists
                            String dbPassword = snapshot.child("pass").getValue(String.class);
                            if(dbPassword.equals(password)){
                                // Login successful
                                userData user = snapshot.getValue(userData.class);
                                System.out.println(user.getName());
                                Toast.makeText(openActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                // sent to userDetails activity

                                Intent intent = new Intent(openActivity.this, userDetails.class);
                                intent.putExtra("username", username);
                                startActivity(intent);
                            }else{
                                // Password incorrect
                                Toast.makeText(openActivity.this, "Password incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            // User does not exist
                            Toast.makeText(openActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Error occurred
                        Toast.makeText(openActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}