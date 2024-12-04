package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsFragment extends Fragment {

    View view;
    Intent intent;
    FirebaseUser currentUser;
    protected TextView userNameTV, feedbackTV, logOutTV;
    protected SharedPreferences.Editor editor;
    protected Switch lockScreenSwitch;
    protected Switch darkModeSwitch;
    protected boolean isDarkModeEnabled;
    protected boolean isLockScreenPortrait;

   

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);

        userNameTV = view.findViewById(R.id.user_name);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String displayName = currentUser.getDisplayName();
            userNameTV.setText(displayName != null ? displayName : getString(R.string.guest_user));
        } else {
            userNameTV.setText(R.string.no_user_logged_in);
        }

        darkModeSwitch = view.findViewById(R.id.darkMode_switch);
        lockScreenSwitch = view.findViewById(R.id.lockScreen_switch);

        getContext();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(getString(R.string.settingsprefs), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        isDarkModeEnabled = sharedPreferences.getBoolean(getString(R.string.darkModeKey), false);
        isLockScreenPortrait = sharedPreferences.getBoolean(getString(R.string.lockScreenKey), false);

        darkModeSwitch.setChecked(isDarkModeEnabled);
        lockScreenSwitch.setChecked(isLockScreenPortrait);

        applyDarkMode(isDarkModeEnabled);
        applyLockScreenMode(isLockScreenPortrait);

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean(getString(R.string.darkModeKey), isChecked);
            editor.apply();
            applyDarkMode(isChecked);
        });

        lockScreenSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean(getString(R.string.lock_screen), isChecked);
            editor.apply();
            applyLockScreenMode(isChecked);
        });

        // Find the Feedback TextView
        feedbackTV = view.findViewById(R.id.feedback_textview);

        // Set the click listener to open FeedbackFragment
        feedbackTV.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new FeedbackFragment()) // Replace with your fragment container ID
                    .addToBackStack(null) // Adds this transaction to the back stack for back navigation
                    .commit();
        });

        // Logout Button
        logOutTV = view.findViewById(R.id.logOut_textview);

        // Set the click listener to perform logout
        logOutTV.setOnClickListener(v -> {
            // Create a Snackbar with indefinite duration
            Snackbar snackbar = Snackbar.make(view, R.string.confirm_logout, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.yes, confirmView -> {
                        // Firebase sign out
                        FirebaseAuth.getInstance().signOut();

                        // Clear local user session data
                        editor.clear();
                        editor.apply();

                        // Navigate to SignUp Activity
                        intent = new Intent(requireActivity(), SignUp.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        // Notify user
                        Toast.makeText(requireContext(), R.string.logged_out_successfully, Toast.LENGTH_SHORT).show();
                    })
                    .setActionTextColor(getResources().getColor(R.color.snackbar_action)); // Optional: Customize action text color

            // Show the Snackbar
            snackbar.show();

            // Override back button behavior to dismiss the Snackbar instead of navigating back
            requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    if (snackbar.isShown()) {
                        snackbar.dismiss(); // Dismiss the Snackbar
                    } else {
                        setEnabled(false); // Restore default back button behavior
                        requireActivity().onBackPressed(); // Navigate back
                    }
                }
            });
        });
        return view;
    }

    private void applyDarkMode(boolean isEnabled) {
        if (isEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    // Method to apply Lock Screen orientation
    private void applyLockScreenMode(boolean isPortrait) {
        if (isPortrait) {
            requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }
}
