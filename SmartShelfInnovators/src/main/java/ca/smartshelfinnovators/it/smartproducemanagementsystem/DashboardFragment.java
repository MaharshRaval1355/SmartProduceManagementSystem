package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DashboardFragment extends Fragment {

    // Define the array of greetings
    private String[] greetings = {
            "Hello there, looks like it's gonna be a great day!",
            "Good day! Ready to manage your inventory?",
            "Hello! All systems operational.",
            "Welcome back! Your insights are ready.",
            "Hi there, time to check today’s metrics!"
    };

    // Define the array of alert messages
    private String[] alertMessages = {
            "Unusual behavior in bakery section",
            "Cold Beverages require Immediate Temperature Check",
            "Stock depletion alert in dairy products",
            "Temperature rise detected in frozen foods aisle"
    };

    // Define the array of stock messages
    private String[] stockMessages = {
            "Deli: 30% - Needs Refill",
            "Produce: 80% - Good Enough",
            "IceCream: 50% - Running out.",
            "Beverages: 70% - Adequate",
            "Bakery: 60% - Consider Restocking"
    };

    private TextView greetingTextView;
    private TextView alertDetails;
    private TextView stockDetails;
    private boolean isAlertsExpanded = false;
    private boolean isStockExpanded = false;

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
                alertDetails.setText(getRandomAlertMessages());
                isAlertsExpanded = true;
            } else {
                alertDetails.setVisibility(View.GONE);
                isAlertsExpanded = false;
            }
        });

        // Initialize the stock TextView and CardView
        CardView stockCard = view.findViewById(R.id.stockCard);
        stockDetails = view.findViewById(R.id.stockDetails);
        stockCard.setOnClickListener(v -> {
            if (!isStockExpanded) {
                stockDetails.setVisibility(View.VISIBLE);
                stockDetails.setText(getRandomStockMessages());
                isStockExpanded = true;
            } else {
                stockDetails.setVisibility(View.GONE);
                isStockExpanded = false;
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
        int idx = new Random().nextInt(greetings.length);
        greetingTextView.setText(greetings[idx]);
    }

    private String getRandomAlertMessages() {
        Random random = new Random();
        int firstMsgIndex = random.nextInt(alertMessages.length);
        int secondMsgIndex;
        do {
            secondMsgIndex = random.nextInt(alertMessages.length);
        } while (secondMsgIndex == firstMsgIndex);
        return alertMessages[firstMsgIndex] + "\n" + alertMessages[secondMsgIndex];
    }

    private String getRandomStockMessages() {
        Random random = new Random();
        Set<Integer> usedIndices = new HashSet<>();
        StringBuilder sb = new StringBuilder();
        while (usedIndices.size() < 3) {
            int index = random.nextInt(stockMessages.length);
            if (!usedIndices.contains(index)) {
                usedIndices.add(index);
                sb.append(stockMessages[index]).append("\n");
            }
        }
        return sb.toString().trim();
    }
}
