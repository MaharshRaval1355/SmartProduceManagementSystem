package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RegulatorsFragment extends Fragment {

    private static final List<String> MESSAGES = Arrays.asList(
            "What changes do we need to make today?",
            "Is the humidity okay?",
            "Check if the temperature settings are optimal.",
            "Remember to adjust the lighting for better energy conservation.",
            "Verify that all sensors are functioning properly.",
            "Ensure the system is calibrated correctly.",
            "Review the energy usage reports from yesterday.",
            "Perform a quick system diagnostics check."
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_regulators, container, false);

        TextView greetingTextView = view.findViewById(R.id.regulators_text_view);

        Random random = new Random();
        String message = MESSAGES.get(random.nextInt(MESSAGES.size()));

        greetingTextView.setText(message);

        return view;
    }
}
