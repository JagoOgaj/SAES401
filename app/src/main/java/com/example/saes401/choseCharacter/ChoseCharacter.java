package com.example.saes401.choseCharacter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class ChoseCharacter extends PagerAdapter {

    private Context context;
    private List<Integer> characterImages;

    public ChoseCharacter(Context context, List<Integer> characterImages) {
        this.context = context;
        this.characterImages = characterImages;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(characterImages.get(position));
        container.addView(imageView);
        return imageView;
    }

    @Override
    public int getCount() {
        return characterImages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
