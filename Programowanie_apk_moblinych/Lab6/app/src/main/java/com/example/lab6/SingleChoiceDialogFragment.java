package com.example.lab6;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class SingleChoiceDialogFragment extends DialogFragment {

    private int selectedItem = -1;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final CharSequence[] items = {"Opcja 1", "Opcja 2", "Opcja 3"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Wybierz jedną opcję")
                .setSingleChoiceItems(items, -1, (dialog, which) -> {
                    selectedItem = which;
                })
                .setPositiveButton("OK", (dialog, id) -> {
                    if (selectedItem != -1) {
                        Toast.makeText(getActivity(), "Wybrano: " + items[selectedItem], Toast.LENGTH_SHORT).show();
                        Bundle result = new Bundle();
                        result.putString("selectedItem", items[selectedItem].toString());
                        getParentFragmentManager().setFragmentResult("singleChoiceKey", result);
                    }
                })
                .setNegativeButton("Anuluj", (dialog, id) -> {
                });

        return builder.create();
    }
}