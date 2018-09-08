package com.example.vladislav.ccash;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.vladislav.ccash.backend.MapFuncs;
import com.example.vladislav.ccash.backend.QRTranslateConfig;
import com.example.vladislav.ccash.backend.SharedPrefKeys;
import com.example.vladislav.ccash.backend.Tuple;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ManageDebtorsActivity extends AppCompatActivity
{
    Button btnShareName;
    HashMap<String, Tuple<String, Integer>> debtorsMap;
    ArrayList<String> debtors;
    SharedPreferences sharedPreferences;

    ListView listViewDebtors;

    @Override
    public void onBackPressed()
    {
        MapFuncs.saveMap(this, SharedPrefKeys.MY_PREF_KEY, SharedPrefKeys.THIS_CONTACTS, debtorsMap);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.managedebtors_activity);
        initView();
        initHashmap();
        initListView();
    }

    private void initHashmap()
    {
        debtorsMap = new HashMap<>(MapFuncs.loadMap(this, SharedPrefKeys.MY_PREF_KEY, SharedPrefKeys.THIS_CONTACTS));
        debtors = new ArrayList<>();

        //Init debtors

        debtors = new ArrayList<>(debtorsMap.keySet());
    }

    private void initView()
    {
        sharedPreferences = getSharedPreferences(SharedPrefKeys.MY_PREF_KEY, MODE_PRIVATE);

        btnShareName = (Button) findViewById(R.id.buttonShareMyName);
        listViewDebtors = (ListView) findViewById(R.id.listViewDebtors);

        btnShareName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                JSONObject sharable = new JSONObject();
                String somename = "vlad"; //need to add sharedprefferences to get name
                try
                {
                    sharable.put(QRTranslateConfig.QRuid, getUniqueId());

                    sharable.put(QRTranslateConfig.QRUserName, somename);

                    JSONObject resultJSON = new JSONObject();

                    resultJSON.put(QRTranslateConfig.QRAction, QRTranslateConfig.QRAddContact);
                    resultJSON.put(QRTranslateConfig.QRContact, sharable);

                    Intent intentQR = new Intent(ManageDebtorsActivity.this, BuildQRActivity.class);
                    intentQR.putExtra(QRTranslateConfig.QRCode, resultJSON.toString());

                    startActivity(intentQR);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private String getUniqueId() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        return wifiInfo.getMacAddress();
    }

    private void initListView()
    {
        debtors = new ArrayList<>(debtorsMap.keySet());
        final ArrayAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, debtors);
        listViewDebtors.setAdapter(listAdapter);

        listViewDebtors.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l)
            {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ManageDebtorsActivity.this);
                mBuilder.setTitle("Select Action");
                mBuilder.setMessage("Please select action:");

                mBuilder.setPositiveButton("Remove", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        debtorsMap.remove(debtors.get(position));
                        debtors.remove(position);
                        listAdapter.notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton("Rename", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.dismiss();
                        AlertDialog.Builder renameBuilder = new AlertDialog.Builder(ManageDebtorsActivity.this);
                        renameBuilder.setTitle("Renaming "+ debtors.get(position));

                        final EditText renameName = new EditText(getBaseContext());
                        renameName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        renameName.setTextColor(getResources().getColor(R.color.black));
                        renameName.setText(debtors.get(position));
                        renameName.setSelection(renameName.getText().length());

                        renameBuilder.setView(renameName);

                        renameBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                Tuple savedUserInfo = debtorsMap.get(debtors.get(position));
                                debtorsMap.remove(debtors.get(position));
                                debtorsMap.put(renameName.getText().toString(), savedUserInfo);
                                debtors.set(position, renameName.getText().toString());
                                dialogInterface.dismiss();
                            }
                        });

                        renameBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.dismiss();
                            }
                        });

                        renameBuilder.create().show();
                    }
                });

                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });
    }


}
