package com.example.demo_application;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextId, editTextName, editTextPhone;
    private Button btnDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize EditTexts
        editTextId = findViewById(R.id.editText);
        editTextName = findViewById(R.id.edit1Text);
        editTextPhone = findViewById(R.id.edit2Text);

        // Initialize Display Button
        btnDisplay = findViewById(R.id.btnDisplay);

        // Set Click Listener for Display Button
        btnDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from EditTexts
                String id = editTextId.getText().toString().trim();
                String name = editTextName.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                editTextId.setText("");
                editTextName.setText("");
                editTextPhone.setText("");

                // Display values using Toast
                Toast.makeText(MainActivity.this,
                        "ID: " + id + "\nName: " + name + "\nPhone: " + phone,
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
