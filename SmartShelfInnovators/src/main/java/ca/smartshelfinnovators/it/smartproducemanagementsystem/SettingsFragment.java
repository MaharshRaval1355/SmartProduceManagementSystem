package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the switch for Dark Mode
        Switch darkModeSwitch = view.findViewById(R.id.dark_mode_switch);

        // Set up the switch toggle listener
        darkModeSwitch.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            if (isChecked) {
                // Logic for enabling dark mode
                Toast.makeText(getContext(), "Dark Mode Enabled", Toast.LENGTH_SHORT).show();
            } else {
                // Logic for disabling dark mode
                Toast.makeText(getContext(), "Dark Mode Disabled", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

