package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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

        // Delay for 3 seconds, then transition to SignUp activity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, SignUp.class);
            startActivity(intent);
            finish(); // Finish splash activity so it doesn't stay in the backstack
        }, 3000); // 3000 milliseconds = 3 seconds
    }
}
