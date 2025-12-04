package com.example.lab5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

        String[] possibleAnimals = {"Pies", "Kot", "Kangur", "Panda", "Żyrafa", "Królik", "Orzeł", "Kaczka", "Wilk", "Słoń", "Tygrys", "Wirus", "Donald"};
        String[] animals = new String[50];
        Random random = new Random();
        for (int i = 0; i < animals.length; i++) {
            animals[i] = possibleAnimals[random.nextInt(possibleAnimals.length)] + " " + (i + 1);
        }

        HarderListAdapter adapter = new HarderListAdapter(animals);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
    }

    public static class HarderListAdapter extends RecyclerView.Adapter<HarderListAdapter.ViewHolder> {

        private final String[] localDataSet;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView textView;

            public ViewHolder(View view) {
                super(view);
                textView = (TextView) view.findViewById(android.R.id.text1);
            }

            public TextView getTextView() {
                return textView;
            }
        }

        public HarderListAdapter(String[] dataSet) {
            localDataSet = dataSet;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(android.R.layout.simple_list_item_1, viewGroup, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            viewHolder.getTextView().setText(localDataSet[position]);
        }

        @Override
        public int getItemCount() {
            return localDataSet.length;
        }
    }
}
