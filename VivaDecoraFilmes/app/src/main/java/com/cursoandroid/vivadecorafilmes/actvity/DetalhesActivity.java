package com.cursoandroid.vivadecorafilmes.actvity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.cursoandroid.vivadecorafilmes.R;
import com.cursoandroid.vivadecorafilmes.model.Filme;
import com.cursoandroid.vivadecorafilmes.service.DownloadImageTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetalhesActivity extends AppCompatActivity {
    private TextView titulo, genero, dataPublicacao, avaliacao, sinopse;
    private Filme filme;
    private ImageView capa, banner;
    private ProgressBar progressBarCapa, progressBarBanner;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);

        titulo = findViewById(R.id.titulo);
        genero = findViewById(R.id.genero);
        dataPublicacao = findViewById(R.id.dataPublicacao);
        avaliacao = findViewById(R.id.avaliacaoFilme);
        sinopse = findViewById(R.id.sinopse);
        capa = findViewById(R.id.imgCapa);
        banner = findViewById(R.id.imgBanner);
        progressBarCapa = findViewById(R.id.progressBarCapa);
        progressBarBanner = findViewById(R.id.progressBarBanner);

        filme = (Filme) getIntent().getSerializableExtra("filmeSelecionado");

        try {
            preencherText();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        recuperarImagens();
    }

    public void voltarAction(View view){
        finish();
    }

    @SuppressLint("SetTextI18n")
    private void preencherText() throws ParseException {
        SimpleDateFormat formatoOriginal = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatoConvertido = new SimpleDateFormat("dd-MM-yyyy");

        Date data = formatoOriginal.parse(filme.getRelaceDate());
        String publicacao = formatoConvertido.format(data);

        titulo.setText(filme.getTitle());
        //genero.setText();
        dataPublicacao.setText(publicacao.replaceAll("-", "/"));
        avaliacao.setText(filme.getVoteAverage().toString());
        sinopse.setText(filme.getOverview());
    }

    private void recuperarImagens(){
        new DownloadImageTask(banner, progressBarBanner).execute("https://image.tmdb.org/t/p/w500/"+filme.getBackdropPath());
        new DownloadImageTask(capa, progressBarCapa).execute("https://image.tmdb.org/t/p/w500/"+filme.getPosterPath());
    }
}