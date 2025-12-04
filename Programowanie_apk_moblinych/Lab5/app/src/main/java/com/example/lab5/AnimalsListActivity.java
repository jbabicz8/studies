package com.example.lab5;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnimalsListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AnimalsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);
        recyclerView = findViewById(R.id.recycler_view);

        Animal[] possibleAnimals = {
                new Animal("Pies", R.drawable.dog_image),
                new Animal("Kot", R.drawable.cat_image),
                new Animal("Lama", R.drawable.lama_image),
                new Animal("Borsuk", R.drawable.borsuk_image),
                new Animal("Koń", R.drawable.kon_image),
                new Animal("Słoń", R.drawable.slon_image),
                new Animal("Leniwiec", R.drawable.leniwiec_image),
                new Animal("Hiena", R.drawable.hiena_image),
                new Animal("Lemur", R.drawable.lemur_image),
                new Animal("Jeleń", R.drawable.jelen_image)
        };

        List<Animal> animals = new ArrayList<>(100);
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            Animal randomAnimal = possibleAnimals[random.nextInt(possibleAnimals.length)];
            animals.add(new Animal(randomAnimal.name + " " + (i + 1), randomAnimal.image));
        }

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AnimalsAdapter(animals);
        recyclerView.setAdapter(mAdapter);
    }
}
