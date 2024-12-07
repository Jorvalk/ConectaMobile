package com.example.conectamobile;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
public class PantallaCarga extends AppCompatActivity {
    private static final int SPLASH_DURATION = 2000; // Duración en milisegundos (2 segundos)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_carga);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Intent para iniciar la siguiente actividad después de la pantalla de carga
                Intent intent = new Intent(PantallaCarga.this, Login.class);
                startActivity(intent);
                finish(); // Cierra la actividad de la pantalla de carga para que no aparezca al presionar "Atrás"
            }
        }, SPLASH_DURATION);
    }
}
