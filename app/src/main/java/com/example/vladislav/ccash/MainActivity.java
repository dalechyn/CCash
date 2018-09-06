package com.example.vladislav.ccash;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.vladislav.ccash.DebtCardView.InvestCardAdapter;
import com.example.vladislav.ccash.Frontend.InvestItem;
import com.example.vladislav.ccash.backend.QRTranslateConfig;
import com.example.vladislav.ccash.backend.Tuple;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private final static int REQUESTCODE_SCANQR = 1;

    Button btnmanageDebtors, btncanQR, btncreateNew, btncheckTotal;

    private RecyclerView recyclerView;
    private InvestCardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<InvestItem> investItems = new ArrayList<>();

    private void initUI()
    {
        btncanQR = (Button) findViewById(R.id.buttonScanQR);
        btncreateNew = (Button) findViewById(R.id.buttonNewItem);
        btncheckTotal = (Button) findViewById(R.id.buttonCheckTotal);
        btnmanageDebtors = (Button) findViewById(R.id.buttonManageDebtors);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMain);
    }

    private void InTest()
    {
        ArrayList<Tuple<String, String>> Debtors = new ArrayList<Tuple<String, String>>();
        Debtors.add(new Tuple<String, String>("Max", "200$"));
        Debtors.add(new Tuple<String, String>("Misha", "400$"));
        investItems.add(new InvestItem("Router", "We bought a real router on this money", "4000$", Debtors)
        );
        mAdapter.notifyItemInserted(0);
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
                    finalQR.put(QRTranslateConfig.QRAdd, investItems.get(position).toJSON());
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

        btncanQR.setOnClickListener(new View.OnClickListener()
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
                Toast.makeText(this, resultJSON,
                               Toast.LENGTH_SHORT).show();
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
        InTest();
    }
}
