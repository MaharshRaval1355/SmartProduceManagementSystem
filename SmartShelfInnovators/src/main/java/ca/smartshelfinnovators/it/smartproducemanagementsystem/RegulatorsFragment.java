package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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
        setupCard(view, R.id.deli_card, R.id.deli_controls_layout);
        setupCard(view, R.id.bakery_card, R.id.bakery_controls_layout);
        setupCard(view, R.id.drinks_card, R.id.drinks_controls_layout);
        setupCard(view, R.id.produce_card, R.id.produce_controls_layout);
        setupCard(view, R.id.icecream_card, R.id.icecream_controls_layout);
        setupCard(view, R.id.dairy_card, R.id.dairy_controls_layout);

        Random random = new Random();
        greetingTextView.setText(MESSAGES[random.nextInt(MESSAGES.length)]);

        return view;
    }

    private void setupCard(View parentView, int cardViewId, int controlsLayoutId) {
        CardView cardView = parentView.findViewById(cardViewId);
        final LinearLayout controlsLayout = parentView.findViewById(controlsLayoutId);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle visibility of the controls
                if (controlsLayout.getVisibility() == View.VISIBLE) {
                    controlsLayout.setVisibility(View.GONE);
                } else {
                    controlsLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        setupSeekBars(controlsLayout);
    }

    private void setupSeekBars(LinearLayout controlsLayout) {
        final SeekBar humiditySeekBar = controlsLayout.findViewById(R.id.humidity_control);
        final SeekBar temperatureSeekBar = controlsLayout.findViewById(R.id.temperature_control);
        final TextView humidityValueTextView = controlsLayout.findViewById(R.id.humidity_value);
        final TextView temperatureValueTextView = controlsLayout.findViewById(R.id.temperature_value);

        humiditySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                humidityValueTextView.setText(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optional: Implement if needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optional: Implement if needed
            }
        });

        temperatureSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                temperatureValueTextView.setText(progress + "°C");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optional: Implement if needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optional: Implement if needed
            }
        });
    }
}
