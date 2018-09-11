package com.example.vladislav.ccash.Frontend;

import com.example.vladislav.ccash.backend.Tuple;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class InvestItem
{
    private int db_id;

    private String InvestName;

    private String InvestDescription;
    private float InvestMyDebts;
    private float InvestSum;
    private ArrayList<Debtor> Debts;

    public int getDb_id()
    {
        return db_id;
    }

    public void setDb_id(int db_id)
    {
        this.db_id = db_id;
    }

    public float getInvestMyDebts()
    {
        return InvestMyDebts;
    }

    public void setInvestMyDebts(float investMyDebts)
    {
        InvestMyDebts = investMyDebts;
    }

    public String getInvestName()
    {
        return InvestName;
    }

    public void setInvestName(String investName)
    {
        InvestName = investName;
    }

    public String getInvestDescription()
    {
        return InvestDescription;
    }

    public void setInvestDescription(String investDescription)
    {
        InvestDescription = investDescription;
    }

    public float getInvestSum()
    {
        return InvestSum;
    }

    public void setInvestSum(float investSum)
    {
        InvestSum = investSum;
    }

    public ArrayList<Debtor> getDebts()
    {
        return Debts;
    }

    public void setDebts(ArrayList<Debtor> debts)
    {
        Debts = debts;
    }

    public InvestItem()
    {
    }

    public InvestItem(String investName, String investDescription, float investMyDebts, float investSum, ArrayList<Debtor> debts){
        setDebts(debts);
        setInvestDescription(investDescription);
        setInvestName(investName);
        setInvestMyDebts(investMyDebts);
        setInvestSum(investSum);
    }

    public JSONObject toJSON()
    {
        JSONObject object = new JSONObject();
        try
        {
            object.put(InvestItemKeys.InvestNameKey, InvestName);
            object.put(InvestItemKeys.InvestDescriptionKey, InvestDescription);
            object.put(InvestItemKeys.InvestSumKey, InvestSum);

            JSONArray arr = new JSONArray();
            for(Debtor debtor : Debts)
            {
                arr.put(debtor.getDebtorUID());
                arr.put(debtor.getDebtorName());
                arr.put(debtor.getDebtorDebt());
            }

            object.put(InvestItemKeys.InvestDebtorsKey, arr);

            return object;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
