package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ca.smartshelfinnovators.it.smartproducemanagementsystem.DashboardFragment;
import ca.smartshelfinnovators.it.smartproducemanagementsystem.InventoryFragment;
import ca.smartshelfinnovators.it.smartproducemanagementsystem.R;
import ca.smartshelfinnovators.it.smartproducemanagementsystem.RegulatorsFragment;
import ca.smartshelfinnovators.it.smartproducemanagementsystem.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        // Set the default fragment to Dashboard
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new DashboardFragment()).commit();
    }

    // Bottom Navigation Listener using the updated if-else logic
    private final BottomNavigationView.OnItemSelectedListener navListener =
            new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    // Use if-else instead of switch-case to handle item selection
                    int itemId = item.getItemId();
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
}
