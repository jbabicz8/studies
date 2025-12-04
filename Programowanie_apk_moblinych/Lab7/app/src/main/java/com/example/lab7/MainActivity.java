package com.example.lab7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private ListView sensorsListView;
    private Button sendSmsButton;
    private String smsContent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorsListView = findViewById(R.id.sensorsListView);
        sendSmsButton = findViewById(R.id.sendSmsButton);

        // pobieranie listy wszystkich sensorów
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        // tworzenie treści SMS-a i wyświetlanie listy
        StringBuilder sb = new StringBuilder("Lista sensorów urządzenia:\n");
        String[] sensorNames = new String[sensorList.size()];
        for (int i = 0; i < sensorList.size(); i++) {
            String name = sensorList.get(i).getName();
            sensorNames[i] = name;
            sb.append(i + 1).append(". ").append(name).append("\n");
        }
        smsContent = sb.toString();

        // wyświetlanie listy w kontrolce ListView
        SensorAdapter adapter = new SensorAdapter(this, sensorList);
        sensorsListView.setAdapter(adapter);

        // obsługa przycisku "Wyślij listę w SMS"
        sendSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSensorListViaSms(smsContent);
            }
        });
    }

    private void sendSensorListViaSms(String content) {
        // protokół smsto: otworzy aplikację SMS
        Uri smsUri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsUri);

        // treść SMSa
        intent.putExtra("sms_body", content);

        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            // wyjątek łapiący brak obsługi aplikacji sms w telefonie
            Toast.makeText(MainActivity.this, "Brak aplikacji do obsługi SMS", Toast.LENGTH_SHORT).show();
        }
    }

    private class SensorAdapter extends ArrayAdapter<Sensor> {
        private final Context context;
        private final List<Sensor> sensors;

        public SensorAdapter(Context context, List<Sensor> sensors) {
            super(context, R.layout.sensor_list_item, sensors);
            this.context = context;
            this.sensors = sensors;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.sensor_list_item, parent, false);

            TextView sensorName = rowView.findViewById(R.id.sensorNameTextView);
            ImageView sensorIcon = rowView.findViewById(R.id.sensorIcon);

            Sensor sensor = sensors.get(position);

            // Ustawienie nazwy sensora
            sensorName.setText(sensor.getName());
            // Ustawienie ikony
            sensorIcon.setImageResource(R.drawable.ic_sensor);

            return rowView;
        }
    }
}