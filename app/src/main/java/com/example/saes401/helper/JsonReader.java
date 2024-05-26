package com.example.saes401.helper;

import android.content.Context;
import android.content.res.Resources;

import com.example.saes401.entities.Enemie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class JsonReader {
    public static String getNaration(Context context, int index) {
        String naration = null;
        try {
            String json = loadJsonFromRaw(context.getResources(), GameConstant.CATALOGUE, context.getPackageName());
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

    private static int getNumberEnemies(Context context, String levelFile) throws Exception {
        int lenght = -1;
        try {
            String json = loadJsonFromRaw(context.getResources(), levelFile, context.getPackageName());
            JSONObject jsonObject = new JSONObject(json);
            lenght = jsonObject.getJSONArray("enemies").length() - 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (lenght == -1) throw new Exception("null enemies array");

        return lenght;
    }

    private static JSONObject getEnemyAtIndex(Context context, String levelFile, int index) {
        JSONObject enemyObject = null;
        try {
            String json = loadJsonFromRaw(context.getResources(), levelFile, context.getPackageName());
            JSONObject jsonObject = new JSONObject(json);
            JSONArray enemiesArray = jsonObject.getJSONArray("enemies");

            // Vérifiez si l'indice est valide
            if (index >= 0 && index < enemiesArray.length()) {
                enemyObject = enemiesArray.getJSONObject(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return enemyObject;
    }

    public static String getEnemieImageSrc(Context context, String levelFile, int index) throws Exception {
        String imgSrc = getEnemyAtIndex(context, levelFile, index).getString("image");
        if (imgSrc == null) throw new Exception("null enemieImage");
        else return imgSrc;
    }

    public static String getEnemieDesc(Context context, String levelFile, int index) throws Exception {
        String desc = getEnemyAtIndex(context, levelFile, index).getString("description");
        if (desc == null) throw new Exception("null enemieDescription");
        else return desc;
    }

    private static JSONObject getStats(Context context, String levelFile, int index) {
        JSONObject stats = null;
        try {
            stats = getEnemyAtIndex(context, levelFile, index).getJSONObject("stats");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }

    public static int getEnemieHP(Context context, String levelFile, int index) throws Exception {
        int hp = getStats(context, levelFile, index) == null ? -1 : getStats(context, levelFile, index).getInt("pv");
        if (hp == -1) throw new Exception("null enemieHp");
        else return hp;
    }

    public static String getEnemieDamageStringFormat(Context context, String levelFile, int index) throws Exception {
        String damage = getStats(context, levelFile, index) == null ? null : getStats(context, levelFile, index).getString("degats");
        if (damage == null) throw new Exception("null enemieDamage");
        else return damage;
    }

    public static String[] getItemsOfEnemie(Context context, String levelFile, int index) throws Exception {
        JSONArray items = getStats(context, levelFile, index) == null || !getStats(context, levelFile, index).has("items") ? null
                : getStats(context, levelFile, index).getJSONArray("items");
        if (items == null) throw new Exception("null enemieItems");
        else {
            String[] itemsString = new String[items.length()];
            for (int i = 0; i < items.length(); i++) {
                itemsString[i] = items.getString(i);
            }
            return itemsString;
        }
    }

    private static JSONObject getWinOfEnemieAttribut(Context context, String levelFile, int index) {
        JSONObject winAttribut = null;
        try {
            winAttribut = getEnemyAtIndex(context, levelFile, index).getJSONObject("win");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return winAttribut;
    }

    public static String[] getDropOfEnemie(Context context, String levelFile, int index) throws Exception {
        JSONArray items = getWinOfEnemieAttribut(context, levelFile, index) == null ||
                !getWinOfEnemieAttribut(context, levelFile, index).has("dropPossible") ||
                getWinOfEnemieAttribut(context, levelFile, index).getJSONArray("dropPossible").length() == 0 ? null
                : getWinOfEnemieAttribut(context, levelFile, index).getJSONArray("dropPossible");
        if (items == null) throw new Exception("null Drop");
        else {
            String[] itemsDrop = new String[items.length()];
            for (int i = 0; i < items.length(); i++) {
                itemsDrop[i] = items.getString(i);
            }
            return itemsDrop;
        }
    }

    public static String getNarationAfterWinEnemie(Context context, String levelFile, int index) throws Exception {
        String naration = getWinOfEnemieAttribut(context, levelFile, index) == null ||
                !getWinOfEnemieAttribut(context, levelFile, index).has("dropPossible") ? null
                : getWinOfEnemieAttribut(context, levelFile, index).getString("dropPossible");
        if (naration == null) throw new Exception("null enemieNarationAfterWin");
        else return naration;
    }

    public static int getIndexBoss(Context context, String levelFile, int index) throws Exception {
        int indexBoss = getWinOfEnemieAttribut(context, levelFile, index) == null ||
                !getWinOfEnemieAttribut(context, levelFile, index).has("suivantAvecClee") ? -1
                : getWinOfEnemieAttribut(context, levelFile, index).getInt("suivantAvecClee");
        if (indexBoss == -1) throw new Exception("null indexForBoss");
        else return indexBoss;
    }

    public static String[] getChoise(Context context, String levelFile) {
        String[] choise = null;
        try {
            String json = loadJsonFromRaw(context.getResources(), levelFile, context.getPackageName());
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
            String json = loadJsonFromRaw(context.getResources(), GameConstant.OBJETS, context.getPackageName());
            imageSrc = new JSONObject(json).
                    getJSONObject("objets").
                    getJSONObject(objet).getString("image");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageSrc;
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
