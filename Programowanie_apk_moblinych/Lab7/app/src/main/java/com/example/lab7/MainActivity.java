package com.example.lab7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper; // Dodany import
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback; // Dodany import
import com.google.android.gms.location.LocationRequest; // Dodany import
import com.google.android.gms.location.LocationResult; // Dodany import
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority; // Dodany import
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private ListView sensorsListView;
    private Button sendSmsButton;
    private String smsContent = "";
    private FusedLocationProviderClient fusedLocationClient;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location lastKnownLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorsListView = findViewById(R.id.sensorsListView);
        sendSmsButton = findViewById(R.id.sendSmsButton);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMinUpdateIntervalMillis(5000)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult.getLastLocation() != null) {
                    lastKnownLocation = locationResult.getLastLocation();
                    stopLocationUpdates();
                    sendSmsWithLocation(lastKnownLocation);
                }
            }
        };

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        StringBuilder sb = new StringBuilder("Lista sensorów urządzenia:\n");
        for (int i = 0; i < sensorList.size(); i++) {
            String name = sensorList.get(i).getName();
            sb.append(i + 1).append(". ").append(name).append("\n");
        }
        smsContent = sb.toString();

        SensorAdapter adapter = new SensorAdapter(this, sensorList);
        sensorsListView.setAdapter(adapter);

        if (!checkPermissions()) {
            requestPermissions();
        }

        sendSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) {
                    getLocationAndSendSms();
                } else {
                    Toast.makeText(MainActivity.this, "Proszę nadać uprawnienia do SMS i lokalizacji.", Toast.LENGTH_LONG).show();
                    requestPermissions();
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
            Toast.makeText(this, "Trwa próba aktywnego pobierania lokalizacji...", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void sendSmsWithLocation(Location location) {
        String fullSmsBody = smsContent;
        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            String mapLink = "https://maps.google.com/?q=" + lat + "," + lng;

            fullSmsBody += "\n\nMoja lokalizacja:\n";
            fullSmsBody += "Lat: " + lat + "\n";
            fullSmsBody += "Lng: " + lng + "\n";
            fullSmsBody += "Link: " + mapLink;
        } else {
            fullSmsBody += "\n\nNie udało się pobrać aktualnej lokalizacji GPS.";
        }
        sendSms(fullSmsBody);
    }

    private void getLocationAndSendSms() {
        if (!checkPermissions()) {
            requestPermissions();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                sendSmsWithLocation(location);
                            } else {
                                // lokalizacja nieznaleziona (getLastLocation() zwróciło null), wymuszamy aktywną aktualizację
                                Toast.makeText(MainActivity.this, "Lokalizacja nieznana, wymuszam aktualizację...", Toast.LENGTH_SHORT).show();
                                startLocationUpdates();
                            }
                        }
                    })
                    .addOnFailureListener(this, e -> {
                        Log.e("MainActivity", "Błąd pobierania ostatniej lokalizacji: " + e.getMessage());
                        // W przypadku błędu pasywnego pobierania, wymuszamy aktualizację
                        startLocationUpdates();
                    });
        }
    }

    private void sendSms(String content) {
        Uri smsUri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsUri);
        intent.putExtra("sms_body", content);

        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "Brak aplikacji do obsługi SMS", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean checkPermissions() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (!allGranted) {
                Toast.makeText(this, "Wymagane uprawnienia zostały odrzucone. Aplikacja zostanie zamknięta.", Toast.LENGTH_LONG).show();
                finish();
            }
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

            sensorName.setText(sensor.getName());
            sensorIcon.setImageResource(R.drawable.ic_sensor);

            return rowView;
        }
    }
}