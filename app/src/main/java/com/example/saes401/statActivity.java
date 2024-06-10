package com.example.saes401;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.saes401.db.DatabaseHelper;
import com.example.saes401.helper.GameConstant;

public class statActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    SQLiteDatabase db;
    DatabaseHelper db_helper;
    private int currentPage = 0;
    private static final int ITEMS_PER_PAGE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stat);
        initAttibuts();
        setListener();
        setPaginationListeners();
        loadDataForCurrentPage();
    }

    public void initAttibuts() {
        linearLayout = findViewById(R.id.linear);
        db_helper = new DatabaseHelper(this);
        db = openOrCreateDatabase(db_helper.getDatabaseName(), MODE_PRIVATE, null);
    }

    public void setListener() {
        findViewById(R.id.mainButton).setOnClickListener(view -> onClickMain());
    }

    private void onClickMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private int getTotalItemCount() {
        Cursor cursor = db.rawQuery(db_helper.getTotalCount(), null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    private void setPaginationListeners() {
        findViewById(R.id.nextButton).setOnClickListener(view -> {
            currentPage++;
            loadDataForCurrentPage();
        });

        findViewById(R.id.prevButton).setOnClickListener(view -> {
            if (currentPage > 0) {
                currentPage--;
            }
            loadDataForCurrentPage();
        });
    }


    private void loadDataForCurrentPage() {
        Cursor cursor = db_helper.getDataByPage(ITEMS_PER_PAGE, currentPage * ITEMS_PER_PAGE);
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
                textView.setText(String.format("Score: %d, Duration: %s, Max Damage to Player: %d, Max Damage to Enemy: %d, Heart Lost: %d, Win: %b",
                        score, duration, maxDamageToPlayer, maxDamageToEnemy, heartLost, isWin));

                // Ajouter la vue au LinearLayout
                linearLayout.addView(textView);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }


}