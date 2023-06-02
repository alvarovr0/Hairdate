package com.example.hairdate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000; // Duración en milisegundos del Splash Screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Retrasar la transición a la siguiente actividad usando un Handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Aquí puedes iniciar la siguiente actividad, por ejemplo:
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DURATION);
    }
}