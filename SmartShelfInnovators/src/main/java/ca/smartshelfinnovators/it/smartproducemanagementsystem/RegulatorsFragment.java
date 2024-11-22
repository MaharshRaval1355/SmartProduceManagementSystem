package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RegulatorsFragment extends Fragment {

    private int regulatorCount = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_regulators, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        TextView countTextView = view.findViewById(R.id.regulator_count_text);
        Button addRegulatorButton = view.findViewById(R.id.add_regulator_button);

        // Set up button click listener
        addRegulatorButton.setOnClickListener(v -> {
            regulatorCount++;
            countTextView.setText("Regulators Count: " + regulatorCount);
            Toast.makeText(getContext(), "Regulator Added! Total: " + regulatorCount, Toast.LENGTH_SHORT).show();
        });
}

