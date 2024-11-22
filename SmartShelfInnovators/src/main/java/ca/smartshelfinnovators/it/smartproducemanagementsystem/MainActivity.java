package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;


import ca.smartshelfinnovators.it.smartproducemanagementsystem.DashboardFragment;
import ca.smartshelfinnovators.it.smartproducemanagementsystem.InventoryFragment;
import ca.smartshelfinnovators.it.smartproducemanagementsystem.RegulatorsFragment;
import ca.smartshelfinnovators.it.smartproducemanagementsystem.SettingsFragment;
import ca.smartshelfinnovators.it.smartproducemanagementsystem.R;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    // Fragments array
    private final Fragment[] fragments = {
            new DashboardFragment(),
            new RegulatorsFragment(),
            new InventoryFragment(),
            new SettingsFragment()
    };

    private static final String CURRENT_FRAGMENT = "current_fragment";
    private int selectedItemId = R.id.menu_dashboard; // default start with dashboard


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupBottomNavigation();

        // Set default fragment to DashboardFragment if it's the first time loading the activity
        if (savedInstanceState == null) {
            loadFragment(fragments[0]); // DashboardFragment is at index 0
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // Using if-else instead of switch-case to determine the selected fragment
            if (item.getItemId() == R.id.menu_dashboard) {
                selectedFragment = fragments[0]; // DashboardFragment
            } else if (item.getItemId() == R.id.menu_regulators) {
                selectedFragment = fragments[1]; // RegulatorsFragment
            } else if (item.getItemId() == R.id.menu_inventory) {
                selectedFragment = fragments[2]; // InventoryFragment
            } else if (item.getItemId() == R.id.menu_settings) {
                selectedFragment = fragments[3]; // SettingsFragment
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        // Ensure that the fragment is not being reloaded if it's already added
        if (getSupportFragmentManager().findFragmentByTag(fragment.getClass().getSimpleName()) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_home) {
            Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.action_settings) {
            openSettings();
            return true;
        } else if (item.getItemId() == R.id.action_help) {
            showHelpDialog();
            return true;
        } else if (item.getItemId() == R.id.action_profile) {
            requestLocationPermission();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            showSnackbar("Location permission already granted");
        }
    }

    private void openSettings() {
        Toast.makeText(this, "Settings opened", Toast.LENGTH_SHORT).show();
    }

    private void showHelpDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Help")
                .setMessage("This is the help section.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showSnackbar("Location permission granted");
            } else {
                showSnackbar("Location permission denied");
            }
        }
    }

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
