package com.example.vladislav.ccash.Frontend;

import com.example.vladislav.ccash.backend.Tuple;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InvestItem
{
    String InvestName;
    String InvestDescription;
    String InvestSum;
    ArrayList<Tuple<String, Integer>> Debts;

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

    public String getInvestSum()
    {
        return InvestSum;
    }

    public void setInvestSum(String investSum)
    {
        InvestSum = investSum;
    }

    public ArrayList<Tuple<String, Integer>> getDebts()
    {
        return Debts;
    }

    public void setDebts(ArrayList<Tuple<String, Integer>> debts)
    {
        Debts = debts;
    }

    public InvestItem()
    {
    }

    public InvestItem(String investName, String investDescription, String investSum, ArrayList<Tuple<String, Integer>> debts){
        setDebts(debts);
        setInvestDescription(investDescription);
        setInvestName(investName);
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

            JSONObject arr = new JSONObject();

            for(Tuple<String, Integer> t : Debts)
            {
                arr.put(t.x ,t.y);
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
