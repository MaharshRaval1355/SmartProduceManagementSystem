package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import java.util.Random;

public class RegulatorsFragment extends Fragment {

    private static final String[] MESSAGES = {
            "What changes do we need to make today?",
            "Is the humidity okay?",
            "Check if the temperature settings are optimal.",
            "Remember to adjust the lighting for better energy conservation.",
            "Verify that all sensors are functioning properly.",
            "Ensure the system is calibrated correctly.",
            "Review the energy usage reports from yesterday.",
            "Perform a quick system diagnostics check."
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_regulators, container, false);

        // Set a random greeting message
        TextView greetingTextView = view.findViewById(R.id.regulators_text_view);
        Random random = new Random();
        greetingTextView.setText(MESSAGES[random.nextInt(MESSAGES.length)]);

        // Delegate setup tasks to the helper class
        RegulatorsHelper.setupCard(view, R.id.deli_card, R.id.deli_controls_layout);
        RegulatorsHelper.setupCard(view, R.id.bakery_card, R.id.bakery_controls_layout);
        RegulatorsHelper.setupCard(view, R.id.drinks_card, R.id.drinks_controls_layout);
        RegulatorsHelper.setupCard(view, R.id.produce_card, R.id.produce_controls_layout);
        RegulatorsHelper.setupCard(view, R.id.icecream_card, R.id.icecream_controls_layout);
        RegulatorsHelper.setupCard(view, R.id.dairy_card, R.id.dairy_controls_layout);

        return view;
    }
}
