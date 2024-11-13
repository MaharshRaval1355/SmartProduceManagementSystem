package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.Random;

public class DashboardFragment extends Fragment {

    // Define the array of greetings
    private String[] greetings = {
            "Hello there,\nlooks like it's gonna be a great day!",
            "Good day!\nReady to manage your inventory?",
            "Hello! All systems operational.",
            "Welcome back!\nYour insights are ready.",
            "Hi there,\ntime to check today’s metrics!"
    };

    // Define the array of alert messages
    private String[] alertMessages = {
            "Unusual behavior in bakery section",
            "Cold Beverages require Immediate Temperature Check",
            "Stock depletion alert in dairy products",
            "Temperature rise detected in frozen foods aisle"
    };

    private TextView greetingTextView;
    private TextView alertDetails;
    private boolean isAlertsExpanded = false; // Tracks expansion state of the alerts card

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Initialize the greeting TextView
        greetingTextView = view.findViewById(R.id.greeting_text_view);

        // Initialize the alerts TextView and CardView
        CardView alertCard = view.findViewById(R.id.alertCard);
        alertDetails = view.findViewById(R.id.alertDetails);

        alertCard.setOnClickListener(v -> {
            if (!isAlertsExpanded) {
                alertDetails.setVisibility(View.VISIBLE);
                alertDetails.setText(getRandomAlertMessages()); // Call to get two random messages
                isAlertsExpanded = true;
            } else {
                alertDetails.setVisibility(View.GONE);
                isAlertsExpanded = false;
            }
        });

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

    private String getRandomAlertMessages() {
        Random random = new Random();
        int firstMsgIndex = random.nextInt(alertMessages.length);
        int secondMsgIndex;
        do {
            secondMsgIndex = random.nextInt(alertMessages.length);
        } while (secondMsgIndex == firstMsgIndex); // Ensure a different message

        return alertMessages[firstMsgIndex] + "\n" + alertMessages[secondMsgIndex]; // Return two random alert messages
    }
}
