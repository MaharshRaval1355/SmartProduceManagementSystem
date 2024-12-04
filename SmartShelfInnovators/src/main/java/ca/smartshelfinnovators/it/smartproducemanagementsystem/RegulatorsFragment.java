package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
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

        TextView greetingTextView = view.findViewById(R.id.regulators_text_view);
        final LinearLayout deliControlsLayout = view.findViewById(R.id.deli_controls_layout);
        CardView deliCard = view.findViewById(R.id.deli_card);

        Random random = new Random();
        greetingTextView.setText(MESSAGES[random.nextInt(MESSAGES.length)]);

        deliCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle visibility of the controls
                if (deliControlsLayout.getVisibility() == View.VISIBLE) {
                    deliControlsLayout.setVisibility(View.GONE);
                } else {
                    deliControlsLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }
}
