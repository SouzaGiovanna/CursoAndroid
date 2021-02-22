package com.cursoandroid.aprendaingls.helper;

import android.media.MediaPlayer;

public class PlayerAudio {
    public void executarSom(MediaPlayer mediaPlayer){
        if(mediaPlayer != null){
            mediaPlayer.start();
        }
    }
}
