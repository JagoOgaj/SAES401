package com.example.saes401;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saes401.entities.Player;
import com.example.saes401.helper.GameConstant;
import com.example.saes401.helper.JsonReader;
import com.example.saes401.helper.Utilities;
import com.example.saes401.utilities.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GameChoise extends AppCompatActivity implements Utilities {

    private Intent intent;
    private Player playerInstance;
    private int currentLevel;
    private TextView textLevel;
    private LinearLayout choiseBeforeLevel;
    private ImageButton imageButton1;
    private ImageButton imageButton2;
    private ImageButton imageButton3;
    private Button buttonContinueToLevel;
    ImageButton selectedButton = null;
    private int currentEnemieIndex;
    private boolean levelStart;
    private boolean gameContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choise);
        intent = getIntent();
        if (intent != null) {
            initAttibuts();
        }

        // Récupérer les noms des objets
        JSONArray objets = JsonReader.getItem(this, String.format(GameConstant.FORMAT_LEVEL, currentLevel));

        // Mettre à jour les ImageButton avec les images des objets
        try {
            if (objets != null && objets.length() > 0) {
                JSONObject objet1 = objets.getJSONObject(0);
                imageButton1.setImageResource(getResources().getIdentifier(objet1.getString("image"), "drawable", getPackageName()));
                imageButton1.setTag(objet1);
            }
            if (objets != null && objets.length() > 1) {
                JSONObject objet2 = objets.getJSONObject(1);
                imageButton2.setImageResource(getResources().getIdentifier(objet2.getString("image"), "drawable", getPackageName()));
                imageButton2.setTag(objet2);
            }
            if (objets != null && objets.length() > 2) {
                JSONObject objet3 = objets.getJSONObject(2);
                imageButton3.setImageResource(getResources().getIdentifier(objet3.getString("image"), "drawable", getPackageName()));
                imageButton3.setTag(objet3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setListener();
    }

    @Override
    public void initAttibuts() {
        textLevel = findViewById(R.id.textLevel);
        choiseBeforeLevel = findViewById(R.id.choiseBeforeLevel);
        imageButton1 = findViewById(R.id.imageButton1);
        imageButton2 = findViewById(R.id.imageButton2);
        imageButton3 = findViewById(R.id.imageButton3);
        buttonContinueToLevel = findViewById(R.id.buttonContinueToLevel);
        currentLevel = intent.getIntExtra(GameConstant.KEY_LEVEL, 0);
        playerInstance = intent.getParcelableExtra(GameConstant.KEY_PLAYER);
        currentEnemieIndex = intent.getIntExtra(GameConstant.KEY_ENEMIE_INDEX, 0);
        levelStart = intent.getBooleanExtra(GameConstant.KEY_START_LEVEL, false);
        gameContinue = intent.getBooleanExtra(GameConstant.KEY_PLAYER_WIN, false);
    }

    @Override
    public void startActivityGameChoise() {
        //void
    }

    @Override
    public void startActivityGameNaration() {
        //void
    }

    @Override
    public void statActivityStory() {
        //void
    }

    @Override
    public void startActivityGame() {
        boolean canAddItem;
        try {
            canAddItem = addItemToPlayer();
            if(!canAddItem){
                showAlertDialog("Impossible", "Impossible");
                //todo recup l'objet saisie et insérer dans player avec un setInventaire si sa passe sinon refaire
            }
            else {
                this.intent = new Intent(this, GameActivity.class);
                this.intent.putExtra(GameConstant.KEY_LEVEL, this.currentLevel);
                this.intent.putExtra(GameConstant.KEY_PLAYER, this.playerInstance);
                this.intent.putExtra(GameConstant.KEY_PREVIOUS_ACTIVITY, GameConstant.VALUE_GAME_CHOISE);
                this.intent.putExtra(GameConstant.KEY_ENEMIE_INDEX, this.currentEnemieIndex);
                this.intent.putExtra(GameConstant.KEY_START_LEVEL, this.levelStart);
                this.intent.putExtra(GameConstant.KEY_PLAYER_WIN, this.gameContinue);
                startActivity(this.intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            canAddItem = false;
        }
    }

    private boolean addItemToPlayer() throws JSONException {
        boolean result = true;
        JSONObject itemJson = (JSONObject) selectedButton.getTag();
        //todo mettre des infos cohérente dans le json
        Item item = new Item(itemJson.getString("nom"), itemJson.getString("degat"), itemJson.getString("image"), itemJson.getString("description"));
        if (playerInstance.isFullinventory()) result = false;
        try {
            playerInstance.setInventory(item);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    @Override
    public void setListener() {
        imageButton1.setOnClickListener(view -> {
            onClickButton((JSONObject) imageButton1.getTag());
            resetImageButtonSelection();
            selectedButton = imageButton1;
            imageButton1.setSelected(true);
            setContinueButon();
        });
        imageButton2.setOnClickListener(view -> {
            onClickButton((JSONObject) imageButton2.getTag());
            resetImageButtonSelection();
            imageButton2.setSelected(true);
            selectedButton = imageButton2;
            setContinueButon();
        });
        imageButton3.setOnClickListener(view -> {
            onClickButton((JSONObject) imageButton3.getTag());
            resetImageButtonSelection();
            imageButton3.setSelected(true);
            selectedButton = imageButton3;
            setContinueButon();
        });
        buttonContinueToLevel.setVisibility(View.INVISIBLE);
    }

    private void setContinueButon() {
        buttonContinueToLevel.setVisibility(View.VISIBLE);
        buttonContinueToLevel.setOnClickListener(v -> startActivityGame());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstance) {
        super.onRestoreInstanceState(savedInstance);
        currentLevel = savedInstance.getInt(GameConstant.KEY_LEVEL);
        playerInstance = savedInstance.getParcelable(GameConstant.KEY_PLAYER);
        levelStart = savedInstance.getBoolean(GameConstant.KEY_START_LEVEL);
        gameContinue = savedInstance.getBoolean(GameConstant.KEY_PLAYER_WIN);
        currentEnemieIndex = savedInstance.getInt(GameConstant.KEY_ENEMIE_INDEX);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(GameConstant.KEY_LEVEL, this.currentLevel);
        outState.putParcelable(GameConstant.KEY_PLAYER, this.playerInstance);
        outState.putBoolean(GameConstant.KEY_START_LEVEL, this.levelStart);
        outState.putBoolean(GameConstant.KEY_PLAYER_WIN, this.gameContinue);
        outState.putInt(GameConstant.KEY_ENEMIE_INDEX, this.currentEnemieIndex);
    }


    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void onClickButton(JSONObject objet) {
        try {
            String objetName = objet.getString("nom");
            String objetDescription = objet.getString("description");
            String objetEffet = objet.getString("effet");

            // Créez le texte avec les labels et les valeurs
            String labelObjet = "Objet : ";
            String labelDescription = "Description : ";
            String labelEffet = "Effet : ";

            // Combinez le tout dans un SpannableString
            SpannableString spannable = new SpannableString(
                    labelObjet + objetName + "\n\n" +
                            labelDescription + objetDescription + "\n\n" +
                            labelEffet + objetEffet
            );

            // Appliquez les styles aux labels
            int start = 0;
            int end = labelObjet.length();
            spannable.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            start = end + objetName.length() + 2;
            end = start + labelDescription.length();
            spannable.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            start = end + objetDescription.length() + 2;
            end = start + labelEffet.length();
            spannable.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Affichez le SpannableString dans le TextView
            textLevel.setText(spannable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetImageButtonSelection() {
        imageButton1.setSelected(false);
        imageButton2.setSelected(false);
        imageButton3.setSelected(false);
    }

}