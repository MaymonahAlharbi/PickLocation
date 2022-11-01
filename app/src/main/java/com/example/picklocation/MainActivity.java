package com.example.picklocation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


public class MainActivity extends AppCompatActivity {


private static final int ERROR_DIALOG_REQUEST = 9001;
Button map_button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isGoogleServicesCompatible()){
            map_button = (Button) findViewById(R.id.mapButton);
            map_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, MapActivity.class);
                    startActivity(intent);
                }
            });
        }
    }


    // check the compatibility of device's google services
    public boolean isGoogleServicesCompatible(){

        int avialble = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (avialble == ConnectionResult.SUCCESS){
            //The google Play Services are Compatible
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(avialble)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, avialble, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(this, "We Can't View Your Location", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


}