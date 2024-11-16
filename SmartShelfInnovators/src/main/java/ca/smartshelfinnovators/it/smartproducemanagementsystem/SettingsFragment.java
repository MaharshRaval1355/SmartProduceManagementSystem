package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    View view;
    protected Switch lockScreenSwitch;
    protected Switch darkModeSwitch;
    protected SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);

        darkModeSwitch = view.findViewById(R.id.darkMode_switch);
        lockScreenSwitch = view.findViewById(R.id.lockScreen_switch);

        sharedPreferences = requireContext().getSharedPreferences("SettingsPrefs", Context.MODE_PRIVATE);

        boolean isDarkModeEnabled = sharedPreferences.getBoolean("dark_mode", false);
        boolean isLockScreenPortrait = sharedPreferences.getBoolean("lock_screen", false);

        darkModeSwitch.setChecked(isDarkModeEnabled);
        lockScreenSwitch.setChecked(isLockScreenPortrait);

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply();
            applyDarkMode(isChecked);
        });

        lockScreenSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean("lock_screen", isChecked).apply();
            applyLockScreenMode(isChecked);
        });

        return view;
    }

    private void applyDarkMode(boolean isEnabled) {
        if (isEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        // Notify the system that the fragment needs to be recreated to apply themes immediately
        if (getActivity() != null) {
            getActivity().recreate();
        }
    }

    // Method to apply Lock Screen orientation
    private void applyLockScreenMode(boolean isPortrait) {
        if (getActivity() != null) {
            if (isPortrait) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }
        }
    }
}
