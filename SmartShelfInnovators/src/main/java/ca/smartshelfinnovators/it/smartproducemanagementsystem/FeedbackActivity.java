package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import android.os.Build;

public class FeedbackActivity extends AppCompatActivity {

    // Declare UI components
    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText commentEditText;
    private RatingBar ratingBar;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Initialize UI components
        nameEditText = findViewById(R.id.feedbackName);
        phoneEditText = findViewById(R.id.feedbackPhoneNumber);
        emailEditText = findViewById(R.id.feedbackEmail);
        commentEditText = findViewById(R.id.feedbackComment);
        ratingBar = findViewById(R.id.feedbackRating);
        submitButton = findViewById(R.id.submitFeedbackButton);

        // Set OnClickListener for the submit button
        submitButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String comment = commentEditText.getText().toString().trim();
            float rating = ratingBar.getRating();

            // Validate inputs
            if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || comment.isEmpty() || rating == 0) {
                Toast.makeText(FeedbackActivity.this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
                return; // Stop if validation fails
            }

            // Fetch device model
            String deviceModel = Build.MODEL;

            // Prepare the feedback data object
            Feedback feedback = new Feedback(name, phone, email, comment, rating, deviceModel);

            // Store data in Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("customer_feedback")
                    .add(feedback)
                    .addOnSuccessListener(documentReference -> {
                        // Show success message
                        Toast.makeText(FeedbackActivity.this, "Feedback submitted successfully!", Toast.LENGTH_SHORT).show();
                        finish();  // Close the activity after submitting feedback
                    })
                    .addOnFailureListener(e -> {
                        // Show error message
                        Toast.makeText(FeedbackActivity.this, "Error submitting feedback: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    // Feedback class to structure feedback data
    public static class Feedback {
        private String name;
        private String phone;
        private String email;
        private String comment;
        private float rating;
        private String deviceModel;

        public Feedback(String name, String phone, String email, String comment, float rating, String deviceModel) {
            this.name = name;
            this.phone = phone;
            this.email = email;
            this.comment = comment;
            this.rating = rating;
            this.deviceModel = deviceModel;
        }

        // Getters for Firestore to use field names for document
        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public String getEmail() {
            return email;
        }

        public String getComment() {
            return comment;
        }

        public float getRating() {
            return rating;
        }

        public String getDeviceModel() {
            return deviceModel;
        }
    }
}

