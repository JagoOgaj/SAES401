package com.example.saes401;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.saes401.story.Story;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private ArrayList<Button> buttonChoise = new ArrayList<Button>();
    private TextView hpCountTextView, timeCountTextView, textContentGame;

    private Button actionButton, inventory;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_game_activity);
        // Obtenez une référence à votre ConstraintLayout
        // Trouver le LinearLayout dans votre layout
        LinearLayout layout = (LinearLayout) findViewById(R.id.heartContainer2);
        LinearLayout layout50 = (LinearLayout) findViewById(R.id.heartContainer1);

// Supposons que 'pv' est le nombre de points de vie
        int pv = 2;

// Ajouter les images de coeur au layout
        for (int i = 0; i < pv; i++) {
            ImageView heart = new ImageView(this);
            ImageView heart2 = new ImageView(this);
            ImageView heart3 = new ImageView(this);
            ImageView heart4 = new ImageView(this);
            ImageView heart5 = new ImageView(this);

            ImageView heart6 = new ImageView(this);
            ImageView heart7 = new ImageView(this);
            ImageView heart8 = new ImageView(this);
            ImageView heart9 = new ImageView(this);
            ImageView heart10 = new ImageView(this);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            // Définir les marges (en pixels). Ici, nous ajoutons une marge à droite de 10px
            //layoutParams.setMargins(0, 0, 10, 0);


            // Assurez-vous d'avoir votre image de coeur dans les ressources drawable
            heart.setImageResource(R.drawable.coueurtest);
            heart2.setImageResource(R.drawable.coueurtest2);
            heart3.setImageResource(R.drawable.coueurtest3);
            heart4.setImageResource(R.drawable.coueurtest4);
            heart5.setImageResource(R.drawable.coueurtest5);

            heart6.setImageResource(R.drawable.coueurtest);
            heart7.setImageResource(R.drawable.coueurtest2);
            heart8.setImageResource(R.drawable.coueurtest3);
            heart9.setImageResource(R.drawable.coueurtest4);
            heart10.setImageResource(R.drawable.coueurtest5);

            // Appliquer les paramètres de layout à l'image de coeur
            /*
            heart.setLayoutParams(layoutParams);
            heart2.setLayoutParams(layoutParams);
            heart3.setLayoutParams(layoutParams);
            heart4.setLayoutParams(layoutParams);
            heart5.setLayoutParams(layoutParams);
            */


            // Ajouter l'image de coeur au layout
            layout.addView(heart);
            layout.addView(heart2);
            layout.addView(heart3);
            layout.addView(heart4);
            layout.addView(heart5);

            layout50.addView(heart6);
            layout50.addView(heart7);
            layout50.addView(heart8);
            layout50.addView(heart9);
            layout50.addView(heart10);
        }



        ImageView imageView = findViewById(R.id.imageView3);
        imageView.setImageResource(R.drawable.aaaa);
        // definir un max de loot
        LinearLayout layout2 = findViewById(R.id.lootContainer1);
        LinearLayout layout4 = findViewById(R.id.lootContainer2);
        for(int i = 0; i< 3; i++){
            ImageView imageView1 = new ImageView(this);
            ImageView imageView2 = new ImageView(this);
            imageView1.setImageResource(R.drawable.shield);
            imageView2.setImageResource(R.drawable.clee);
            layout2.addView(imageView2);
            layout2.addView(imageView1);

            ImageView imageView3 = new ImageView(this);
            ImageView imageView4 = new ImageView(this);
            imageView3.setImageResource(R.drawable.shield);
            imageView4.setImageResource(R.drawable.clee);
            layout4.addView(imageView3);
            layout4.addView(imageView4);
        }


        LinearLayout layout3 = (LinearLayout) findViewById(R.id.linearLayout);
        TextView textView = findViewById(R.id.resultdice);
        textView.setText("");

        for(int i = 0; i < 3; i++){
            ImageButton imageButton = new ImageButton(this);
            imageButton.setImageResource(R.drawable.sword);

            int sizeInPixels = 150; // Remplacez cette valeur par la taille souhaitée en pixels
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(sizeInPixels, sizeInPixels);
            layoutParams.setMargins(10, 10, 10, 10);
            imageButton.setLayoutParams(layoutParams);

            imageButton.setOnClickListener(view -> {
                layout3.removeAllViews();
                for (int j = 0; j < 3; j++) {
                    ImageView imageButton3 = new ImageView(this);
                    imageButton3.setBackgroundResource(R.drawable.animation_dice_test);

                    LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(
                            150,
                            150
                    );
                    layoutParams3.setMargins(10,10,10,10);
                    imageButton3.setLayoutParams(layoutParams3);
                    layout3.addView(imageButton3);

                    AnimationDrawable animation = (AnimationDrawable) imageButton3.getBackground();
                    animation.start();

                    if (j == 2) { // Si c'est la dernière animation
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                animation.stop();

                                // Supposons que 'result' est le résultat que vous voulez afficher
                                String result = "Votre résultat 3d4+5";

                                // Créer un nouveau Handler pour exécuter un Runnable après un délai
                                final Handler handler = new Handler();
                                for (int k = 0; k < result.length(); k++) {
                                    final int finalK = k;
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Ajouter la lettre suivante du résultat au TextView
                                            textView.append(String.valueOf(result.charAt(finalK)));
                                        }
                                    }, 100 * k); // Le délai est en millisecondes, donc 100 ms * i
                                }
                            }
                        }, 2000);
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                animation.stop();
                            }
                        }, 2000);
                    }
                }
            });
            layout3.addView(imageButton);
        }









        Story story = new Story(this);
        story.start();
    }

    public TextView getHpCountTextView() {
        return hpCountTextView;
    }

    public TextView getTimeCountTextView() {
        return timeCountTextView;
    }

    public TextView getTextContentGame() {
        return textContentGame;
    }

    public Button getActionButton() {
        return actionButton;
    }

    public Button getInventory() {
        return inventory;
    }

    public ArrayList<Button> getButtonChoise(){
        return buttonChoise;
    }



    public void resetLayout(){
        for(Button b : buttonChoise){
            b.setText("");
            b.setVisibility(View.VISIBLE);
        }
        textContentGame.setText("");
        hpCountTextView.setVisibility(View.VISIBLE);
        timeCountTextView.setVisibility(View.VISIBLE);
        inventory.setVisibility(View.VISIBLE);
        actionButton.setVisibility(View.VISIBLE);
    }

}
