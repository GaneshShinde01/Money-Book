package com.gs.moneybook.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gs.moneybook.Database.DBHelper;
import com.gs.moneybook.R;
import com.gs.moneybook.databinding.FragmentCalendarBinding;

import java.util.Calendar;

public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding binding;
    private long selectedDate;
    private DBHelper dbHelper;
    private int userId = 1; // Assuming you have a userId variable, change as needed

    public CalendarFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        dbHelper = DBHelper.getInstance(requireContext());

        // Set current selected date to today's date
        selectedDate = binding.calendarView.getDate();


        // Handle date selection on calendar
        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                selectedDate = calendar.getTimeInMillis();

                // Check if a note/reminder exists for the selected date
                checkForReminder(selectedDate);
            }
        });

        return view;
    }

    // Check if any reminder exists for the selected date
    private void checkForReminder(long date) {
        String reminder = dbHelper.getReminderForDate(userId, date);
        if (reminder != null && !reminder.isEmpty()) {
            // If a reminder exists, show the reminder with Update/Delete options
            showNoteDialog(reminder, date);
        } else {
            // If no reminder exists, show Add Note button
            showAddNoteDialog(date);
        }
    }

    // Show a dialog to display the note or allow the user to update/delete it
    private void showNoteDialog(String reminder, long date) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Reminder");

        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_note, null);
        builder.setView(customLayout);

        TextView noteTextView = customLayout.findViewById(R.id.noteTextView);
        noteTextView.setText(reminder);

        // Create the dialog instance here so we can dismiss it later
        AlertDialog dialog = builder.create();

        // Set up the Update button
        customLayout.findViewById(R.id.updateButton).setOnClickListener(v -> {
            openUpdateDialog(date, reminder, dialog);  // Pass dialog to close it after update
        });

        // Set up the Delete button
        customLayout.findViewById(R.id.deleteButton).setOnClickListener(v -> {
            dbHelper.deleteReminderForDate(userId, date);  // Deletes the reminder from the database
            Toast.makeText(getContext(), "Note deleted!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();  // Close the dialog immediately after deletion
        });

        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Close", (dialogInterface, which) -> dialog.dismiss());
        dialog.show();
    }

    // Show a dialog to add a new note for the selected date
    private void showAddNoteDialog(long date) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("No Note Found");

        final EditText input = new EditText(getContext());
        input.setHint("Write your note here...");
        builder.setView(input);

        builder.setPositiveButton("Add Note", (dialog, which) -> {
            String note = input.getText().toString();
            dbHelper.addReminder(userId, date, note);  // Add the note to the database
            Toast.makeText(getContext(), "Note added!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    // Open a dialog to update the note for the selected date
    private void openUpdateDialog(long date, String oldNote, AlertDialog parentDialog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update Note");

        final EditText input = new EditText(getContext());
        input.setHint("Update your note...");
        input.setText(oldNote);  // Display the existing note
        builder.setView(input);

        // Set up the Save button
        builder.setPositiveButton("Save", (dialog, which) -> {
            String note = input.getText().toString();
            dbHelper.updateReminder(userId, date, note);  // Update the note in the database
            Toast.makeText(getContext(), "Note updated!", Toast.LENGTH_SHORT).show();
            parentDialog.dismiss();  // Close the parent dialog (reminder dialog)
        });

        // Set up the Cancel button
        builder.setNegativeButton("Cancel", (dialogInterface, which) -> dialogInterface.dismiss());

        // Create and show the dialog
        AlertDialog updateDialog = builder.create();
        updateDialog.show();  // Show the update dialog
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
