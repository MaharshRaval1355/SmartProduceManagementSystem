package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class FeedbackFragment extends Fragment {

    protected Button backButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        // Find the back button
        backButton = view.findViewById(R.id.back_button);

        // Handle the back button in the UI
        backButton.setOnClickListener(v -> showExitConfirmationDialog());

        // Handle physical back button using OnBackPressedDispatcher
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        showExitConfirmationDialog();
                    }
                }
        );

        return view;
    }

    // Show an AlertDialog for confirmation
    public void showExitConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirm Exit")
                .setMessage("Are you sure you want to go back?")
                .setIcon(R.drawable.warning)
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Go back to the previous screen
                    requireActivity().getSupportFragmentManager().popBackStack();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Dismiss the dialog
                    dialog.dismiss();
                })
                .create()
                .show();
    }
}