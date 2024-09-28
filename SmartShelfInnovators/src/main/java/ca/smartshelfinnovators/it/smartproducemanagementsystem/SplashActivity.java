package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Find the ImageView for the logo in your activity_splash.xml layout
        ImageView splash_logo = findViewById(R.id.splash_logo);

        // Load the zoom-out animation
        Animation zoomOutAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom_in);

        // Start the animation on ImageView
        splash_logo.startAnimation(zoomOutAnimation);

        // Delay for 3 seconds, then transition to MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finish splash activity so it doesn't stay in the backstack
            }
        }, 3000); // 3000 milliseconds = 3 seconds
    }
}