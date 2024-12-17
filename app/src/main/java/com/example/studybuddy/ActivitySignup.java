package com.example.studybuddy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class ActivitySignup extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        
        sharedPreferences = getSharedPreferences("StudyBuddyPrefs", MODE_PRIVATE);
        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        EditText etBirthday = findViewById(R.id.etBirthDate);
        EditText etPhoneNumber = findViewById(R.id.etPhoneNumber);
        Button btnSignup = findViewById(R.id.btnSignUp);

        etBirthday.setOnClickListener(v -> { // meaning of -> the lambda Expressionion
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ActivitySignup.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> { // arti dari -> itu lambda Expresiion
                        String date = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                        etBirthday.setText(date);
                    },
                    year,
                    month,
                    day
            );
            datePickerDialog.show();
        });

        // The logic of raffle sign ups
        btnSignup.setOnClickListener(view -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String birthday = etBirthday.getText().toString().trim();
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            // input validation
            if (username.isEmpty() || password.isEmpty() || birthday.isEmpty() || phoneNumber.isEmpty()) { // in the code there is || the OR operator logic he checks for empty validation
                                                                                                            // if validation is empty then there is a Toast as message.
                Toast.makeText(ActivitySignup.this, "please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // save the data to Shared Preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", username);
            editor.putString("password", password);
            editor.putString("birthday", birthday);
            editor.putString("phoneNumber", phoneNumber);
            editor.apply();

            // notification to be provided upon successful account creation
            Toast.makeText(ActivitySignup.this, "Successful account creation", Toast.LENGTH_SHORT).show(); // LENTH_SHORT it is to provide time-definite notifications
            startActivity(new Intent(ActivitySignup.this, ActivityLogin.class));
            finish();
        });
    }
}

