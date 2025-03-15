package com.example.demo_application.course;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CourseMain extends AppCompatActivity {

    private TextView userDetails, selectedCourses;
    private Spinner oeSpinner, deSpinner, mhSpinner;
    private Button submitButton;
    private DatabaseReference databaseReference;
    private String username;
    private String studentBranch;
    private boolean isEmptyFields = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_main);

        userDetails = findViewById(R.id.userDetails);
        selectedCourses = findViewById(R.id.selectedCourses);
        oeSpinner = findViewById(R.id.oeSpinner);
        deSpinner = findViewById(R.id.deSpinner);
        mhSpinner = findViewById(R.id.mhSpinner);
        submitButton = findViewById(R.id.submitButton);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        username = getIntent().getStringExtra("username");

        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Error: No username found!", Toast.LENGTH_SHORT).show();
            return;
        }

        checkStudentDetails();
        submitButton.setOnClickListener(v -> saveSelectedCourses());
    }

    private void checkStudentDetails() {
        databaseReference.child("students").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    studentBranch = snapshot.child("branch").getValue(String.class);
                    String oe = snapshot.child("oe").getValue(String.class);
                    String de = snapshot.child("de").getValue(String.class);
                    String mh = snapshot.child("mh").getValue(String.class);

                    boolean isOEEmpty = oe == null || oe.isEmpty();
                    boolean isDEEmpty = de == null || de.isEmpty();
                    boolean isMHEmpty = mh == null || mh.isEmpty();

                    isEmptyFields = isOEEmpty || isDEEmpty || isMHEmpty;

                    userDetails.setText("Name: " + name + "\nBranch: " + studentBranch);

                    if (!isOEEmpty && !isDEEmpty && !isMHEmpty) {
                        selectedCourses.setText("OE: " + oe + "\nDE: " + de + "\nMH: " + mh);
                        oeSpinner.setVisibility(View.GONE);
                        deSpinner.setVisibility(View.GONE);
                        mhSpinner.setVisibility(View.GONE);
                        submitButton.setVisibility(View.GONE);
                    } else {
                        loadCoursesFromDatabase();
                    }
                } else {
                    Toast.makeText(CourseMain.this, "Student not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CourseMain.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCoursesFromDatabase() {
        if (studentBranch == null || studentBranch.isEmpty()) {
            Toast.makeText(this, "Branch not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child("course").child(studentBranch).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> courses = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String courseName = child.child("name").getValue(String.class);
                    if (courseName != null) courses.add(courseName);
                }

                if (!courses.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CourseMain.this,
                            android.R.layout.simple_spinner_dropdown_item, courses);

                    oeSpinner.setAdapter(adapter);
                    deSpinner.setAdapter(adapter);
                    mhSpinner.setAdapter(adapter);

                    oeSpinner.setVisibility(View.VISIBLE);
                    deSpinner.setVisibility(View.VISIBLE);
                    mhSpinner.setVisibility(View.VISIBLE);
                    submitButton.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(CourseMain.this, "No courses available!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CourseMain.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveSelectedCourses() {
        if (oeSpinner.getSelectedItem() == null || deSpinner.getSelectedItem() == null || mhSpinner.getSelectedItem() == null) {
            Toast.makeText(this, "Please select all courses!", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedOE = oeSpinner.getSelectedItem().toString();
        String selectedDE = deSpinner.getSelectedItem().toString();
        String selectedMH = mhSpinner.getSelectedItem().toString();

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("oe", selectedOE);
        updateMap.put("de", selectedDE);
        updateMap.put("mh", selectedMH);

        databaseReference.child("students").child(username).updateChildren(updateMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CourseMain.this, "Courses Updated Successfully!", Toast.LENGTH_SHORT).show();

                    // Hide selection fields
                    submitButton.setVisibility(View.GONE);
                    oeSpinner.setVisibility(View.GONE);
                    deSpinner.setVisibility(View.GONE);
                    mhSpinner.setVisibility(View.GONE);

                    selectedCourses.setVisibility(View.VISIBLE);
                    selectedCourses.setText("OE: " + selectedOE + "\nDE: " + selectedDE + "\nMH: " + selectedMH);
                })
                .addOnFailureListener(e -> Toast.makeText(CourseMain.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
