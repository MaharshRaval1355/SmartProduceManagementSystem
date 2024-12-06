package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    private static final int REQUEST_NOTIFICATION_PERMISSION = 101;

    private View view;
    private SettingsManager settingsManager;

    private TextView userNameTV, feedbackTV, logOutTV;
    private Switch darkModeSwitch, lockScreenSwitch, notificationSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize SettingsManager
        settingsManager = new SettingsManager(requireContext(),
                requireContext().getSharedPreferences(getString(R.string.userprefs), Context.MODE_PRIVATE));

        // UI elements
        userNameTV = view.findViewById(R.id.user_name);
        feedbackTV = view.findViewById(R.id.feedback_textview);
        logOutTV = view.findViewById(R.id.logOut_textview);

        darkModeSwitch = view.findViewById(R.id.darkMode_switch);
        lockScreenSwitch = view.findViewById(R.id.lockScreen_switch);
        notificationSwitch = view.findViewById(R.id.notification_switch);

        // Fetch and display user details
        settingsManager.fetchUserDetails(userNameTV);

        // Initialize switch states
        darkModeSwitch.setChecked(settingsManager.isDarkModeEnabled());
        lockScreenSwitch.setChecked(settingsManager.isLockScreenPortrait());
        notificationSwitch.setChecked(settingsManager.isNotificationEnabled());

        // Listeners
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsManager.setDarkMode(isChecked);
            applyDarkMode(isChecked);
        });

        lockScreenSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsManager.setLockScreenPortrait(isChecked);
            applyLockScreenMode(isChecked);
        });

        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.POST_NOTIFICATIONS},
                            REQUEST_NOTIFICATION_PERMISSION);
                } else {
                    settingsManager.setNotificationEnabled(true);
                    Toast.makeText(requireContext(), R.string.notifications_enabled, Toast.LENGTH_SHORT).show();
                }
            } else {
                settingsManager.setNotificationEnabled(false);
                Toast.makeText(requireContext(), R.string.notifications_disabled, Toast.LENGTH_SHORT).show();
            }
        });

        feedbackTV.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new FeedbackFragment())
                    .addToBackStack(null)
                    .commit();
        });

        logOutTV.setOnClickListener(v -> {
            Snackbar snackbar = Snackbar.make(view, R.string.confirm_logout, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.yes, confirmView -> {
                        FirebaseAuth.getInstance().signOut();
                        settingsManager.setDarkMode(false);
                        settingsManager.setLockScreenPortrait(false);
                        Intent intent = new Intent(requireActivity(), SignUp.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    });
            snackbar.show();
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

    private void applyLockScreenMode(boolean isPortrait) {
        if (isPortrait) {
            requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                settingsManager.setNotificationEnabled(true);
                Toast.makeText(requireContext(), R.string.notifications_enabled, Toast.LENGTH_SHORT).show();
            } else {
                notificationSwitch.setChecked(false);
                Toast.makeText(requireContext(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
