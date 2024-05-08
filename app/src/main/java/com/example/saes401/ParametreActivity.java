package com.example.saes401;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class ParametreActivity extends AppCompatActivity {
    private Spinner langueSpinner;
    private Button enregistrerButton;


    private String selectedLanguage;
    private Intent intent;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre);
        langueSpinner = findViewById(R.id.langueSpinner);
        enregistrerButton = findViewById(R.id.enregistrerButton);
        findViewById(R.id.menuprincipaleButton).setOnClickListener(view -> onClickMenu());



        // Créer un ArrayAdapter en utilisant le tableau de chaînes et un layout par défaut du spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.langues_array, android.R.layout.simple_spinner_item);

        // Spécifier le layout à utiliser lorsque la liste des choix apparaît
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Appliquer l'adaptateur au spinner
        langueSpinner.setAdapter(adapter);

        langueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLanguage = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //  Appelée lorsqu'aucun élément n'est sélectionné.
            }
        });

        enregistrerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale(selectedLanguage);
            }
        });
    }
    private void onClickMenu(){

        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void setLocale(String lang) {
        if ("French".equalsIgnoreCase(lang) || "Français".equalsIgnoreCase(lang)) {
            lang = "fr";
        } else if ("English".equalsIgnoreCase(lang) || "Anglais".equalsIgnoreCase(lang)) {
            lang = "en";
        }



        updateLocale(lang);
    }

    public void updateLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
            Context context = createConfigurationContext(config);
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        } else {
            config.locale = locale;
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }

        finish();
        startActivity(getIntent());
    }



}
