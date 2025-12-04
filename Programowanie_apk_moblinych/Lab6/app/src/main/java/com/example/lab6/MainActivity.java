package com.example.lab6;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView infoDialog;

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

        infoDialog = findViewById(R.id.infoDialog);

        getSupportFragmentManager().setFragmentResultListener("singleChoiceKey", this, (requestKey, bundle) -> {
            String result = bundle.getString("selectedItem");
            infoDialog.setText("Wybrana opcja: " + result);
        });

        getSupportFragmentManager().setFragmentResultListener("multiChoiceKey", this, (requestKey, bundle) -> {
            String result = bundle.getString("selectedItems");
            infoDialog.setText("Wybrane opcje: " + result);
        });

        getSupportFragmentManager().setFragmentResultListener("timePickerKey", this, (requestKey, bundle) -> {
            String result = bundle.getString("selectedTime");
            infoDialog.setText("Wybrana godzina: " + result);
        });

        getSupportFragmentManager().setFragmentResultListener("datePickerKey", this, (requestKey, bundle) -> {
            String result = bundle.getString("selectedDate");
            infoDialog.setText("Wybrana data: " + result);
        });

        getSupportFragmentManager().setFragmentResultListener("customDialogKey", this, (requestKey, bundle) -> {
            String result = bundle.getString("inputText");
            infoDialog.setText("Wpisano: " + result);
        });

        // przyciski
        Button btnSingle = findViewById(R.id.btnSingle);
        Button btnMultiple = findViewById(R.id.btnMultiple);
        Button btnHour = findViewById(R.id.btnHour);
        Button btnDate = findViewById(R.id.btnDate);
        Button btnCustom = findViewById(R.id.btnCustom);

        btnSingle.setOnClickListener(v -> {
            new SingleChoiceDialogFragment().show(getSupportFragmentManager(), "SingleChoiceDialogFragment");
        });

        btnMultiple.setOnClickListener(v -> {
            new MultiChoiceDialogFragment().show(getSupportFragmentManager(), "MultiChoiceDialogFragment");
        });

        btnHour.setOnClickListener(v -> {
            new TimePickerFragment().show(getSupportFragmentManager(), "TimePickerFragment");
        });

        btnDate.setOnClickListener(v -> {
            new DatePickerFragment().show(getSupportFragmentManager(), "DatePickerFragment");
        });

        btnCustom.setOnClickListener(v -> {
            new CustomDialogFragment().show(getSupportFragmentManager(), "CustomDialogFragment");
        });
    }
}