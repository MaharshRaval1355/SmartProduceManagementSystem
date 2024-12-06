package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import static androidx.core.content.ContextCompat.startActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;


public class SignUp extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword, userName, phoneNumber;
    private Button signupButton;
    private TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)  // Enable offline persistence
                .build();
        firestore.setFirestoreSettings(settings);


        // Initialize UI elements
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        userName = findViewById(R.id.user_name);
        phoneNumber = findViewById(R.id.phone_number);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSignUp();
            }
        });

        loginRedirectText.setOnClickListener(view -> {
            startActivity(new Intent(SignUp.this, Login.class));
        });
    }

    private void handleSignUp() {
        String email = signupEmail.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();
        String name = userName.getText().toString().trim();
        String phone = phoneNumber.getText().toString().trim();

        // Validate input fields
        if (!ValidationUtils.isValidEmail(this, email)) {
            signupEmail.setError(getString(R.string.invalid_email));
            return;
        }
        if (!ValidationUtils.isValidPassword(this, password)) {
            signupPassword.setError(getString(R.string.invalid_password));
            return;
        }
        if (name.isEmpty()) {
            userName.setError(getString(R.string.full_name_cannot_be_empty));
            return;
        }
        if (!ValidationUtils.isValidPhone(this, phone)) {
            phoneNumber.setError(getString(R.string.invalid_phone_number));
            return;
        }

        // Check if the device is online or offline
        if (!isNetworkConnected()) {
            // Show a Toast message indicating offline status
            Toast.makeText(SignUp.this, "You're offline. Showing cached data.", Toast.LENGTH_SHORT).show();
        }
        // Fetch user data from Firestore
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Users").document(email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Display user data if available
                            userName.setText(document.getString("name"));
                            phoneNumber.setText(document.getString("phone"));
                        }
                    } else {
                        Toast.makeText(SignUp.this, "Error fetching data: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Register user
        FirebaseUtils.registerUser(auth, email, password, name, phone, new FirebaseUtils.FirebaseCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(SignUp.this, getString(R.string.signup_successful), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUp.this, Login.class));
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(SignUp.this, getString(R.string.signup_failed) + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Check if the device is connected to the network
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
