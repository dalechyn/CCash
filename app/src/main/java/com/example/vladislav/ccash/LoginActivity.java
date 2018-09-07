package com.example.vladislav.ccash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.vladislav.ccash.backend.SharedPrefKeys;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;
import java.util.zip.CRC32;

import static com.example.vladislav.ccash.backend.SharedPrefKeys.THIS_UID;
import static com.example.vladislav.ccash.backend.SharedPrefKeys.THIS_USERNAME;

public class LoginActivity extends AppCompatActivity
{
    EditText editText;
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(getSharedPreferences(SharedPrefKeys.MY_PREF_KEY, MODE_PRIVATE).getString(SharedPrefKeys.THIS_USERNAME, null) != null)
        {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);
        InitView();
    }

    private void InitView()
    {
        editText = (EditText) findViewById(R.id.editTextSetName);
        button = (Button) findViewById(R.id.buttonStart);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!editText.getText().toString().isEmpty())
                {
                    SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences(SharedPrefKeys.MY_PREF_KEY, MODE_PRIVATE).edit();
                    sharedPreferencesEditor.putString(THIS_USERNAME, editText.getText().toString());
                    sharedPreferencesEditor.putString(THIS_UID, getUniqueId());
                    sharedPreferencesEditor.apply();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }

    private String getUniqueId() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        return wifiInfo.getMacAddress();
    }
}
