package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FeedbackHelper {

    private static final String PREFS_NAME = "FeedbackPrefs";
    protected static final String LAST_SUBMISSION_TIME = "LastSubmissionTime"; // Base key
    private static final long SUBMISSION_INTERVAL = 24 * 60 * 60 * 1000; // 24 hours in milliseconds

    private Context context;
    private ProgressBar progressBar;
    private String userEmail;// New variable to store the user identifier (email)

    public FeedbackHelper(Context context, ProgressBar progressBar, String userEmail) {
        this.context = context;
        this.progressBar = progressBar;
        this.userEmail = userEmail;  // Initialize with the user's email
    }

    private String getSubmissionTimeKey() {
        return LAST_SUBMISSION_TIME + "_" + userEmail;  // Unique key based on the user's email
    }

    public boolean canSubmitFeedback() {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        long lastSubmissionTime = preferences.getLong(getSubmissionTimeKey(), 0);
        long currentTime = System.currentTimeMillis();

        return (currentTime - lastSubmissionTime) >= SUBMISSION_INTERVAL;
    }

    public long getRemainingTime() {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        long lastSubmissionTime = preferences.getLong(getSubmissionTimeKey(), 0);
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastSubmissionTime;
        long remainingTime = SUBMISSION_INTERVAL - elapsedTime;

        return Math.max(remainingTime, 0);
    }

    public String getDeviceModel() {
        return android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
    }

    public void saveFeedbackToFirestore(String name, String phone, String email, String comment, float rating, String deviceModel) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        Map<String, Object> feedback = new HashMap<>();
        feedback.put("name", name);
        feedback.put("phone", phone);
        feedback.put("email", email);
        feedback.put("comment", comment);
        feedback.put("rating", rating);
        feedback.put("deviceModel", deviceModel);

        firestore.collection("Feedback")
                .add(feedback)
                .addOnSuccessListener(documentReference -> {
                    progressBar.setVisibility(View.GONE); // Hide progress bar
                    saveLastSubmissionTime(); // Save the submission timestamp
                    Toast.makeText(context, "Feedback Submitted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE); // Hide progress bar
                    Toast.makeText(context, "Failed to submit feedback: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveLastSubmissionTime() {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(getSubmissionTimeKey(), System.currentTimeMillis());
        editor.apply();
    }

    public void saveFeedbackToFirestore(String testFeedback, Context mockContext, ProgressBar mockProgressBar, String comment) {
    }
}
