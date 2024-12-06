package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

import ca.smartshelfinnovators.it.smartproducemanagementsystem.DashboardFragment;
import ca.smartshelfinnovators.it.smartproducemanagementsystem.InventoryFragment;
import ca.smartshelfinnovators.it.smartproducemanagementsystem.R;
import ca.smartshelfinnovators.it.smartproducemanagementsystem.RegulatorsFragment;
import ca.smartshelfinnovators.it.smartproducemanagementsystem.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private static final String CURRENT_FRAGMENT = "current_fragment";
    private int selectedItemId = R.id.menu_dashboard; // default start with dashboard

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        if (savedInstanceState != null) {
            // Restore the last state for checked position.
            selectedItemId = savedInstanceState.getInt(CURRENT_FRAGMENT, R.id.menu_dashboard);
        }

        bottomNav.setSelectedItemId(selectedItemId); // Set the saved item as selected

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_FRAGMENT, selectedItemId);
    }

    // Bottom Navigation Listener using the updated if-else logic
    private final BottomNavigationView.OnItemSelectedListener navListener =
            new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    // Use if-else instead of switch-case to handle item selection
                    int itemId = item.getItemId();
                    selectedItemId = itemId; // Update the last selected item id

                    if (itemId == R.id.menu_dashboard) {
                        selectedFragment = new DashboardFragment();
                    } else if (itemId == R.id.menu_regulators) {
                        selectedFragment = new RegulatorsFragment();
                    } else if (itemId == R.id.menu_inventory) {
                        selectedFragment = new InventoryFragment();
                    } else if (itemId == R.id.menu_settings) {
                        selectedFragment = new SettingsFragment();
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();
                    }

                    return true;
                }
            };

    public void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit?")
                .setIcon(R.drawable.warning) // Optional: Add an alert icon
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Exit the app
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Dismiss the dialog
                    dialog.dismiss();
                })
                .create()
                .show();
    }

}
