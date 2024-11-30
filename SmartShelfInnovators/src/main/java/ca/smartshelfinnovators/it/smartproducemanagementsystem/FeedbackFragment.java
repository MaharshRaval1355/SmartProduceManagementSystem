package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;


import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class FeedbackFragment extends Fragment {

    private EditText etName, etPhone, etEmail, etComment;
    private RatingBar ratingBar;
    private ProgressBar progressBar;

    protected final String PREFS_NAME = getString(R.string.feedbackprefs);
    protected final String LAST_SUBMISSION_TIME = getString(R.string.lastsubmissiontime);
    protected final long SUBMISSION_INTERVAL = 86400000; // 24 hours in milliseconds

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        // Initialize UI elements
        etName = view.findViewById(R.id.et_name);
        etPhone = view.findViewById(R.id.et_phone);
        etEmail = view.findViewById(R.id.et_email);
        etComment = view.findViewById(R.id.et_comment);
        ratingBar = view.findViewById(R.id.ratingBar);
        progressBar = view.findViewById(R.id.progressBar);

        // Back Button
        Button btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> showExitConfirmationDialog());

        // Submit Button
        view.findViewById(R.id.btn_submit).setOnClickListener(v -> submitFeedback());

        return view;
    }

    private void submitFeedback() {
        if (!canSubmitFeedback()) {
            long remainingTime = getRemainingTime();
            showRemainingTimeToast(remainingTime);
            return;
        }

        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String comment = etComment.getText().toString().trim();
        float rating = ratingBar.getRating();
        String deviceModel = getDeviceModel();

        // Validate inputs
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || rating == 0) {
            Toast.makeText(getContext(), R.string.toast_validating_inputs, Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Simulate a delay for submission
        new Handler().postDelayed(() -> saveFeedbackToFirestore(name, phone, email, comment, rating, deviceModel), 5000);
    }

    private boolean canSubmitFeedback() {
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        long lastSubmissionTime = preferences.getLong(LAST_SUBMISSION_TIME, 0);
        long currentTime = System.currentTimeMillis();

        return (currentTime - lastSubmissionTime) >= SUBMISSION_INTERVAL;
    }

    private void saveFeedbackToFirestore(String name, String phone, String email, String comment, float rating, String deviceModel) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        Map<String, Object> feedback = new HashMap<>();
        feedback.put(getString(R.string.full_name), name);
        feedback.put(getString(R.string.phone), phone);
        feedback.put(getString(R.string.email), email);
        feedback.put(getString(R.string.comment), comment);
        feedback.put(getString(R.string.rating), rating);
        feedback.put(getString(R.string.devicemodel), deviceModel);

        firestore.collection(getString(R.string.feedback))
                .add(feedback)
                .addOnSuccessListener(documentReference -> {
                    progressBar.setVisibility(View.GONE); // Hide progress bar
                    saveLastSubmissionTime(); // Save the submission timestamp
                    showConfirmationDialog(); // Show success confirmation dialog
                    clearInputs(); // Clear input fields
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE); // Hide progress bar
                    Toast.makeText(getContext(), getString(R.string.failed_to_submit_feedback) + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void clearInputs() {
        etName.setText("");
        etPhone.setText("");
        etEmail.setText("");
        etComment.setText("");
        ratingBar.setRating(0);
    }

    private void saveLastSubmissionTime() {
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(LAST_SUBMISSION_TIME, System.currentTimeMillis());
        editor.apply();
    }

    private long getRemainingTime() {
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        long lastSubmissionTime = preferences.getLong(LAST_SUBMISSION_TIME, 0);
        long currentTime = System.currentTimeMillis();

        return SUBMISSION_INTERVAL - (currentTime - lastSubmissionTime);
    }

    private void showRemainingTimeToast(long remainingTime) {
        long hours = TimeUnit.MILLISECONDS.toHours(remainingTime);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(remainingTime) % 60;

        String timeString = String.format(Locale.getDefault(), getString(R.string.d_hours_and_d_minutes), hours, minutes);
        Toast.makeText(getContext(), getString(R.string.you_can_submit_feedback_again_in) + timeString, Toast.LENGTH_LONG).show();
    }

    private String getDeviceModel() {
        return android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.feedback_submitted)
                .setMessage(R.string.thank_you_for_your_feedback)
                .setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    // Show an AlertDialog for confirmation
    public void showExitConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.confirm_exit)
                .setMessage(R.string.are_you_sure_you_want_to_go_back)
                .setIcon(R.drawable.warning)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    // Go back to the previous screen
                    requireActivity().getSupportFragmentManager().popBackStack();
                })
                .setNegativeButton(R.string.no, (dialog, which) -> {
                    // Dismiss the dialog
                    dialog.dismiss();
                })
                .create()
                .show();
    }
}