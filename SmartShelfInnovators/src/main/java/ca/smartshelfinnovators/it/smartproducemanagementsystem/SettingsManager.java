package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsManager {

    private final Context context;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public static final String DARK_MODE_KEY = "darkModeKey";
    public static final String LOCK_SCREEN_KEY = "lockScreenKey";
    public static final String NOTIFICATIONS_KEY = "notificationsKey";

    public SettingsManager(Context context, SharedPreferences sharedPreferences) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
        this.editor = sharedPreferences.edit();
    }

    public boolean isDarkModeEnabled() {
        return sharedPreferences.getBoolean(DARK_MODE_KEY, false);
    }

    public void setDarkMode(boolean isEnabled) {
        editor.putBoolean(DARK_MODE_KEY, isEnabled);
        editor.apply();
    }

    public boolean isLockScreenPortrait() {
        return sharedPreferences.getBoolean(LOCK_SCREEN_KEY, false);
    }

    public void setLockScreenPortrait(boolean isPortrait) {
        editor.putBoolean(LOCK_SCREEN_KEY, isPortrait);
        editor.apply();
    }

    public boolean isNotificationEnabled() {
        return sharedPreferences.getBoolean(NOTIFICATIONS_KEY, true);
    }

    public void setNotificationEnabled(boolean isEnabled) {
        editor.putBoolean(NOTIFICATIONS_KEY, isEnabled);
        editor.apply();
    }

    public void fetchUserDetails(TextView userNameTV) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userRef = db.collection("Users").document(uid);

            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = document.getString("name");
                        userNameTV.setText(name != null ? name : "Guest User");
                    } else {
                        userNameTV.setText("No user data found.");
                    }
                } else {
                    Toast.makeText(context, "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            userNameTV.setText("No user logged in.");
        }
    }
}
