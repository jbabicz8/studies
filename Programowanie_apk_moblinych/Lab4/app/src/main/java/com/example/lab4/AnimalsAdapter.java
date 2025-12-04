package com.example.lab4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AnimalsAdapter extends RecyclerView.Adapter<AnimalsAdapter.AnimalViewHolder> {

    private String[] animals;

    public AnimalsAdapter(String[] animals) { this.animals = animals;}

    public static class AnimalViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public AnimalViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.textView);
        }
    }

    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new AnimalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {
        String animal = animals[position];
        holder.textView.setText(animal + " " + (position + 1));
    }

    @Override
    public int getItemCount() { return animals.length; }
}
