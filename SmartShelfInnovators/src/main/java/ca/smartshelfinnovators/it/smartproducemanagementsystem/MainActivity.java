package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

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
import ca.smartshelfinnovators.it.smartproducemanagementsystem.R;
import ca.smartshelfinnovators.it.smartproducemanagementsystem.RegulatorsFragment;
import ca.smartshelfinnovators.it.smartproducemanagementsystem.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
            return true;
        });

        // Set the default fragment to Dashboard
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new DashboardFragment()).commit();
    }


//Winso's code
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_home) {
            Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_settings) {
            openSettings();
            return true;
        } else if (itemId == R.id.action_help) {
            showHelpDialog();
            return true;
        } else if (itemId == R.id.action_profile) {
            requestLocationPermission();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_bar_menu, menu);
        return true;
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

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    private void showHelpDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Help")
                .setMessage("This is the help section.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
