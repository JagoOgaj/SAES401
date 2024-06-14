package com.example.saes401;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.saes401.db.DatabaseHelper;
import com.example.saes401.helper.GameConstant;

public class statActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    SQLiteDatabase db;
    DatabaseHelper db_helper;
    private int pageActuel = 0;
    private static final int nombreParPage = 3;
    TextView affichagePagination;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stat);
        initAttibuts();
        setListener();
        loadDataForpageActuel();

    }

    public void initAttibuts() {
        linearLayout = findViewById(R.id.linear);
        db_helper = new DatabaseHelper(this);
        db = openOrCreateDatabase(db_helper.getDatabaseName(), MODE_PRIVATE, null);
    }

    public void setListener() {
        findViewById(R.id.mainButton).setOnClickListener(view -> onClickMain());
        findViewById(R.id.deleteButton).setOnClickListener(view -> onClickDelete());
        findViewById(R.id.nextButton).setOnClickListener(view -> onClickNext());
        findViewById(R.id.prevButton).setOnClickListener(view -> onClickPrev());
        affichagePagination = findViewById(R.id.affichagePagination);
    }

    private void onClickMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void onClickDelete() {
        deleteAllRow();
    }
    private void onClickNext() {
        int totalItemCount = getTotalItemCount();
        int maxPage = (totalItemCount + nombreParPage - 1) / nombreParPage - 1;
        if (pageActuel < maxPage) {
            pageActuel++;
            loadDataForpageActuel();
        }
    }
    private void onClickPrev() {
        if (pageActuel > 0) {
            pageActuel--;
        }
        loadDataForpageActuel();
    }

    private int getTotalItemCount() {
        Cursor cursor = db.rawQuery(db_helper.getTotalCount(), null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    private void deleteAllRow() {
        db.execSQL(db_helper.deleteAllRow());
    }


    private void loadDataForpageActuel() {
        Cursor cursor = db_helper.getDataByPage(nombreParPage, pageActuel * nombreParPage);
        linearLayout.removeAllViews();

        if (cursor.moveToFirst()) {
            do {
                // Récupérer les données de chaque ligne
                int score = cursor.getInt(cursor.getColumnIndex("score"));
                String duration = cursor.getString(cursor.getColumnIndex("duration"));
                int maxDamageToPlayer = cursor.getInt(cursor.getColumnIndex("max_damage_to_player"));
                int maxDamageToEnemy = cursor.getInt(cursor.getColumnIndex("max_damage_to_enemy"));
                int heartLost = cursor.getInt(cursor.getColumnIndex("heart_lost"));
                boolean isWin = cursor.getInt(cursor.getColumnIndex("is_win")) > 0;

                // Créer une vue pour afficher ces données
                TextView textView = new TextView(this);
                textView.setBackground(ContextCompat.getDrawable(this, R.drawable.encadrementtextview)); // Appliquer le fond avec bordure
                setColoredKeywords(textView,score,duration,maxDamageToPlayer,maxDamageToEnemy,heartLost,isWin,this);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    textView.setTextSize(9); // Taille plus petite en paysage
                } else {
                    textView.setTextSize(25); // Taille plus grande en portrait
                }
                // Ajouter la vue au LinearLayout
                linearLayout.addView(textView);
            } while (cursor.moveToNext());
        }
        cursor.close();
        int totalItemCount = getTotalItemCount();
        int totalPageCount = (totalItemCount + nombreParPage - 1) / nombreParPage;
        affichagePagination.setText("Page " + (pageActuel + 1) + " of " + totalPageCount);
    }


    public static void setColoredKeywords(TextView textView, int score, String duration, int maxDamageToPlayer, int maxDamageToEnemy, int heartLost, boolean isWin, Context context) {
        // Format du texte initial
        String fullText = String.format("Score: %d \nDuration: %s \nMax Damage to Player: %d \nMax Damage to Enemy: %d \nHeart Lost: %d \nWin: %b",
                score, duration, maxDamageToPlayer, maxDamageToEnemy, heartLost, isWin);


        // Création d'un SpannableString à partir du texte complet
        SpannableString spannableString = new SpannableString(fullText);

        // Définir la couleur
        int color = Color.parseColor("#A3E34C");

        // Appliquer le style aux mots clés
        String[] keywords = {"Score:", "Duration:", "Max Damage to Player:", "Max Damage to Enemy:", "Heart Lost:", "Win:"};
        for (String keyword : keywords) {
            int start = spannableString.toString().indexOf(keyword);
            int end = start + keyword.length();
            if (start >= 0) {
                spannableString.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        Typeface typeface = ResourcesCompat.getFont(context, R.font.dpcomic);
        textView.setTypeface(typeface);

        // Mettre le texte formaté dans le TextView
        textView.setText(spannableString);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("pageActuel", pageActuel); // Sauvegarder la page actuelle
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pageActuel = savedInstanceState.getInt("pageActuel", 0); // Restaurer la page actuelle
        loadDataForpageActuel(); // Recharger les données pour la page restaurée
    }

}