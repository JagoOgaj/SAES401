package com.example.saes401;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.saes401.choseCharacter.ChoseCharacter;

import java.util.Arrays;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_game_activity);
        viewPager = findViewById(R.id.viewPager);
        List<Integer> characterImages = Arrays.asList(R.drawable.knight, R.drawable.samourai); // Remplacez les IDs des ressources par les vôtres
        ChoseCharacter adapter = new ChoseCharacter(this, characterImages);
        viewPager.setAdapter(adapter);
    }

    public void previousCharacter(View view) {
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }

    public void nextCharacter(View view) {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }
}
