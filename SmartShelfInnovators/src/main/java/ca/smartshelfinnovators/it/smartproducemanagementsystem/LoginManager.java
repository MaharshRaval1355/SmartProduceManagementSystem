package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.util.Patterns;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.AuthCredential;

import androidx.annotation.NonNull;

public class LoginManager {

    public static final int RC_SIGN_IN = 100; // Request code for Google Sign-In
    private Context context;
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;

    public LoginManager(Context context) {
        this.context = context;
        this.auth = FirebaseAuth.getInstance();
    }

    public boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.userprefs), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(context.getString(R.string.loggedin), false); // Check if the "loggedIn" flag is true
    }

    public boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidPassword(String password) {
        // Password validation can be implemented with a regex pattern or similar validation
        return password.length() >= 6; // Basic validation for length
    }

    public GoogleSignInClient initializeGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        return GoogleSignIn.getClient(context, gso);
    }

    public void loginWithEmailPassword(String email, String password, final LoginCallback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure("Login failed!"));
    }

    public void firebaseAuthWithGoogle(String idToken, final LoginCallback callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure("Google sign-in failed!");
                    }
                });
    }

    public void saveLoginState(String email, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.userprefs), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.loggedin), true); // Save login state
        editor.putString(context.getString(R.string.email), email);
        editor.putString(context.getString(R.string.password), password);
        editor.apply();
    }

    public interface LoginCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
}
