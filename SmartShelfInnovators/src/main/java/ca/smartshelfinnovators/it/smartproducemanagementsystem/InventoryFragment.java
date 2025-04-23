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
    private DatabaseReference rootRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        rootRef = FirebaseDatabase.getInstance().getReference();

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
            rootRef.child("status").child("neopixel").setValue("red");
            Toast.makeText(getContext(), "Emergency stop triggered!", Toast.LENGTH_SHORT).show();
        });

        refreshBtn.setOnClickListener(v -> fetchFirebaseData());

        setupRealtimeListener();

        return view;
    }

    private void setupRealtimeListener() {
        DatabaseReference liveDataRef = rootRef.child("live_data");

        liveDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String item = snapshot.child("item").getValue(String.class);
                String lightStatus = snapshot.child("light_status").getValue(String.class);
                String joystickBtn = snapshot.child("joystick/button").getValue(String.class);
                Long timestamp = snapshot.child("timestamp").getValue(Long.class);

                if (item == null) item = "--";
                if (lightStatus == null) lightStatus = "--";
                if (joystickBtn == null) joystickBtn = "--";

                neopixelStatusText.setText("NeoPixel: " + item);
                conveyorStatusText.setText("Conveyor Belt: " + ("red".equalsIgnoreCase(item) ? "Issue Detected" : "Running Fine"));
                joystickStatusText.setText("Joystick: " + joystickBtn);

                if (timestamp != null) {
                    lastUpdatedText.setText("Last Updated: " +
                            new SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(new Date(timestamp * 1000)));
                }

                fetchBinData(); // Nested call to fetch bins safely
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load live data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchBinData() {
        rootRef.child("bins").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot binSnapshot) {
                Long bin1 = binSnapshot.child("bin1/quantity").getValue(Long.class);
                Long bin2 = binSnapshot.child("bin2/quantity").getValue(Long.class);
                Long bin3 = binSnapshot.child("bin3/quantity").getValue(Long.class);
                Long bin4 = binSnapshot.child("bin4/quantity").getValue(Long.class);

                if (bin1 == null) bin1 = 0L;
                if (bin2 == null) bin2 = 0L;
                if (bin3 == null) bin3 = 0L;
                if (bin4 == null) bin4 = 0L;


                bin1Count.setText("Bin 1 (Bakery): " + bin1 + " items");
                bin1Progress.setProgress(Math.min((int) (bin1 * 10), 100));

                bin2Count.setText("Bin 2 (Seasonal): " + bin2 + "items");
                bin2Progress.setProgress(Math.min((int) (bin2 * 10), 100));

                bin3Count.setText("Bin 3 (Face & Body): " + bin3 + "items");
                bin3Progress.setProgress(Math.min((int) (bin3 * 10), 100));

                bin4Count.setText("Bin 4 (Produce): " + bin4 + " items");
                bin4Progress.setProgress(Math.min((int) (bin4 * 10), 100));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load bin data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchFirebaseData() {
        // Manual refresh fallback
        setupRealtimeListener();
    }
}
