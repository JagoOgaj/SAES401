package com.example.saes401.soud;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.saes401.R;

public class GameSound {
    public static MediaPlayer launchFightSound(Context context) {
        MediaPlayer mp = MediaPlayer.create(context, R.raw.undertale);
        mp.start();
        return mp;
    }

    public static void stopFightSound(Context context, MediaPlayer mp) {
        mp.pause();
    }

    public static MediaPlayer creditSound(Context context) {
        MediaPlayer mp = MediaPlayer.create(context, R.raw.medievale);
        mp.start();
        return mp;
    }

    public static void stopCreditSound(MediaPlayer mp) {
        if (mp != null) {
            mp.stop();
            mp.release();
        }
    }

    public static MediaPlayer narrationSound(Context context) {
        MediaPlayer mp = MediaPlayer.create(context, R.raw.medievale);
        mp.start();
        return mp;
    }

    public static void stopNarrationSound(MediaPlayer mp) {
        if (mp != null) {
            mp.stop();
            mp.release();
        }
    }

    public static MediaPlayer homeScreenSound(Context context) {
        MediaPlayer mp = MediaPlayer.create(context, R.raw.medievale); // Assurez-vous d'avoir ce fichier sonore
        mp.start();
        return mp;
    }

    public static void stopHomeScreenSound(MediaPlayer mp) {
        if (mp != null) {
            mp.stop();
            mp.release();
        }
    }

    public static void playClickSound(Context context) {
        MediaPlayer mp = MediaPlayer.create(context, R.raw.dernier); // Assurez-vous d'avoir ce fichier sonore d'une seconde
        mp.start();
        mp.setOnCompletionListener(MediaPlayer::release);
    }


}
