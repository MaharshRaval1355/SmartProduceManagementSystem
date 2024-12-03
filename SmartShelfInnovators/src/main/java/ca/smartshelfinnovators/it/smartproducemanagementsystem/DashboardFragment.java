package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        greetingTextView = view.findViewById(R.id.greeting_text_view);
        alertDetails = view.findViewById(R.id.alertDetails);
        stockDetails = view.findViewById(R.id.stockDetails);
        sensorDetails = view.findViewById(R.id.sensorDetails);
        fabRefresh = view.findViewById(R.id.fab_refresh);

        randomizeContent();

        fabRefresh.setOnClickListener(v -> randomizeContent());

        setupCardListeners(view);
        updateGreeting();

        return view;
    }

    private void setupCardListeners(View view) {
        CardView alertCard = view.findViewById(R.id.alertCard);
        alertCard.setOnClickListener(v -> toggleExclusiveDetails(alertDetails, stockDetails, sensorDetails));

        CardView stockCard = view.findViewById(R.id.stockCard);
        stockCard.setOnClickListener(v -> toggleExclusiveDetails(stockDetails, alertDetails, sensorDetails));

        CardView sensorCard = view.findViewById(R.id.sensorCard);
        sensorCard.setOnClickListener(v -> toggleExclusiveDetails(sensorDetails, alertDetails, stockDetails));
    }

    private void toggleExclusiveDetails(TextView currentView, TextView... otherViews) {
        for (TextView view : otherViews) {
            view.setVisibility(View.GONE);
        }

        if (currentView.getVisibility() == View.GONE) {
            currentView.setVisibility(View.VISIBLE);
        } else {
            currentView.setVisibility(View.GONE);
        }
    }

    private void randomizeContent() {
        alertDetails.setText(getRandomMessages(alertMessages, 2));
        stockDetails.setText(getRandomMessages(stockMessages, 3));
        sensorDetails.setText(getRandomMessages(sensorMessages, 3));

        // Store the data in Firestore
        storeDataInFirestore();
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

    // Method to store data in Firestore
    private void storeDataInFirestore() {
        String greeting = greetingTextView.getText().toString();
        String alerts = alertDetails.getText().toString();
        String stock = stockDetails.getText().toString();
        String sensors = sensorDetails.getText().toString();

        // Create a new DashboardData object
        DashboardData dashboardData = new DashboardData(greeting, alerts, stock, sensors);

        // Get Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Store the data in Firestore
        db.collection("dashboardData")
                .document("latestData")
                .set(dashboardData)
                .addOnSuccessListener(aVoid -> {
                    // Data stored successfully
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }

    public class DashboardData {
        private String greeting;
        private String alertMessages;
        private String stockMessages;
        private String sensorMessages;

        // Default constructor required for Firestore
        public DashboardData() {}

        public DashboardData(String greeting, String alertMessages, String stockMessages, String sensorMessages) {
            this.greeting = greeting;
            this.alertMessages = alertMessages;
            this.stockMessages = stockMessages;
            this.sensorMessages = sensorMessages;
        }

        // Getters and setters
        public String getGreeting() {
            return greeting;
        }

        public void setGreeting(String greeting) {
            this.greeting = greeting;
        }

        public String getAlertMessages() {
            return alertMessages;
        }

        public void setAlertMessages(String alertMessages) {
            this.alertMessages = alertMessages;
        }

        public String getStockMessages() {
            return stockMessages;
        }

        public void setStockMessages(String stockMessages) {
            this.stockMessages = stockMessages;
        }

        public String getSensorMessages() {
            return sensorMessages;
        }

        public void setSensorMessages(String sensorMessages) {
            this.sensorMessages = sensorMessages;
        }
    }

}
