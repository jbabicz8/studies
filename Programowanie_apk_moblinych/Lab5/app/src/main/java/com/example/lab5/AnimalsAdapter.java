package com.example.lab5;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AnimalsAdapter extends RecyclerView.Adapter<AnimalsAdapter.AnimalViewHolder> {

    private final List<Animal> animals;

    public AnimalsAdapter(List<Animal> animals) {
        this.animals = animals;
    }

    public static class AnimalViewHolder extends RecyclerView.ViewHolder {
        public TextView animalName;
        public ImageView animalImage;
        public ImageButton animalDeleteButton;

        public AnimalViewHolder(View v) {
            super(v);
            animalName = v.findViewById(R.id.animal_name);
            animalImage = v.findViewById(R.id.animal_image);
            animalDeleteButton = v.findViewById(R.id.delete_animal_button);
        }
    }

    @NonNull
    @Override
    public AnimalsAdapter.AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list_item_recycle_animal, parent, false);
        return new AnimalsAdapter.AnimalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalsAdapter.AnimalViewHolder holder, int position) {
        Animal animal = animals.get(position);
        holder.animalName.setText(animal.name);
        holder.animalImage.setImageResource(animal.image);
        holder.animalName.setBackgroundColor(position % 2 == 0 ? Color.CYAN : Color.GREEN);
        holder.animalDeleteButton.setOnClickListener(view -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                View itemView = holder.itemView;
                // animacja zanikania
                itemView.animate()
                        .alpha(0f)
                        .setDuration(900)
                        .withEndAction(() -> {
                            animals.remove(pos);
                            notifyItemRemoved(pos);
                            Toast.makeText(view.getContext(), "Usunalem - " + animal.name, Toast.LENGTH_SHORT).show();
                        })
                        .start();
            }
        });
        holder.animalName.setOnLongClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                Toast.makeText(v.getContext(), "onLongClick - " + animals.get(pos).name, Toast.LENGTH_SHORT).show();
                Log.d("Info", "setOnLongClickListener: " + animals.get(pos).name + " | pivotX=" + v.getPivotX());
            }
            return true;
        });
        holder.animalImage.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                Toast.makeText(v.getContext(), "Kliknieto obrazek - " + animals.get(pos).name, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return animals.size();
    }
}
