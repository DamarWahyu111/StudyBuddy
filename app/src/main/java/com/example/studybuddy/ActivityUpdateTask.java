package com.example.studybuddy;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class ActivityUpdateTask extends AppCompatActivity {
    private static final int MAX_DESCRIPTION_LENGTH = 800; // Batas maksimum deskripsi

    private DatabaseHelper databaseHelper; // Inisialisasi database helper
    private EditText etDeadline;
    private Spinner spinnerPriority; // Spinner untuk priority
    private TextView tvCharacterCount; // TextView untuk jumlah karakter tersisa


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task); // Menghubungkan dengan layout update_task.xml

        databaseHelper = new DatabaseHelper(this);

        String id = getIntent().getStringExtra("ID");
        String title = getIntent().getStringExtra("TITLE");
        String description = getIntent().getStringExtra("DESCRIPTION");
        String deadline = getIntent().getStringExtra("DEADLINE");
        String priority = getIntent().getStringExtra("PRIORITY");

        EditText etTitle = findViewById(R.id.etUpdateTitle);
        EditText etDescription = findViewById(R.id.etUpdateDescription);
        etDeadline = findViewById(R.id.etUpdateDeadline);
        spinnerPriority = findViewById(R.id.UpdateSpinnerPriority);
        Button btnSaveUpdate = findViewById(R.id.btnSaveUpdate);
        Button btnBackUpdate = findViewById(R.id.btnBackUpdate);
        tvCharacterCount = findViewById(R.id.tvUpdateCharacterCount); // Tambahkan inisialisasi untuk TextView

        // Set data awal ke input field
        etTitle.setText(title);
        etDescription.setText(description);
        etDeadline.setText(deadline);

        // Set jumlah karakter tersisa berdasarkan deskripsi awal
        int remainingChars = MAX_DESCRIPTION_LENGTH - description.length();
        tvCharacterCount.setText(remainingChars + " characters remaining");

        // pada bagian TextWatcher untuk menghitung karakter yang tersisa ketika mengetik
        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Tidak digunakan
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int remainingChars = MAX_DESCRIPTION_LENGTH - s.length();
                tvCharacterCount.setText(remainingChars + " Characters");
                if (remainingChars <= 0) {
                    Toast.makeText(ActivityUpdateTask.this, "Character limit reached", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Tidak digunakan
            }
        });
        // Set up Spinner data
        setupPrioritySpinner(priority);
        // Set DatePicker untuk Deadline
        etDeadline.setOnClickListener(v -> showDatePickerDialog());
        // Simpan perubahan task
        btnSaveUpdate.setOnClickListener(v -> {
            if (id == null || id.isEmpty()) {
                Toast.makeText(this, "Invalid Task ID", Toast.LENGTH_SHORT).show();
                return;
            }

            String newTitle = etTitle.getText().toString();
            String newDescription = etDescription.getText().toString();
            String newDeadline = etDeadline.getText().toString();
            String newPriority = spinnerPriority.getSelectedItem().toString();

            boolean isUpdated = databaseHelper.updateTask(id, newTitle, newDescription, newDeadline, newPriority);

            if (isUpdated) {
                Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show();
                finish(); // Kembali ke aktivitas sebelumnya (HomeFragment)
            } else {
                Toast.makeText(this, "Failed to update task", Toast.LENGTH_SHORT).show();
            }
        });
        btnBackUpdate.setOnClickListener(v -> {
            finish(); // Menutup ActivityUpdateTask
        });
    }

    // ini untuk mewarnai drop down
    private void setupPrioritySpinner(String selectedPriority) {
        // Gunakan PrioritySpinnerAdapter untuk spinner
        String[] priorities = {"High", "Medium", "Low"};
        PrioritySpinnerAdapter adapter = new PrioritySpinnerAdapter(this, priorities);
        spinnerPriority.setAdapter(adapter);

        // Set Spinner ke nilai yang diterima dari Intent
        if (selectedPriority != null) {
            int spinnerPosition = adapter.getPosition(selectedPriority);
            spinnerPriority.setSelection(spinnerPosition);
        }
    }


    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            selectedMonth += 1; // Kalender berbasis 0 untuk bulan
            String date = selectedDay + "/" + selectedMonth + "/" + selectedYear;
            etDeadline.setText(date);
        }, year, month, day);
        datePickerDialog.show();
    }
}
