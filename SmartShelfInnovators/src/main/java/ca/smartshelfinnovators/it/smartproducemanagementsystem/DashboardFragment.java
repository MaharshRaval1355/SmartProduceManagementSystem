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

    private TextView greetingTextView, alertDetails, stockDetails, sensorDetails;
    private FloatingActionButton fabRefresh;
    protected String[] greetings = getResources().getStringArray(R.array.greetings);
    protected String[] alertMessages = getResources().getStringArray(R.array.alert_messages);
    protected String[] stockMessages = getResources().getStringArray(R.array.stock_messages);
    protected String[] sensorMessages = getResources().getStringArray(R.array.sensor_messages);

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
