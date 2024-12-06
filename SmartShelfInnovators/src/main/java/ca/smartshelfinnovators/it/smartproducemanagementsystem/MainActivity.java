package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

        // If you're using a Toolbar instead of the default ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar); // Make sure toolbar is in your layout
        setSupportActionBar(toolbar); // Set it as the ActionBar

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        if (savedInstanceState != null) {
            // Restore the last state for checked position
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the app bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu); // Replace 'menu_main' with your actual menu XML file name
        return true;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        // Handle the Search item click
        if (itemId == R.id.action_profile) {
            // You can implement the search functionality here
            // For example, show a Toast or open a Search activity or fragment
            Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        // Handle the About item click
        else if (itemId == R.id.action_about) {
            // Open an About dialog or navigate to an About page
            Toast.makeText(this, "About clicked", Toast.LENGTH_SHORT).show();
            // For example, you can show an AlertDialog with information about the app
            showAboutDialog();
            return true;
        }

        // Handle the Help item click
        else if (itemId == R.id.action_help) {
            // Open a Help section or show help information
            Toast.makeText(this, "Help clicked", Toast.LENGTH_SHORT).show();
            // You can show an AlertDialog or navigate to a Help fragment/activity
            showHelpDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Function to show an About dialog
    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("About")
                .setMessage("This app is developed by Smart Shelf Innovators.\nVersion 1.0")
                .setPositiveButton("OK", null)
                .show();
    }

    // Function to show a Help dialog
    private void showHelpDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Help")
                .setMessage("To use this app, you can access different features from the menu.")
                .setPositiveButton("OK", null)
                .show();
    }


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
