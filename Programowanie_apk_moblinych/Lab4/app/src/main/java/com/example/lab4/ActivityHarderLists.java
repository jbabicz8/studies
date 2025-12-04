package com.example.lab4;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Random;

public class ActivityHarderLists extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harder_lists);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        String[] possibleAnimals = {"Pies", "Kot", "Kangur", "Panda", "Żyrafa", "Królik", "Orzeł", "Kaczka", "Wilk", "Słoń", "Tygrys"};
        String[] animals = new String[50];
        Random random = new Random();
        for (int i = 0; i < animals.length; i++) {
            animals[i] = possibleAnimals[random.nextInt(possibleAnimals.length)];
        }

        AnimalsAdapter adapter = new AnimalsAdapter(animals);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }
}
