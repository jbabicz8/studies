package com.example.lab6;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class MultiChoiceDialogFragment extends DialogFragment {

    private ArrayList<Integer> selectedItems = new ArrayList<>();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final CharSequence[] items = {"Opcja 1", "Opcja 2", "Opcja 3"};
        boolean[] checkedItems = {false, false, false};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Wybierz wiele opcji")
                .setMultiChoiceItems(items, checkedItems, (dialog, which, isChecked) -> {
                    if (isChecked) {
                        selectedItems.add(which);
                    } else if (selectedItems.contains(which)) {
                        selectedItems.remove(Integer.valueOf(which));
                    }
                })
                .setPositiveButton("OK", (dialog, id) -> {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < selectedItems.size(); i++) {
                        sb.append(items[selectedItems.get(i)]);
                        if (i != selectedItems.size() - 1) {
                            sb.append(", ");
                        }
                    }
                    Toast.makeText(getActivity(), "Wybrano: " + sb.toString(), Toast.LENGTH_SHORT).show();
                    Bundle result = new Bundle();
                    result.putString("selectedItems", sb.toString());
                    getParentFragmentManager().setFragmentResult("multiChoiceKey", result);
                })
                .setNegativeButton("Anuluj", (dialog, id) -> {
                });

        return builder.create();
    }
}