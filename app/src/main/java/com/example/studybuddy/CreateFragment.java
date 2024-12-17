package com.example.studybuddy;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;

public class CreateFragment extends Fragment {

    private EditText etTitle, etDescription;
    private EditText etDeadline;
    private Spinner spinnerPriority;
    private Button btnSave;
    private DatabaseHelper databaseHelper;
    private TextView tvCharacterCount; // TextView untuk jumlah karakter tersisa

    private static final int MAX_DESCRIPTION_LENGTH = 800;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        // Inisialisasi komponen
        etTitle = view.findViewById(R.id.etTitle);
        etDescription = view.findViewById(R.id.etDescription);
        etDeadline = view.findViewById(R.id.etDeadline);
        spinnerPriority = view.findViewById(R.id.spinnerPriority);
        btnSave = view.findViewById(R.id.btnSave);
        databaseHelper = new DatabaseHelper(getActivity());
        tvCharacterCount = view.findViewById(R.id.tvCharacterCount);

        // Atur spinner dengan adapter custom PrioritySpinnerAdapter
        String[] priorities = {"High", "Medium", "Low"};
        PrioritySpinnerAdapter priorityAdapter = new PrioritySpinnerAdapter(getContext(), priorities);
        spinnerPriority.setAdapter(priorityAdapter);

        // Add TextWatcher to count the remaining characters
        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Tidak digunakan
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Update jumlah karakter tersisa
                int remainingChars = MAX_DESCRIPTION_LENGTH - s.length();
                tvCharacterCount.setText(remainingChars + " Characters");

                // Tampilkan notifikasi jika batas tercapai
                if (remainingChars <= 0) {
                    Toast.makeText(getActivity(), "Character limit reached", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Tidak digunakan
            }
        });

        // Tampilkan DatePicker saat EditText Deadline ditekan
        etDeadline.setOnClickListener(v -> showDatePickerDialog());

        // Listener untuk tombol Save
        btnSave.setOnClickListener(v -> saveTask());
        return view;
    }

    // in this section to display the calendar
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view, selectedYear, selectedMonth, selectedDay) -> {
            selectedMonth += 1; // Bulan dimulai dari 0
            String date = selectedDay + "/" + selectedMonth + "/" + selectedYear;
            etDeadline.setText(date);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void saveTask() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String deadline = etDeadline.getText().toString().trim();
        String priority = spinnerPriority.getSelectedItem().toString();

        // Validasi input
        if (title.isEmpty() || description.isEmpty() || deadline.isEmpty() || priority.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        // Simpan data ke database
        boolean isInserted = databaseHelper.insertTask(title, description, deadline, priority);
        if (isInserted) {
            Toast.makeText(getActivity(), "Task Saved", Toast.LENGTH_SHORT).show();
            // Bersihkan input fields
            clearFields();
            // Navigasi ke HomeFragment dan muat ulang data
            navigateToHomeFragment();
        } else {
            Toast.makeText(getActivity(), "Error Saving Task", Toast.LENGTH_SHORT).show();
        }
    }

    // untuk clearFields ketika sudah melakukan save data
    private void clearFields() {
        etTitle.setText("");
        etDescription.setText("");
        etDeadline.setText("");
        spinnerPriority.setSelection(0); // Reset spinner ke pilihan pertama
    }

    // ketika melakukan save dia akan ke halaman HomeFragment
    private void navigateToHomeFragment() {
        HomeFragment homeFragment = new HomeFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, homeFragment); // ID layout container utama
        fragmentTransaction.addToBackStack(null); // Tambahkan ke back stack (opsional)
        fragmentTransaction.commit();
    }
}
