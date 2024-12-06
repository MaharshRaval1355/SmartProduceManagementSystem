package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.cardview.widget.CardView;

public class RegulatorsHelper {

    // Method to set up a card with toggle functionality and seek bars
    public static void setupCard(View parentView, int cardViewId, int controlsLayoutId) {
        CardView cardView = parentView.findViewById(cardViewId);
        final LinearLayout controlsLayout = parentView.findViewById(controlsLayoutId);

        // Set up click listener to toggle visibility
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlsLayout.getVisibility() == View.VISIBLE) {
                    controlsLayout.setVisibility(View.GONE);
                } else {
                    controlsLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        // Set up seek bars inside the controls layout
        setupSeekBars(controlsLayout);
    }

    // Method to set up seek bars with functionality for humidity and temperature controls
    private static void setupSeekBars(LinearLayout controlsLayout) {
        SeekBar humiditySeekBar = controlsLayout.findViewById(R.id.humidity_control);
        SeekBar temperatureSeekBar = controlsLayout.findViewById(R.id.temperature_control);
        TextView humidityValueTextView = controlsLayout.findViewById(R.id.humidity_value);
        TextView temperatureValueTextView = controlsLayout.findViewById(R.id.temperature_value);

        // Configure the humidity seek bar
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

        // Configure the temperature seek bar
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
