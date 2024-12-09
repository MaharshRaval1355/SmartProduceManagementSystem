package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        auth = FirebaseAuth.getInstance();

        // Find the TextViews in your activity_splash.xml layout
        TextView titleText = findViewById(R.id.title_text);
        TextView subtitleText = findViewById(R.id.subtitle_text);

        // Set colors for the TextViews to match your background
        titleText.setTextColor(getResources().getColor(R.color.CoolSteelBlue));  // Color for title
        subtitleText.setTextColor(getResources().getColor(R.color.CoolSteelBlue)); // Color for subtitle

        // Load the fade-in animation
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom_in);

        // Start the animation on both TextViews
        titleText.startAnimation(fadeInAnimation);
        subtitleText.startAnimation(fadeInAnimation);

        // Delay for 3 seconds, then check the login status
        new Handler().postDelayed(this::checkLoginStatus, 3000); // 3000 milliseconds = 3 seconds
    }

    private void checkLoginStatus() {
        FirebaseUser currentUser = auth.getCurrentUser();

        // Check if the user is logged in and "Remember Me" is checked
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.userprefs), MODE_PRIVATE);
        boolean rememberMeChecked = sharedPreferences.getBoolean(getString(R.string.remember_me), false);

        if (currentUser != null || rememberMeChecked) {
            // User is logged in, navigate directly to MainActivity
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            // User is not logged in, navigate to LoginActivity
            startActivity(new Intent(SplashActivity.this, Login.class));
        }
        finish();
    }
}
