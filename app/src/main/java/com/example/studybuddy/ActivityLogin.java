package com.example.studybuddy;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityLogin extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize SharedPreferences to read user data
        sharedPreferences = getSharedPreferences("StudyBuddyPrefs", MODE_PRIVATE);
        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnSignup = findViewById(R.id.btnSignup);

        // btn login to be able to log in to the home page
        btnLogin.setOnClickListener(view -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            String savedUsername = sharedPreferences.getString("username", null);  // Ambil username yang tersimpan
            String savedPassword = sharedPreferences.getString("password", null);  // Ambil password yang tersimpan
            if (username.equals(savedUsername) && password.equals(savedPassword)) {
                // Save the logged-in username to display in ActivityHome
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("logged_in", username); // The "logged_in" key is used for logged-in users, and goes into the editText "Pengguna"
                editor.apply();

                Toast.makeText(ActivityLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ActivityLogin.this, ActivityHome.class));
                finish();
            } else {
                // If login fails
                Toast.makeText(ActivityLogin.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, ActivitySignup.class);
                startActivity(intent);
            }
        });
    }
}
