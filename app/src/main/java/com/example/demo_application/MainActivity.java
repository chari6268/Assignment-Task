package com.example.demo_application;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText textView1, textView2, textView3;
    private Button btnDisplay, btnAdd;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    ListView listView;
    ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = findViewById(R.id.editText);
        textView2 = findViewById(R.id.edit1Text);
        textView3 = findViewById(R.id.edit2Text);

        listView = findViewById(R.id.listView);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("demo_task");

        btnDisplay = findViewById(R.id.btnDisplay);
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> {
            String id = textView1.getText().toString().trim();
            String name = textView2.getText().toString().trim();
            String phone = textView3.getText().toString().trim();

            if(id.isEmpty() || name.isEmpty() || phone.isEmpty()){
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }else{
                User user = new User(id, name, phone);
                databaseReference.push().setValue(user)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplication(), "details submitted successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplication(), "Failed to submit details. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getApplication(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        btnDisplay.setOnClickListener(v -> {
            databaseReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    users.clear(); // Clear the list before adding new data
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        User user = snapshot.getValue(User.class);
                        users.add(user);
                    }
                    userAdapters userAdapter = new userAdapters(this, users);
                    listView.setAdapter(userAdapter);
                } else {
                    Toast.makeText(getApplication(), "Failed to fetch data. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        });


    }
}
