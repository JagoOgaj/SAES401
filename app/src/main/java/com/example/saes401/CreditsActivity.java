package com.example.saes401;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_credit);
        findViewById(R.id.goBackButton).setOnClickListener(view -> onClickGoBack());
    }

    private void onClickGoBack() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        //tesst
    }
}
