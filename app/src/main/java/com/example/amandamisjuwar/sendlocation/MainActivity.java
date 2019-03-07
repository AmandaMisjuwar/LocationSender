package com.example.amandamisjuwar.sendlocation;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.app.PendingIntent;
import android.support.v4.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    EditText emergencyNumber, emergencyName;
    Button butSaveInfo, butSendSms;
    TextView emergencyInfo;
    String phoneNumber = "", message = "Amanda is testing her app :)", contactName = "";
    double latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        emergencyName = (EditText) findViewById(R.id.emergencyName);
        emergencyNumber = (EditText) findViewById(R.id.emergencyNumber);
        butSaveInfo = (Button) findViewById(R.id.butSaveInfo);
        butSendSms = (Button) findViewById(R.id.butSendSms);
        emergencyInfo = (TextView) findViewById(R.id.emergencyInfo);

        // updating contact information after button click
        butSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateContactInfo();
            }
        });
        // sending the sms after button click
        butSendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSms(view);
            }
        });

    }


    public void sendSms(View view) {
        // retrieve location coordinates
        retrieveLocation();
        message = "Amanda is currently located here: " +
                "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude +
                ". Please open it in Google Maps or your browser.";
        // Using SmsManager
        SmsManager smsManager = SmsManager.getDefault();
        String scAddress = null;
        PendingIntent sentIntent = null;
        PendingIntent deliveryIntent = null;
        smsManager.sendTextMessage
                (phoneNumber, scAddress, message,
                        sentIntent, deliveryIntent);
    }


    private void checkPermissions() {
        // checking for sending sms permission
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        //checking for location information permission
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        }
    }


    private void updateContactInfo() {
        phoneNumber = emergencyNumber.getText().toString();
        contactName = emergencyName.getText().toString();
        emergencyInfo.setText(contactName + ": " + phoneNumber);
    }


    private void retrieveLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Permission denied, turn on location", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
