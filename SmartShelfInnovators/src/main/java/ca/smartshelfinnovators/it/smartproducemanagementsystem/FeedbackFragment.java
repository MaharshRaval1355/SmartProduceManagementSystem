package ca.smartshelfinnovators.it.smartproducemanagementsystem;

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
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FeedbackFragment extends Fragment {

    private EditText etName, etPhone, etEmail, etComment;
    private RatingBar ratingBar;
    private ProgressBar progressBar;
    private Button btnSubmit;  // Submit button reference
    private FeedbackHelper feedbackHelper;

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
        btnSubmit = view.findViewById(R.id.btn_submit);  // Initialize the submit button

        // Initialize FeedbackHelper
        feedbackHelper = new FeedbackHelper(requireContext(), progressBar, getString(R.string.user_email));

        // Back Button
        Button btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            // Go back to the previous screen directly
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Submit Button
        btnSubmit.setOnClickListener(v -> submitFeedback());

        // Disable the button if feedback has already been submitted recently
        if (!feedbackHelper.canSubmitFeedback()) {
            long remainingTime = feedbackHelper.getRemainingTime();
            showRemainingTimeOnButton(remainingTime);
            disableSubmitButton();
        }

        return view;
    }

    private void submitFeedback() {
        if (!feedbackHelper.canSubmitFeedback()) {
            long remainingTime = feedbackHelper.getRemainingTime();
            showRemainingTimeOnButton(remainingTime);
            return;
        }

        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String comment = etComment.getText().toString().trim();
        float rating = ratingBar.getRating();
        String deviceModel = feedbackHelper.getDeviceModel();

        // Validate inputs
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || rating == 0) {
            Toast.makeText(getContext(), getString(R.string.please_fill_all_fields_and_provide_a_rating), Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Simulate a delay for submission
        new Handler().postDelayed(() -> {
            feedbackHelper.saveFeedbackToFirestore(name, phone, email, comment, rating, deviceModel);

            // Clear fields after submission
            etName.setText(R.string.empty);
            etPhone.setText(R.string.empty);
            etEmail.setText(R.string.empty);
            etComment.setText(R.string.empty);
            ratingBar.setRating(0);  // Reset rating bar

            disableSubmitButton();  // Disable button after submission
        }, 5000);
    }

    private void disableSubmitButton() {
        btnSubmit.setEnabled(false);
        btnSubmit.setBackgroundColor(getResources().getColor(R.color.grey));  // Make button gray
        updateButtonWithRemainingTime();
    }

    private void updateButtonWithRemainingTime() {
        long remainingTime = feedbackHelper.getRemainingTime();
        if (remainingTime > 0) {
            showRemainingTimeOnButton(remainingTime);  // Call the existing method to display the time
        } else {
            enableSubmitButton();  // Re-enable the submit button when the time is up
        }
    }

    private void enableSubmitButton() {
        btnSubmit.setEnabled(true);
        btnSubmit.setBackgroundColor(getResources().getColor(R.color.primary));  // Restore original color
    }

    private void showRemainingTimeOnButton(long remainingTime) {
        long hours = TimeUnit.MILLISECONDS.toHours(remainingTime);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(remainingTime) % 60;

        String timeString = String.format(Locale.getDefault(), getString(R.string.wait_d_hours_d_min), hours, minutes);
        btnSubmit.setText(timeString);

        // Update the remaining time periodically
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                long updatedRemainingTime = feedbackHelper.getRemainingTime();
                if (updatedRemainingTime > 0) {
                    showRemainingTimeOnButton(updatedRemainingTime);
                    // Continue updating until the time is up
                    new Handler().postDelayed(this, 60000);  // Update every minute
                } else {
                    enableSubmitButton();  // Re-enable the button when time is up
                }
            }
        }, 60000);  // Update every minute
    }

    private void showRemainingTimeToast(long remainingTime) {
        long hours = TimeUnit.MILLISECONDS.toHours(remainingTime);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(remainingTime) % 60;

        String timeString = String.format(Locale.getDefault(), getString(R.string.d_hours_and_d_minutes), hours, minutes);
        Toast.makeText(getContext(), getString(R.string.you_can_submit_feedback_again_in) + timeString, Toast.LENGTH_LONG).show();
    }

}

