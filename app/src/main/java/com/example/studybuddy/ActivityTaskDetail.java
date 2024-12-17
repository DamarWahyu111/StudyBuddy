package com.example.studybuddy;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityTaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // Ambil data dari Intent
        String title = getIntent().getStringExtra("TITLE");
        String description = getIntent().getStringExtra("DESCRIPTION");
        String deadline = getIntent().getStringExtra("DEADLINE");
        String priority = getIntent().getStringExtra("PRIORITY");

        // Hubungkan dengan layout
        TextView tvTitle = findViewById(R.id.tvTitleDetail);
        TextView tvDescription = findViewById(R.id.tvDescriptionDetail);
        TextView tvDeadline = findViewById(R.id.tvDeadlineDetail);
        TextView tvPriority = findViewById(R.id.tvPriorityDetail);
        Button btnBack = findViewById(R.id.btnBack);

        // Set data ke view
        tvTitle.setText(title);
        tvDescription.setText(description);
        tvDeadline.setText(deadline);
        tvPriority.setText(priority);

        // Ubah warna teks Priority berdasarkan nilai
        switch (priority.toLowerCase()) {
            case "high":
                tvPriority.setTextColor(getResources().getColor(R.color.priority_high));
                break;
            case "medium":
                tvPriority.setTextColor(getResources().getColor(R.color.priority_medium));
                break;
            case "low":
                tvPriority.setTextColor(getResources().getColor(R.color.priority_low));
                break;
            default:
                tvPriority.setTextColor(getResources().getColor(android.R.color.black));
        }

        // Back button
        btnBack.setOnClickListener(v -> finish());
    }
}
