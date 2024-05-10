package com.example.saes401.helper;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class JsonReader {

    public static String getNaration(Context context, int index) {
        String naration = null;
        try {
            String json = loadJson(context, GameConstant.CATALOGUE);
            JSONObject jsonObject = new JSONObject(json);
            JSONArray catalogues = jsonObject.getJSONArray("catalogues");
            if (index >= 0 && index < catalogues.length()) {
                JSONObject item = catalogues.getJSONObject(index);
                naration = item.getString("naration");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return naration;
    }

    public static String[] getChoise(Context context, String levelFile) {
        String[] choise = null;
        try {
            String json = loadJson(context, levelFile);
            JSONArray choises = new JSONObject(json).getJSONArray("objets");
            choise = new String[choises.length()];
            for (int i = 0; i < choises.length(); i++) {
                choise[i] = choises.getString(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return choise;
    }

    public static String getImageObject(Context context, String objet) {
        String imageSrc = null;
        try {
            String json = loadJson(context, GameConstant.OBJETS);
            imageSrc = new JSONObject(json).
                    getJSONObject("objets").
                    getJSONObject(objet).getString("image");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageSrc;
    }

    private static String loadJson(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
