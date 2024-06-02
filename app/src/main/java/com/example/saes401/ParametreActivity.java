package com.example.saes401;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class ParametreActivity extends AppCompatActivity {
    private Switch soundSwitch; // Déclaration du Switch pour activer/désactiver le son
    private SeekBar volumeSeekBar; // Déclaration de la SeekBar pour ajuster le volume

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre); // Charge le layout de l'interface utilisateur


        volumeSeekBar = findViewById(R.id.volumeSeekBar); // Initialisation de la SeekBar à partir du layout

        loadSettings(); // Charge les paramètres actuels à partir des SharedPreferences

        // Écouteur pour le changement d'état du Switch
        soundSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveSoundEnabled(isChecked); // Sauvegarde le nouvel état du son activé/désactivé
        });

        // Écouteur pour les changements sur la SeekBar
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                saveVolume(progress / 100f); // Sauvegarde le nouveau volume calculé en pourcentage
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Méthode appelée lorsque l'utilisateur commence à déplacer le curseur de la SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Méthode appelée lorsque l'utilisateur arrête de déplacer le curseur de la SeekBar
            }
        });
    }

    // Méthode pour sauvegarder l'état du son activé/désactivé
    private void saveSoundEnabled(boolean isEnabled) {
        SharedPreferences prefs = getSharedPreferences("GameSettings", MODE_PRIVATE); // Obtient les SharedPreferences
        SharedPreferences.Editor editor = prefs.edit(); // Crée un éditeur pour les SharedPreferences
        editor.putBoolean("SoundEnabled", isEnabled); // Met à jour la valeur de "SoundEnabled"
        editor.apply(); // Applique les modifications
    }

    // Méthode pour sauvegarder le volume
    private void saveVolume(float volume) {
        SharedPreferences prefs = getSharedPreferences("GameSettings", MODE_PRIVATE); // Obtient les SharedPreferences
        SharedPreferences.Editor editor = prefs.edit(); // Crée un éditeur pour les SharedPreferences
        editor.putFloat("Volume", volume); // Met à jour la valeur de "Volume"
        editor.apply(); // Applique les modifications
    }

    // Méthode pour charger les paramètres de son
    private void loadSettings() {
        SharedPreferences prefs = getSharedPreferences("GameSettings", MODE_PRIVATE); // Obtient les SharedPreferences
        boolean soundEnabled = prefs.getBoolean("SoundEnabled", true); // Lit la valeur de "SoundEnabled", défaut à true
        float volume = prefs.getFloat("Volume", 0.5f); // Lit la valeur de "Volume", défaut à 0.5

        soundSwitch.setChecked(soundEnabled); // Définit l'état du Switch selon la valeur chargée
        volumeSeekBar.setProgress((int) (volume * 100)); // Définit la position de la SeekBar selon la valeur chargée
    }
}
