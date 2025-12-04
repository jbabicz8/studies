package com.example.lab4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // przypisanie przyciskow
        Button btnList = findViewById(R.id.btnList);
        Button btnListGrid = findViewById(R.id.btnListGrid);

        // ustawienie akcji po kliknieciu w przyciski
        btnList.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ActivitySimpleLists.class);
            startActivity(intent);
        });

        btnListGrid.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ActivityHarderLists.class);
            startActivity(intent);
        });
    }
}