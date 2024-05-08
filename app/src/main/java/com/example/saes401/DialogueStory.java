package com.example.saes401;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;

public class DialogueStory extends AppCompatActivity {



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dialoguestory);

            TextView dialogueText = findViewById(R.id.texte_dialogue);
            String jsonString = loadJSONFromAsset();


            JSONObject jsonObject = null;
            try {
                assert jsonString != null;
                jsonObject = new JSONObject(jsonString);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            String dialogue = null;
            try {
                dialogue = jsonObject.getString("texte");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            dialogueText.setText(dialogue);

        }
        private String loadJSONFromAsset() {
            String json = null;
            try {
                InputStream is = getAssets().open("@raw/storytest.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            return json;
        }
    }





