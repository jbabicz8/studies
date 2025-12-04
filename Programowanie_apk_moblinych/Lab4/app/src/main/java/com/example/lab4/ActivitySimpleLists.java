package com.example.lab4;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class ActivitySimpleLists extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // powiązanie XML linear do wczytania widoku.
        setContentView(R.layout.activity_simple_lists);

        ListView listView = findViewById(R.id.animalList);

        String[] possibleAnimals = {"Pies", "Kot", "Kangur", "Panda", "Żyrafa", "Królik", "Orzeł", "Kaczka", "Wilk", "Słoń", "Tygrys"};
        String[] animals = new String[200];
        Random random = new Random();
        for (int i = 0; i < animals.length; i++) {
            animals[i] = possibleAnimals[random.nextInt(possibleAnimals.length)];
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, animals);
        listView.setAdapter(adapter);
    }
}
