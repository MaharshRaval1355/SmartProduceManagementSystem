package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore; // Firestore instance
    private EditText signupEmail, signupPassword, userName, phoneNumber;
    private Button signupButton;
    private TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            // User is already logged in, navigate to MainActivity
            startActivity(new Intent(SignUp.this, MainActivity.class));
            finish();
        }

        // Initialize UI elements
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        userName = findViewById(R.id.user_name); // Full name EditText
        phoneNumber = findViewById(R.id.phone_number); // Phone number EditText
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = signupEmail.getText().toString().trim();
                String password = signupPassword.getText().toString().trim();
                String name = userName.getText().toString().trim(); // Get full name
                String phone = phoneNumber.getText().toString().trim(); // Get phone number

                // Validate input fields
                if (email.isEmpty()) {
                    signupEmail.setError(getString(R.string.email_cannot_be_empty));
                    return;
                }
                if (password.isEmpty()) {
                    signupPassword.setError(getString(R.string.password_cannot_be_empty));
                    return;
                }
                if (name.isEmpty()) {
                    userName.setError(getString(R.string.full_name_cannot_be_empty));
                    return;
                }
                if (phone.isEmpty()) {
                    phoneNumber.setError(getString(R.string.phone_number_cannot_be_empty));
                    return;
                }

                // Create user with email and password
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Get the user's unique ID
                            String userId = auth.getCurrentUser().getUid();

                            // Create a map to store user information
                            Map<String, Object> user = new HashMap<>();
                            user.put("name", name);
                            user.put("email", email);
                            user.put("phone", phone);
                            user.put("password", password); // Note: storing plain text password is not secure

                            // Store user information in Firestore
                            firestore.collection("Users").document(userId).set(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SignUp.this, getString(R.string.signup_successful), Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SignUp.this, Login.class));
                                            } else {
                                                Toast.makeText(SignUp.this, getString(R.string.signup_failed) + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(SignUp.this,getString(R.string.signup_failed) + task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, Login.class));
            }
        });
    }
}
