package com.example.vladislav.ccash;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ManageDebtorsActivity extends AppCompatActivity
{
    Button goBack;
    ArrayList<String> debtors;

    ListView listViewDebtors;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.managedebtors_activity);
        initView();
        initListView();
    }

    private void initView()
    {
        goBack = (Button) findViewById(R.id.buttonGoBack);
        listViewDebtors = (ListView) findViewById(R.id.listViewDebtors);
    }

    private void testdebtors()
    {
        debtors.add("Max");
        debtors.add("Misha");
    }

    private void initListView()
    {
        debtors = new ArrayList<>();
        testdebtors();
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
