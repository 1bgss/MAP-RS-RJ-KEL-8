package com.example.antrianrumahsakit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Logo
        ImageView logo = findViewById(R.id.logo);
        Animation logoAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        logo.startAnimation(logoAnimation);

        // TextView (invisible dulu di awal)
        TextView appName = findViewById(R.id.app_name);
        appName.setVisibility(View.INVISIBLE);
        Animation textAnimation = AnimationUtils.loadAnimation(this, R.anim.text_animation);

        // Delay sedikit supaya text muncul setelah logo
        appName.postDelayed(() -> {
            appName.setVisibility(View.VISIBLE);
            appName.startAnimation(textAnimation);
        }, 500); // delay 0,5 detik

        // Delay 2 detik sebelum pindah ke MainActivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish(); // biar splash tidak bisa di-back
        }, 2000);
    }
}
