package com.example.saes401;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.saes401.helper.JsonReader;
import com.example.saes401.helper.OnTextLoadedListener;
import com.example.saes401.story.Story;

public class GameNaration extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_naration);
        intent = getIntent();
        launchInitStory();
    }

    private TextView getTextView() {
        return findViewById(R.id.narationContainer);
    }

    private Button getButtonContinue() {
        return findViewById(R.id.continueButton);
    }

    private void launchInitStory() {

    }

    private void launchNaration() throws Exception {
        String naration;
        if (intent.getStringExtra("level").equals("niveau1")) {
            naration = JsonReader.getNaration(this, 0);
            setVisibilityOfContinue(getTextView(), naration);
        } else if (intent.getStringExtra("level").equals("niveau2")) {
            naration = JsonReader.getNaration(this, 1);
            setVisibilityOfContinue(getTextView(), naration);
        } else if (intent.getStringExtra("level").equals("niveau3")) {
            naration = JsonReader.getNaration(this, 2);
            setVisibilityOfContinue(getTextView(), naration);
        } else {
            throw new Exception("null pointer file by intent");
        }
    }

    private void loadText(TextView textView, String naration, OnTextLoadedListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler();
                for (int k = 0; k < naration.length(); k++) {
                    final int finalK = k;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textView.append(String.valueOf(naration.charAt(finalK)));
                            if (finalK == naration.length() - 1 && listener != null) {
                                listener.onTextLoaded();
                            }
                        }
                    }, 100 * k);
                }
            }
        }, 2000);
    }

    private void setVisibilityOfContinue(TextView textView, String naration) {
        loadText(textView, naration, new OnTextLoadedListener() {
            @Override
            public void onTextLoaded() {
                // Faire quelque chose à la fin de l'exécution de loadText()
                getButtonContinue().setVisibility(View.VISIBLE);
                getButtonContinue().setOnClickListener(view -> {
                    Intent intent2 = new Intent(GameNaration.this, GameChoise.class);
                    intent2.putExtra("curentLevel", intent.getStringExtra("level"));
                    startActivity(intent2);
                });
            }
        });

    }

}
