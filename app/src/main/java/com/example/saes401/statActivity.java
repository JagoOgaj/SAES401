package com.example.saes401;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stat);
        initAttibuts();
        this.setListener();
    }
    public void initAttibuts() {
         linearLayout= findViewById(R.id.linear);
         db_helper = new DatabaseHelper(this);
         db = openOrCreateDatabase(db_helper.getDatabaseName(), MODE_PRIVATE, null);
         int x = getTotalItemCount();
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



}