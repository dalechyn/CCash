package com.example.vladislav.ccash;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vladislav.ccash.DebtCardView.InvestCardAdapter;
import com.example.vladislav.ccash.Frontend.Contact;
import com.example.vladislav.ccash.Frontend.Debtor;
import com.example.vladislav.ccash.Frontend.InvestItem;
import com.example.vladislav.ccash.Frontend.InvestItemKeys;
import com.example.vladislav.ccash.backend.DBFuncs;
import com.example.vladislav.ccash.backend.QRTranslateConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private final static int REQUESTCODE_SCANQR = 1;

    Button btnmanageDebtors, btnscanQR, btncreateNew, btncheckTotal;

    private RecyclerView recyclerView;
    private InvestCardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private DBFuncs dbFuncs;

    private ArrayList<InvestItem> investItems = new ArrayList<>();


    private void initUI()
    {
        btnscanQR = (Button) findViewById(R.id.buttonScanQR);
        btncreateNew = (Button) findViewById(R.id.buttonNewItem);
        btncheckTotal = (Button) findViewById(R.id.buttonCheckTotal);
        btnmanageDebtors = (Button) findViewById(R.id.buttonManageDebtors);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMain);

        dbFuncs = new DBFuncs(this);

        investItems = dbFuncs.DBgetAllInvestItems();

        btncreateNew.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
                View mView = getLayoutInflater().inflate(R.layout.invest_add, null);

                final EditText iName = (EditText) mView.findViewById(R.id.editTextName);
                final EditText iDescription = (EditText) mView.findViewById(R.id.editTextDescription);
                final EditText iSum = (EditText) mView.findViewById(R.id.editTextSum);
                final EditText iMyDebt = (EditText) mView.findViewById(R.id.editTextMyDebt);

                final ImageView imageView = (ImageView) mView.findViewById(R.id.imageViewAddDebtor);

                final LinearLayout linearLayout = (LinearLayout) mView.findViewById(R.id.linearlayoutDebtors);

                final ArrayList<EditText> editTextRefferences = new ArrayList<EditText>();
                final ArrayList<Spinner> spinnerRefferences = new ArrayList<Spinner>();

                final ArrayList<Contact> contacts = dbFuncs.DBgetAllContacts();

                imageView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if(contacts.isEmpty())
                        {
                            Toast.makeText(getApplicationContext(), "You've got no debtors to add", Toast.LENGTH_LONG).show();
                            return;
                        }

                        Context context = MainActivity.this;

                        final LinearLayout newLayout = new LinearLayout(context);

                        newLayout.setOrientation(LinearLayout.HORIZONTAL);
                        newLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                        final Spinner newSpinner = new Spinner(context);

                        final ArrayList<String> addable = new ArrayList<>();

                        for(Contact contact : contacts)
                            addable.add(contact.getName());

                        newSpinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,
                                                                       addable));
                        newSpinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                        spinnerRefferences.add(newSpinner);

                        TextView tv1 = new TextView(context);

                        tv1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        tv1.setTextColor(getResources().getColor(R.color.white));
                        tv1.setText("N:");

                        TextView tv2 = new TextView(context);

                        tv2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        tv2.setTextColor(getResources().getColor(R.color.white));
                        tv2.setText("S:");

                        final EditText editText2 = new EditText(context);
                        editText2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                        editText2.setTextColor(getResources().getColor(R.color.white));

                        editText2.setInputType(InputType.TYPE_CLASS_NUMBER);

                        editTextRefferences.add(editText2);

                        ImageView imageViewDel = new ImageView(context);
                        imageViewDel.setImageResource(android.R.drawable.ic_input_delete);

                        imageViewDel.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                linearLayout.removeView(newLayout);
                                editTextRefferences.remove(editText2);
                            }
                        });

                        newLayout.addView(tv1);
                        newLayout.addView(newSpinner);
                        newLayout.addView(tv2);
                        newLayout.addView(editText2);
                        newLayout.addView(imageViewDel);

                        linearLayout.addView(newLayout);

                    }
                });

                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        boolean editTextRefferencesBad = false;

                        if(!editTextRefferences.isEmpty())
                        {
                            for (EditText editText : editTextRefferences)
                            {
                                if (editText.getText().toString().isEmpty())
                                    editTextRefferencesBad = true;
                            }
                        }

                        if(iName.getText().toString().isEmpty() || iDescription.getText().toString().isEmpty()
                                || iSum.getText().toString().isEmpty() || editTextRefferencesBad)
                        {
                            Toast.makeText(getApplicationContext(), "Something is empty, please fill it up", Toast.LENGTH_LONG).show();
                            return;
                        }

                        ArrayList<Debtor> _debtors = new ArrayList<>();

                        for(int i = 0; i<spinnerRefferences.size(); i++)
                        {

                            String selectedItem = spinnerRefferences.get(i).getSelectedItem().toString();

                            _debtors.add(new Debtor(selectedItem,
                                                    Contact.getUIDbyName(contacts, selectedItem),
                                                    Float.parseFloat(editTextRefferences.get(i).getText().toString())));
                        }

                        InvestItem newItem = new InvestItem(iName.getText().toString(), iDescription.getText().toString(), Integer.parseInt(iSum.getText().toString()),
                                                            Integer.parseInt(iMyDebt.getText().toString()), _debtors);

                        investItems.add(newItem);

                        dbFuncs.DBinsertInvestItem(newItem);

                        mAdapter.notifyItemInserted(investItems.size());
                        dialog.dismiss();
                    }
                });

                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });

                mBuilder.setView(mView);

                AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        btnscanQR.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intentScanQR = new Intent(MainActivity.this, ScanQRActivity.class);
                startActivityForResult(intentScanQR, REQUESTCODE_SCANQR);
            }
        });

        btnmanageDebtors.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent newIntent = new Intent(MainActivity.this, ManageDebtorsActivity.class);
                startActivity(newIntent);
            }
        });

        btncheckTotal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getApplicationContext());
                mBuilder.setTitle("Total checkout");
                StringBuilder stringBuilder = new StringBuilder();

                double totalCheckout = 0.1;

                for(InvestItem investItem : investItems)
                {
                    totalCheckout += investItem.getInvestMyDebts();
                    for(Debtor debtor : investItem.getDebts())
                    {
                        totalCheckout += debtor.getDebtorDebt();
                    }
                }
                mBuilder.setMessage("Your total checkout is " + totalCheckout);
            }
        });


    }

    public void buildRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMain);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new InvestCardAdapter(investItems);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new InvestCardAdapter.OnItemClickListener()
        {
            @Override
            public void onShowInfo(int position)
            {

            }

            @Override
            public void onShareQR(int position)
            {
                JSONObject finalQR = new JSONObject();
                try
                {
                    finalQR.put(QRTranslateConfig.QRAction, QRTranslateConfig.QRAdd);
                    finalQR.put(QRTranslateConfig.QRInvestment, investItems.get(position).toJSON());
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                Intent intentQR = new Intent(MainActivity.this, BuildQRActivity.class);
                intentQR.putExtra(QRTranslateConfig.QRCode, finalQR.toString());

                startActivity(intentQR);
            }
        });



    }

    private void QRHandler(String encodedJSON)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(encodedJSON);
            switch(jsonObject.getString(QRTranslateConfig.QRAction))
            {
                case QRTranslateConfig.QRAdd:

                    JSONObject investJSON = jsonObject.getJSONObject(QRTranslateConfig.QRInvestment);

                    ArrayList<Debtor> debtors = new ArrayList<>();
                    if(investJSON.has(InvestItemKeys.InvestDebtorsKey))
                    {
                        JSONArray jsonDebtors = investJSON.getJSONArray(InvestItemKeys.InvestDebtorsKey);
                        if (jsonDebtors != null)
                        {
                            for (int i = 0; i < jsonDebtors.length() / 3; i++)
                            {
                                debtors.add(new Debtor(
                                        jsonDebtors.getString(i), jsonDebtors.getString(i + 1), (Float) jsonDebtors.get(i + 2)
                                ));
                            }
                        }
                    }
                    InvestItem newInvestItem = new InvestItem(investJSON.getString(InvestItemKeys.InvestNameKey), investJSON.getString(InvestItemKeys.InvestDescriptionKey),
                                                              investJSON.getDouble(InvestItemKeys.InvestSumKey), investJSON.getDouble(InvestItemKeys.InvestMyDebtKey), debtors);

                    Log.d("newItem", newInvestItem.toJSON().toString());
                    dbFuncs.DBinsertInvestItem(newInvestItem);
                    investItems.add(newInvestItem);
                    mAdapter.notifyItemInserted(0);
                    break;
                case QRTranslateConfig.QRAddContact:
                    JSONObject contactJSON = jsonObject.getJSONObject(QRTranslateConfig.QRContact);
                    dbFuncs.DBinsertContacts(new Contact(contactJSON.getString(QRTranslateConfig.QRUserName),
                                                         contactJSON.getString(QRTranslateConfig.QRuid)));
                    break;
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUESTCODE_SCANQR)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                String resultJSON = data.getStringExtra(QRTranslateConfig.QRScanResult);
                Toast.makeText(this, "Everything is fine",
                               Toast.LENGTH_SHORT).show();

                QRHandler(resultJSON);
            }
            else
                if (resultCode == Activity.RESULT_CANCELED)
                {
                    Toast.makeText(this, "Something went wrong",
                                   Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        buildRecyclerView();
    }
}
