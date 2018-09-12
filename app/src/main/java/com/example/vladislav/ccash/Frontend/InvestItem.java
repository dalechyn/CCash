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
    private double InvestMyDebts;
    private double InvestSum;
    private ArrayList<Debtor> Debts;

    public int getDb_id()
    {
        return db_id;
    }

    public void setDb_id(int db_id)
    {
        this.db_id = db_id;
    }

    public double getInvestMyDebts()
    {
        return InvestMyDebts;
    }

    public void setInvestMyDebts(double investMyDebts)
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

    public double getInvestSum()
    {
        return InvestSum;
    }

    public void setInvestSum(double investSum)
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

    public InvestItem(String investName, String investDescription, double investMyDebts, double investSum, ArrayList<Debtor> debts){
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
            object.put(InvestItemKeys.InvestMyDebtKey, InvestMyDebts);

            if(Debts != null)
            {
                JSONArray arr = new JSONArray();
                for (Debtor debtor : Debts)
                {
                    arr.put(debtor.getDebtorUID());
                    arr.put(debtor.getDebtorName());
                    arr.put(debtor.getDebtorDebt());
                }
                object.put(InvestItemKeys.InvestDebtorsKey, arr);
            }
            //else object.put(InvestItemKeys.InvestDebtorsKey, null);

            return object;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
