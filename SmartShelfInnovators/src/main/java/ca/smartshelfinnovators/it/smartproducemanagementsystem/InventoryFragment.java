package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InventoryFragment extends Fragment {

    private TextView neopixelStatusText, conveyorStatusText, joystickStatusText, lastUpdatedText;
    private TextView bin1Count, bin2Count, bin3Count, bin4Count;
    private ProgressBar bin1Progress, bin2Progress, bin3Progress, bin4Progress;
    private Button emergencyStopBtn, refreshBtn;
    private DatabaseReference dbRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        dbRef = FirebaseDatabase.getInstance().getReference();

        neopixelStatusText = view.findViewById(R.id.neopixel_status);
        conveyorStatusText = view.findViewById(R.id.conveyor_status);
        joystickStatusText = view.findViewById(R.id.joystick_status);
        lastUpdatedText = view.findViewById(R.id.last_updated);

        bin1Count = view.findViewById(R.id.bin1_count);
        bin2Count = view.findViewById(R.id.bin2_count);
        bin3Count = view.findViewById(R.id.bin3_count);
        bin4Count = view.findViewById(R.id.bin4_count);

        bin1Progress = view.findViewById(R.id.bin1_progress);
        bin2Progress = view.findViewById(R.id.bin2_progress);
        bin3Progress = view.findViewById(R.id.bin3_progress);
        bin4Progress = view.findViewById(R.id.bin4_progress);

        emergencyStopBtn = view.findViewById(R.id.emergency_stop_btn);
        refreshBtn = view.findViewById(R.id.refresh_btn);

        emergencyStopBtn.setOnClickListener(v -> {
            dbRef.child("status").child("neopixel").setValue("Red");
            Toast.makeText(getContext(), "Emergency stop triggered!", Toast.LENGTH_SHORT).show();
        });

        refreshBtn.setOnClickListener(v -> fetchData());

        fetchData();

        return view;
    }

    private void fetchData() {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String neopixel = snapshot.child("status/neopixel").getValue(String.class);
                String joystick = snapshot.child("status/joystick").getValue(String.class);
                Long bin1 = snapshot.child("bins/bin1/quantity").getValue(Long.class);
                Long bin4 = snapshot.child("bins/bin4/quantity").getValue(Long.class);

                neopixelStatusText.setText("NeoPixel: " + neopixel);
                conveyorStatusText.setText("Conveyor Belt: " + ("green".equals(neopixel) ? "Running Fine" : "Issue Detected"));
                joystickStatusText.setText("Joystick: " + (joystick != null ? joystick : "Unknown"));
                lastUpdatedText.setText("Last Updated: " + new SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(new Date()));

                bin1Count.setText("Bin 1 (Bakery): " + (bin1 != null ? bin1 : 0) + " items");
                bin1Progress.setProgress(bin1 != null ? Math.min((int)(bin1 * 10), 100) : 0);

                bin2Count.setText("Bin 2 (Household): N/A");
                bin2Progress.setProgress(0);

                bin3Count.setText("Bin 3 (Seasonal): 0 items");
                bin3Progress.setProgress(0);

                bin4Count.setText("Bin 4 (Face & Body): " + (bin4 != null ? bin4 : 0) + " items");
                bin4Progress.setProgress(bin4 != null ? Math.min((int)(bin4 * 10), 100) : 0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
