package com.example.vladislav.ccash.backend;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapFuncs
{
    public static void saveMap(Context context, String keyPrefs, String keyItem, Map<String,Tuple<String, Integer>> inputMap){
        SharedPreferences pSharedPref = context.getSharedPreferences(keyPrefs, Context.MODE_PRIVATE);
        if (pSharedPref != null){
            //We need to parse map to JSON manually

            JSONObject resultParse = new JSONObject();

            for(String key : inputMap.keySet())
            {
                Tuple userInfo = inputMap.get(key);
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(userInfo.x);
                jsonArray.put(userInfo.y);

                try
                {
                    resultParse.put(key, jsonArray);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            String jsonString = resultParse.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove(keyItem).apply();
            editor.putString(keyItem, jsonString);
            editor.apply();
        }
    }

    public static Map<String,Tuple<String, Integer>> loadMap(Context context, String keyPrefs, String keyItem){
        Map<String, Tuple<String, Integer>> outputMap = new HashMap<>();
        SharedPreferences pSharedPref = context.getSharedPreferences(keyPrefs, Context.MODE_PRIVATE);
        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString(keyItem, (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    JSONArray jsonArray = (JSONArray) jsonObject.get(key);
                    outputMap.put(key, new Tuple<>((String)jsonArray.get(0), (Integer)jsonArray.get(1)));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return outputMap;
    }
}
