package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Random;

public class DashboardFragment extends Fragment {

    // Define the array of greetings
    private String[] greetings = {
            "Hello there,\nlooks like it's gonna be a great day",
            "Good day!\nReady to manage your inventory?",
            "Hello! All systems operational.",
            "Welcome back!\nYour insights are ready.",
            "Hi there,\ntime to check today’s metrics!"
    };

    private TextView greetingTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        greetingTextView = view.findViewById(R.id.greeting_text_view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateGreeting(); // Update greeting text each time the fragment is accessed
    }

    private void updateGreeting() {
        int idx = new Random().nextInt(greetings.length); // Get a random index
        greetingTextView.setText(greetings[idx]); // Set the TextView with a random greeting
    }
}
