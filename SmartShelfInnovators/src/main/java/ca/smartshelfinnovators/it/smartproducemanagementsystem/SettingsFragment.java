package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    View view;
    protected SharedPreferences.Editor editor;
    protected Switch lockScreenSwitch;
    protected Switch darkModeSwitch;
    protected boolean isDarkModeEnabled;
    protected boolean isLockScreenPortrait;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);

        darkModeSwitch = view.findViewById(R.id.darkMode_switch);
        lockScreenSwitch = view.findViewById(R.id.lockScreen_switch);

        getContext();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(getString(R.string.settingsprefs), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();




        isDarkModeEnabled = sharedPreferences.getBoolean(getString(R.string.darkmodekey), false);

        isLockScreenPortrait = sharedPreferences.getBoolean(getString(R.string.lockScreenKey), false);


        darkModeSwitch.setChecked(isDarkModeEnabled);
        lockScreenSwitch.setChecked(isLockScreenPortrait);

        applyDarkMode(isDarkModeEnabled);
        applyLockScreenMode(isLockScreenPortrait);

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {



            editor.putBoolean(getString(R.string.darkmodekey), isChecked);


            editor.apply();
            applyDarkMode(isChecked);
        });

        lockScreenSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            editor.putBoolean(getString(R.string.lock_screen), isChecked);

            editor.apply();
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
