package com.cursoandroid.cardview.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.cursoandroid.cardview.R;
import com.cursoandroid.cardview.adapter.PostagemAdapter;
import com.cursoandroid.cardview.model.Postagem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerPostagem;
    private List<Postagem> postagens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerPostagem = findViewById(R.id.recyclerView);
        postagens = new ArrayList<>();

        prepararPostagens();

        //Define layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerPostagem.setLayoutManager(layoutManager);

        //Define com.cursoandroid.cardview.adapter
        PostagemAdapter adapter = new PostagemAdapter(postagens);
        recyclerPostagem.setAdapter(adapter);
    }

    public void prepararPostagens(){
        Postagem postagem = new Postagem("Alexandre de Lima", "Amor da minha vida", R.drawable.teste);
        Postagem postagem2 = new Postagem("Elisabete Alves", "Minhas filhas e sobrinho ♥", R.drawable.screenshot_20190823201542);
        Postagem postagem3 = new Postagem("Alexandre de Lima", "Amor da minha vida", R.drawable.a00082013);
        Postagem postagem4 = new Postagem("Alexandre de Lima", "Amor da minha vida", R.drawable.onca);

        postagens.add(postagem);
        postagens.add(postagem2);
        postagens.add(postagem3);
        postagens.add(postagem4);
    }
}