package com.cursoandroid.vivadecorafilmes.actvity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cursoandroid.vivadecorafilmes.R;
import com.cursoandroid.vivadecorafilmes.adapter.AdapterFilmes;
import com.cursoandroid.vivadecorafilmes.helper.Conectividade;
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
    private AdapterFilmes adapter = new AdapterFilmes();
    private int anuncio = adapter.getAnuncio(), page = 1;
    private TextView maisFilmes, semConexaoText, textView;
    private ProgressBar progressBar;
    private ImageView semConexao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerFilmes = findViewById(R.id.filmes);
        maisFilmes = findViewById(R.id.maisFilmes);
        progressBar = findViewById(R.id.progressBarMaisFilmes);
        semConexao = findViewById(R.id.semConexao);
        semConexaoText = findViewById(R.id.semConexaoText);
        textView = findViewById(R.id.textView);

        progressBar.setVisibility(View.INVISIBLE);
        semConexao.setVisibility(View.GONE);
        semConexaoText.setVisibility(View.GONE);

        filmes = new ArrayList<>();

        getSupportActionBar().setElevation(0);

        if(Conectividade.Conectividade(this)) {

            Tarefa tarefa = new Tarefa();
            tarefa.execute("https://api.themoviedb.org/3/discover/movie?api_key=a9b4aa2bd7b0cdc0e0d9a9d837537d22&language=pt-BR");

            recyclerFilmes.addOnItemTouchListener(
                    new RecyclerItemClickListener(getApplicationContext(), recyclerFilmes, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            if (position % anuncio == 0 && position != 0) {
                                startActivity(new Intent(getApplicationContext(), FilmesAnuncioActivity.class));
                            } else {
                                filmeSelecionado = filmes.get(position);

                                Intent intent = new Intent(getApplicationContext(), DetalhesActivity.class);
                                intent.putExtra("filmeSelecionado", filmeSelecionado);

                                startActivity(intent);
                            }
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
        else{
            recyclerFilmes.setVisibility(View.INVISIBLE);
            maisFilmes.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
            semConexao.setVisibility(View.VISIBLE);
            semConexaoText.setVisibility(View.VISIBLE);
        }
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

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerFilmes.setLayoutManager(layoutManager);
        recyclerFilmes.setHasFixedSize(true);

        adapter = new AdapterFilmes(filmes, true);
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