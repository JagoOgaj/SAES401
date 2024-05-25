package com.example.saes401.helper;

import android.content.Context;
import android.content.res.Resources;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

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
    public static JSONArray getItem(Context context, String level) {
        String constanteLevel;
        switch (level) {
            case "niveau1":
                constanteLevel = GameConstant.NIVEAU1;
                break;
            case "niveau2":
                constanteLevel = GameConstant.NIVEAU2;
                break;
            default:
                constanteLevel = GameConstant.NIVEAU3;
                break;
        }

        try {
            Resources res = context.getResources();
            // Charger le JSON correspondant au niveau
            String json = loadJsonFromRaw(res, constanteLevel, context.getPackageName());
            JSONObject jsonObject = new JSONObject(json);
            JSONArray objets = jsonObject.getJSONArray("objets");

            // Récupérer les noms de chaque objet et mélanger
            List<String> objetsList = new ArrayList<>();
            for (int i = 0; i < objets.length(); i++) {
                objetsList.add(objets.getString(i));
            }
            Collections.shuffle(objetsList);

            // Sélectionner 3 objets aléatoires
            JSONArray randomObjets = new JSONArray();
            for (int i = 0; i < 3 && i < objetsList.size(); i++) {
                randomObjets.put(objetsList.get(i));
            }

            // Charger le JSON complet des objets
            json = loadJsonFromRaw(res, "objets", context.getPackageName());
            JSONObject allObjets = new JSONObject(json).getJSONObject("objets");

            // Récupérer les objets complets
            JSONArray result = new JSONArray();
            for (int i = 0; i < randomObjets.length(); i++) {
                result.put(allObjets.getJSONObject(randomObjets.getString(i)));
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String loadJsonFromRaw(Resources res, String resourceName, String packageName) {
        try {
            int resourceId = res.getIdentifier(resourceName, "raw", packageName);
            InputStream inputStream = res.openRawResource(resourceId);
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
            //ne leve pas d'exception si le json est vide lors de la lecture
            String json = scanner.hasNext() ? scanner.next() : "";
            scanner.close();
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}



