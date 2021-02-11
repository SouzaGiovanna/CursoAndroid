package com.cursoandroid.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
    ImageButton playPause, stop;
    SeekBar volume;
    boolean play = true;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;

    //true = play / false = pause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.hanyo_no_yashahime);

        playPause = findViewById(R.id.btnPlayPause);
        stop = findViewById(R.id.btnStop);
        volume = findViewById(R.id.seekBarVolume);

        inicializarSeekBar();

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(play){
                    playPause.setImageResource(R.drawable.ic_baseline_pause_24);

                    executarSom();

                    play = false;
                }
                else{
                    playPause.setImageResource(R.drawable.ic_baseline_play_arrow_24);

                    pausarSom();

                    play = true;
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pararSom();
            }
        });
    }

    public void executarSom(){
        if(mediaPlayer != null){
            mediaPlayer.start();
        }
    }

    public void pararSom(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();

            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.hanyo_no_yashahime);

            playPause.setImageResource(R.drawable.ic_baseline_play_arrow_24);

            play = true;
        }
    }

    private void inicializarSeekBar(){
        //configura o audio mananger
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //recupera os valores de volume máximo e o volume atual
        int volMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int volAtual = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        //configura os valores máximos para o SeekBar
        volume.setMax(volMax);

        //configura os valores atuais para o SeekBar
        volume.setProgress(volAtual);

        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void pausarSom(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        pausarSom();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}