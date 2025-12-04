package com.example.lab3;

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

        // wyjecie przyciskow z xmla po id
        Button btnLinear = findViewById(R.id.btnLinear);
        Button btnRelative = findViewById(R.id.btnRelative);
        Button btnTable = findViewById(R.id.btnTable);

        // ustawienie akcji po kliknieciu w przyciski
        btnLinear.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LinearLayoutActivity.class);
            startActivity(intent);
        });

        btnRelative.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RelativeLayoutActivity.class);
            startActivity(intent);
        });

        btnTable.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TableLayoutActivity.class);
            startActivity(intent);
        });
    }
}