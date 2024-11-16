package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Login extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100; // Request code for Google Sign-In
    private EditText loginEmail, loginPassword;
    private TextView signupRedirectText;
    private Button loginButton, googleSignInButton;
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;
    private CheckBox rememberMeCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the user is already logged in
        if (isUserLoggedIn()) {
            // Navigate directly to the main screen
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
            return; // Skip the rest of onCreate
        }

        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signUpRedirectText);
        googleSignInButton = findViewById(R.id.googleSignInButton);
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);
        auth = FirebaseAuth.getInstance();

        // Initialize Google Sign-In options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Email/Password Login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString();
                String pass = loginPassword.getText().toString();

                // Validate email
                if (!isValidEmail(email)) {
                    loginEmail.setError(getString(R.string.please_enter_correct_email));
                    return;
                }

                // Validate password
                if (!isValidPassword(pass)) {
                    loginPassword.setError(getString(R.string.invalid_password_format));
                    return;
                }

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!pass.isEmpty()) {
                        auth.signInWithEmailAndPassword(email, pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(Login.this, R.string.login_successful, Toast.LENGTH_SHORT).show();

                                        // Save login state if "Remember Me" is checked
                                        if (rememberMeCheckBox.isChecked()) {
                                            saveLoginState(email, pass);
                                        }

                                        startActivity(new Intent(Login.this, MainActivity.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Login.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        loginPassword.setError(getString(R.string.empty_fields_are_not_allowed));
                    }
                } else if (email.isEmpty()) {
                    loginEmail.setError(getString(R.string.empty_fields_are_not_allowed));
                } else {
                    loginEmail.setError(getString(R.string.please_enter_correct_email));
                }
            }
        });

        // Google Sign-In
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, SignUp.class));
            }
        });
    }

    // email validation  using regex
    private boolean isValidEmail(String email) {
        // Simple email validation regex pattern
        String emailPattern = getString(R.string.regex_email_validation);
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // password validation using regex
    private boolean isValidPassword(String password) {
        // Password validation regex pattern:
        // - At least 6 characters
        // - At least one uppercase letter
        // - At least one digit
        // - At least one special character
        String passwordPattern = getString(R.string.regex_password_validation);
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


    private boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.userprefs), MODE_PRIVATE);
        return sharedPreferences.getBoolean(getString(R.string.loggedin), false); // Check if the "loggedIn" flag is true
    }

    private void saveLoginState(String email, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.userprefs), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.loggedin), true); // Save login state
        editor.putString(getString(R.string.email), email);
        editor.putString(getString(R.string.password), password);
        editor.apply();
    }

    private void clearLoginState() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.userprefs), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all saved preferences
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(Login.this, R.string.google_sign_in_failed, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            Toast.makeText(Login.this, R.string.login_successful, Toast.LENGTH_SHORT).show();
                            saveLoginState(user.getEmail(), null); // Save login state for Google users
                            startActivity(new Intent(Login.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(Login.this, R.string.google_sign_in_failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
