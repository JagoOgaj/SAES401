package com.example.saes401;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saes401.helper.Settings;
import com.example.saes401.soud.GameSound;

import java.util.Locale;
import java.util.Objects;

public class ParametreActivity extends AppCompatActivity {
    private SeekBar volumeSeekBar;
    private Spinner langueSpinner;
    private Button sauvegardeButton, mainButton;
    String selectedLanguage;
    int volume;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre);

        volumeSeekBar = findViewById(R.id.volumeSeekBar);
        langueSpinner = findViewById(R.id.languespinner);
        sauvegardeButton = findViewById(R.id.sauvegardeButton);
        mainButton = findViewById(R.id.mainButton);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        setupSpinner();
        setupSeekBar();
        setupButtons();
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.langues_array, R.layout.spinner_vert);
        adapter.setDropDownViewResource(R.layout.spinner_vert);
        langueSpinner.setAdapter(adapter);
        String langueParDefaut = Settings.loadLanguage(this);
        chargementParDefaut(langueParDefaut);
        langueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Ce code est exécuté lorsque l'utilisateur sélectionne une langue dans le Spinner
                selectedLanguage = parent.getItemAtPosition(position).toString();
                if (Objects.equals(selectedLanguage, "French") || Objects.equals(selectedLanguage, "Français")) {
                    selectedLanguage = "fr";
                } else if (Objects.equals(selectedLanguage, "English") || Objects.equals(selectedLanguage, "Anglais")) {
                    selectedLanguage = "en";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Ce code est exécuté si aucune sélection n'est faite
            }
        });
    }

    private void setupSeekBar() {
        //objet audio
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volumeSeekBar.setMax(maxVolume);
        //initialise le volume max de la machine
        int volumeSave = Settings.loadVolume(this);
        volumeSeekBar.setProgress(volumeSave);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeSave, 0);

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Ce code est exécuté chaque fois que l'utilisateur modifie la position du curseur

                if (fromUser) {  // Vérifie si le changement vient de l'utilisateur
                    // Applique le volume sélectionné au flux de musique
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_SHOW_UI);
                    // FLAG_SHOW_UI permet d'afficher un indicateur visuel de changement de volume sur l'écran
                    volume = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optionnel, appelé lorsque l'utilisateur commence à toucher le SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optionnel, appelé après que l'utilisateur ait relâché le SeekBar
            }
        });
    }

    private void setupButtons() {
        sauvegardeButton.setOnClickListener(v -> {
            GameSound.playClickSound(v.getContext()); // Ajout du son de clic
            Settings.saveSettings(this, volume, selectedLanguage);
            String message = "Saved parameters";
            if (selectedLanguage.equals("fr")) {
                message = "Paramètres sauvegardés";
            }

            Toast.makeText(ParametreActivity.this, message, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        mainButton.setOnClickListener(v -> {
            GameSound.playClickSound(v.getContext()); // Ajout du son de clic
            // Code pour retourner au menu principal
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void chargementParDefaut(String langue) {
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) langueSpinner.getAdapter();
        int spinnerPosition = adapter.getPosition(isoVersLangue(langue));
        langueSpinner.setSelection(spinnerPosition);
    }

    private String isoVersLangue(String langue) {
        Locale locale = new Locale(langue);
        return locale.getDisplayName(locale);
    }
}


//    private void save(int volume, String langue) {
//        SharedPreferences sharedPreferences = getSharedPreferences("AppSettingsPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("SavedVolume", volume);
//        editor.putString("SavedLanguage", langue);
//        editor.apply();
//    }
//
//    private void load() {
//        // Obtenir l'instance de SharedPreferences
//        SharedPreferences sharedPreferences = getSharedPreferences("AppSettingsPrefs", MODE_PRIVATE);
//        // Lire la valeur du volume, avec une valeur par défaut si non trouvée
//        int savedVolume = sharedPreferences.getInt("SavedVolume", audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
//        // Définir la progression du SeekBar avec la valeur sauvegardée
//        volumeSeekBar.setProgress(savedVolume);
//        String langue = sharedPreferences.getString("SavedLanguage", "en");
//        changeLanguage(langue);
//        if(Objects.equals(langue,"en")){
//            langue = "English";
//        }
//        int spinnerPosition = ((ArrayAdapter) langueSpinner.getAdapter()).getPosition(langue);
//        langueSpinner.setSelection(spinnerPosition, false);
//    }
//    private void changeLanguage(String langue) {
//
//        if(Objects.equals(langue, "French") || Objects.equals(langue, "Français")){
//            langue="fr";
//        }
//        else if (Objects.equals(langue, "English") || Objects.equals(langue, "Anglais")){
//            langue="en";
//        }
//        selectedLanguage=langue;
//        if (!Locale.getDefault().getLanguage().equals(langue)) {
//            Locale locale = new Locale(langue);
//            Locale.setDefault(locale);
//            Configuration config = new Configuration();
//            config.setLocale(locale);
//            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
//
//            // Redémarrage de l'activité pour appliquer la nouvelle configuration de langue
//            recreate();
//        }
//    }

