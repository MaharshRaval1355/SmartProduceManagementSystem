package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DashboardFragment extends Fragment {

    private String[] greetings = {
            "Hello there,\nlooks like it's gonna be a great day!",
            "Good day!\nReady to manage your inventory?",
            "Hello!\nAll systems operational.",
            "Welcome back!\nYour insights are ready.",
            "Hi there,\ntime to check today’s metrics!"
    };

    private String[] alertMessages = {
            "Unusual behavior in bakery section",
            "Cold Beverages require Immediate Temperature Check",
            "Stock depletion alert in dairy products",
            "Temperature rise detected in frozen foods aisle"
    };

    private String[] stockMessages = {
            "Deli: 30% - Needs Refill",
            "Produce: 80% - Good Enough",
            "IceCream: 50% - Running out.",
            "Beverages: 70% - Adequate",
            "Bakery: 60% - Consider Restocking"
    };

    private String[] sensorMessages = {
            "High temperature in Frozen Foods",
            "Lighting issues in Dairy",
            "Moderate humidity in Produce",
            "Excessive CO2 levels in Greenhouse",
            "Ventilation failure in Bakery",
            "Stable conditions in Meat Section"
    };

    private TextView greetingTextView, alertDetails, stockDetails, sensorDetails;
    private FloatingActionButton fabRefresh;
    private String randomAlert, randomStock, randomSensor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        greetingTextView = view.findViewById(R.id.greeting_text_view);
        alertDetails = view.findViewById(R.id.alertDetails);
        stockDetails = view.findViewById(R.id.stockDetails);
        sensorDetails = view.findViewById(R.id.sensorDetails);
        fabRefresh = view.findViewById(R.id.fab_refresh);

        // Generate initial content for the cards
        randomAlert = getRandomMessages(alertMessages, 2);
        randomStock = getRandomMessages(stockMessages, 3);
        randomSensor = getRandomMessages(sensorMessages, 2);

        // Assign initial text to keep details ready for display
        alertDetails.setText(randomAlert);
        stockDetails.setText(randomStock);
        sensorDetails.setText(randomSensor);

        fabRefresh.setOnClickListener(v -> refreshCardContent());

        initializeCardListeners(view);
        updateGreeting();

        return view;
    }

    private void initializeCardListeners(View view) {
        CardView alertCard = view.findViewById(R.id.alertCard);
        alertCard.setOnClickListener(v -> toggleExclusiveDetails(alertDetails));

        CardView stockCard = view.findViewById(R.id.stockCard);
        stockCard.setOnClickListener(v -> toggleExclusiveDetails(stockDetails));

        CardView sensorCard = view.findViewById(R.id.sensorCard);
        sensorCard.setOnClickListener(v -> toggleExclusiveDetails(sensorDetails));
    }

    private void toggleExclusiveDetails(TextView detailsView) {
        // Toggle visibility
        if (detailsView.getVisibility() == View.VISIBLE) {
            detailsView.setVisibility(View.GONE);
        } else {
            detailsView.setVisibility(View.VISIBLE);
        }
    }

    private void refreshCardContent() {
        // Update messages
        randomAlert = getRandomMessages(alertMessages, 2);
        randomStock = getRandomMessages(stockMessages, 3);
        randomSensor = getRandomMessages(sensorMessages, 2);

        // Apply updated messages to TextViews
        alertDetails.setText(randomAlert);
        stockDetails.setText(randomStock);
        sensorDetails.setText(randomSensor);
    }

    private void updateGreeting() {
        greetingTextView.setText(greetings[new Random().nextInt(greetings.length)]);
    }

    private String getRandomMessages(String[] messages, int count) {
        Set<Integer> indices = new HashSet<>();
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        while (indices.size() < count) {
            int index = random.nextInt(messages.length);
            if (indices.add(index)) {
                builder.append(messages[index]).append("\n");
            }
        }
        return builder.toString().trim();
    }
}
