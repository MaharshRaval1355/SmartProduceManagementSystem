package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsFragment extends Fragment {

    private static final int REQUEST_NOTIFICATION_PERMISSION = 101;

    View view;
    Intent intent;
    FirebaseUser currentUser;
    protected TextView userNameTV, feedbackTV, logOutTV;
    protected SharedPreferences.Editor editor;
    protected SharedPreferences sharedPreferences;
    protected Switch lockScreenSwitch, darkModeSwitch, notificationSwitch;
    protected boolean isDarkModeEnabled, isLockScreenPortrait, isNotificationEnabled;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);

        ConstraintLayout profileSection = view.findViewById(R.id.profile_section);

        // Initialize user information
        userNameTV = view.findViewById(R.id.user_name);
        // Fetch user details from Firestore
        fetchUserDetails(userNameTV);

        // Set click listener for profile section
        profileSection.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ProfileFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Initialize switches and shared preferences
        darkModeSwitch = view.findViewById(R.id.darkMode_switch);
        lockScreenSwitch = view.findViewById(R.id.lockScreen_switch);
        notificationSwitch = view.findViewById(R.id.notification_switch);

        sharedPreferences = requireContext().getSharedPreferences(getString(R.string.userprefs), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        isDarkModeEnabled = sharedPreferences.getBoolean(getString(R.string.darkModeKey), false);
        isLockScreenPortrait = sharedPreferences.getBoolean(getString(R.string.lockScreenKey), false);
        isNotificationEnabled = sharedPreferences.getBoolean(getString(R.string.notificationsKey), true);

        darkModeSwitch.setChecked(isDarkModeEnabled);
        lockScreenSwitch.setChecked(isLockScreenPortrait);
        notificationSwitch.setChecked(isNotificationEnabled);

        applyDarkMode(isDarkModeEnabled);
        applyLockScreenMode(isLockScreenPortrait);

        // Dark mode toggle listener
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean(getString(R.string.darkModeKey), isChecked);
            editor.apply();
            applyDarkMode(isChecked);
        });

        // Lock screen orientation toggle listener
        lockScreenSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean(getString(R.string.lockScreenKey), isChecked);
            editor.apply();
            applyLockScreenMode(isChecked);
        });

        // Notification toggle listener
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    // For API 33+ request runtime permission
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(requireActivity(),
                                new String[]{Manifest.permission.POST_NOTIFICATIONS},
                                REQUEST_NOTIFICATION_PERMISSION);
                    } else {
                        enableNotifications();
                    }
                } else {
                    enableNotifications();
                }
            } else {
                disableNotifications();
            }
        });

        // Feedback text view click listener
        feedbackTV = view.findViewById(R.id.feedback_textview);
        feedbackTV.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new FeedbackFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Logout button with Snackbar confirmation
        logOutTV = view.findViewById(R.id.logOut_textview);
        logOutTV.setOnClickListener(v -> {
            Snackbar snackbar = Snackbar.make(view, R.string.confirm_logout, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.yes, confirmView -> {
                        FirebaseAuth.getInstance().signOut();
                        editor.clear();
                        editor.apply();
                        intent = new Intent(requireActivity(), Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toast.makeText(requireContext(), R.string.logged_out_successfully, Toast.LENGTH_SHORT).show();
                    })
                    .setActionTextColor(getResources().getColor(R.color.snackbar_action));
            snackbar.show();

            requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    if (snackbar.isShown()) {
                        snackbar.dismiss();
                    } else {
                        setEnabled(false);
                        requireActivity().onBackPressed();
                    }
                }
            });
        });

        return view;
    }

    private void fetchUserDetails(TextView userNameTV) {
        // Get the current user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid(); // Get user UID

            // Reference the Firestore database
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Reference the 'Users' collection and the current user's document
            DocumentReference userRef = db.collection("Users").document(uid);

            // Fetch the user data
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Retrieve fields
                        String name = document.getString("name");

                        // Display data in TextView
                        userNameTV.setText(name != null ? name : "Guest User");
                    } else {
                        userNameTV.setText("No user data found in the database.");
                        Log.d("Firestore", "No such document for UID: " + uid);
                    }
                } else {
                    Log.e("Firestore", "Error fetching document: ", task.getException());
                    Toast.makeText(requireContext(), "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            userNameTV.setText("No user logged in.");
        }
    }

    private void enableNotifications() {
        editor.putBoolean(getString(R.string.notificationsKey), true);
        editor.apply();
        Toast.makeText(requireContext(), R.string.notifications_enabled, Toast.LENGTH_SHORT).show();
    }

    private void disableNotifications() {
        editor.putBoolean(getString(R.string.notificationsKey), false);
        editor.apply();
        Toast.makeText(requireContext(), R.string.notifications_disabled, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableNotifications();
            } else {
                Toast.makeText(requireContext(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
                notificationSwitch.setChecked(false);
            }
        }
    }

    private void applyDarkMode(boolean isEnabled) {
        if (isEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void applyLockScreenMode(boolean isPortrait) {
        if (isPortrait) {
            requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }
}
