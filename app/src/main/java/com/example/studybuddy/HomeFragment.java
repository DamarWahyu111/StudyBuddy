package com.example.studybuddy;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private LinearLayout taskContainer; //

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Main layout inflation
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        databaseHelper = new DatabaseHelper(getActivity());

        // Ambil kontainer untuk tugas
        taskContainer = view.findViewById(R.id.taskContainer);
        return view;
    }

    @Override
    public void onResume() { // onResume is used to retrieve the task list when the fragment is active again
        super.onResume();
        displayTasks();
    }

    private void displayTasks() {
        Cursor cursor = databaseHelper.getAllData(); // Retrieve all data from the database
        if (cursor == null || cursor.getCount() == 0) {
            // Clear the taskContainer if there are no tasks
            taskContainer.removeAllViews();
            Toast.makeText(getActivity(), "No tasks found", Toast.LENGTH_SHORT).show();
            if (cursor != null) cursor.close();
            return;
        }
        // Clean up the taskContainer so that there is no duplication
        taskContainer.removeAllViews();
        while (cursor.moveToNext()) { // cursor reads an object from the database which is generated from the query db
            // Add layout for each task item
            View taskView = LayoutInflater.from(getActivity()).inflate(R.layout.task_item, taskContainer, false);

            // Get elemen from layout CreateTask
            TextView taskTitle = taskView.findViewById(R.id.tvTaskTitle);
            TextView taskDescription = taskView.findViewById(R.id.tvTaskDescription);
            Button btnTaskDetail = taskView.findViewById(R.id.btnTaskDetail);
            Button btnUpdateTask = taskView.findViewById(R.id.btnUpdateTask);
            Button btnDeleteTask = taskView.findViewById(R.id.btnDeleteTask);

            // Fetch data from the cursor
            String id = cursor.getString(0);
            String title = cursor.getString(1);
            String description = cursor.getString(2);
            String deadline = cursor.getString(3);
            String priority = cursor.getString(4);

            // Set task title (keep default color)
            taskTitle.setText("Exam " + id);

            // Text format with title and status
            String formattedText = title + " - ";
            taskDescription.setText(formattedText);

            // Change only the color of the status text (priority)
            switch (priority.toLowerCase()) {
                case "high":
                    taskDescription.append(createColoredText(priority, R.color.priority_high));
                    break;
                case "medium":
                    taskDescription.append(createColoredText(priority, R.color.priority_medium));
                    break;
                case "low":
                    taskDescription.append(createColoredText(priority, R.color.priority_low));
                    break;
                default:
                    taskDescription.append(priority); // Default warna hitam
            }
            btnTaskDetail.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), ActivityTaskDetail.class);
                intent.putExtra("TITLE", title);
                intent.putExtra("DESCRIPTION", description);
                intent.putExtra("DEADLINE", deadline);
                intent.putExtra("PRIORITY", priority);
                startActivity(intent);
            });
            btnUpdateTask.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), ActivityUpdateTask.class);
                intent.putExtra("ID", id);
                intent.putExtra("TITLE", title);
                intent.putExtra("DESCRIPTION", description);
                intent.putExtra("DEADLINE", deadline);
                intent.putExtra("PRIORITY", priority);
                Log.d("UpdateTask", "ID: " + id); // Log untuk memeriksa ID yang dikirim
                startActivity(intent);
            });
            btnDeleteTask.setOnClickListener(v -> {
                Log.d("DeleteTask", "ID: " + id); // Logs are used to record logs on Android,
                                                            // that .d is for debugging.
                boolean isDeleted = databaseHelper.deleteTask(id);
                if (isDeleted) {
                    Toast.makeText(getActivity(), "Task Deleted", Toast.LENGTH_SHORT).show();
                    displayTasks(); // Refresh the task list view after deletion
                } else {
                    Toast.makeText(getActivity(), "Failed to Delete Task", Toast.LENGTH_SHORT).show();
                }
            });
            // Add a task view to the container
            taskContainer.addView(taskView);
        }
        cursor.close();
    }

    // Method to make colored text for status only
    private SpannableString createColoredText(String text, int colorRes) {
        SpannableString coloredText = new SpannableString(text);
        coloredText.setSpan(
                new ForegroundColorSpan(getResources().getColor(colorRes)),
                0,
                text.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        return coloredText;
    }
}
