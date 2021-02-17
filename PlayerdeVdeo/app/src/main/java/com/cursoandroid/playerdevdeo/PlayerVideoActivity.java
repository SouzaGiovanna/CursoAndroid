package com.cursoandroid.playerdevdeo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class PlayerVideoActivity extends AppCompatActivity {

    private VideoView videoView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_video);

        videoView = findViewById(R.id.videoView);

        getSupportActionBar().hide();

        //Esconde a statusBar e barra de navegação
        View decorView = getWindow().getDecorView();

        int uiOpcoes = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

        decorView.setSystemUiVisibility(uiOpcoes);

        //Executar o vídeo
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoPath("android.resource://" +getPackageName()+ "/" +R.raw.inuyasha_1o_episodio);
        videoView.start();
    }
}