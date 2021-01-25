package com.cursoandroid.vivadecorafilmes.actvity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cursoandroid.vivadecorafilmes.R;
import com.cursoandroid.vivadecorafilmes.adapter.AdapterFilmes;
import com.cursoandroid.vivadecorafilmes.helper.Conexao;
import com.cursoandroid.vivadecorafilmes.helper.RecyclerItemClickListener;
import com.cursoandroid.vivadecorafilmes.model.Filme;
import com.cursoandroid.vivadecorafilmes.service.ServiceFilme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerFilmes;
    private List<Filme> filmes;
    private Filme filmeSelecionado;
    private int anuncio = 2, page = 1;
    private TextView maisFilmes;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerFilmes = findViewById(R.id.filmes);
        maisFilmes = findViewById(R.id.maisFilmes);
        progressBar = findViewById(R.id.progressBarMaisFilmes);

        progressBar.setVisibility(View.INVISIBLE);
        filmes = new ArrayList<>();

        getSupportActionBar().setElevation(0);

        Tarefa tarefa = new Tarefa();
        tarefa.execute("https://api.themoviedb.org/3/discover/movie?api_key=a9b4aa2bd7b0cdc0e0d9a9d837537d22&language=pt-BR");

        recyclerFilmes.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerFilmes, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        filmeSelecionado = filmes.get(position);

                        Intent intent = new Intent(getApplicationContext(), DetalhesActivity.class);
                        intent.putExtra("filmeSelecionado", filmeSelecionado);

                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                })
        );
    }

    private class Tarefa extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                String retorno = Conexao.getDados(strings[0]);
                return retorno;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);

            filmes.addAll(Objects.requireNonNull(ServiceFilme.dadosFilmes(json)));

            iniciarRecycler();
        }
    }

    private void iniciarRecycler(){
        page+=1;

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerFilmes.setLayoutManager(layoutManager);
        recyclerFilmes.setHasFixedSize(true);

        AdapterFilmes adapter = new AdapterFilmes(filmes);
        recyclerFilmes.setAdapter(adapter);
    }

    public void carregarMaisFilmes(View view){
        progressBar.setVisibility(View.VISIBLE);
        maisFilmes.setVisibility(View.INVISIBLE);

        Tarefa tarefa = new Tarefa();
        tarefa.execute("https://api.themoviedb.org/3/discover/movie?api_key=a9b4aa2bd7b0cdc0e0d9a9d837537d22&language=pt-BR&page="+page);

        progressBar.setVisibility(View.INVISIBLE);
        maisFilmes.setVisibility(View.VISIBLE);
    }
}