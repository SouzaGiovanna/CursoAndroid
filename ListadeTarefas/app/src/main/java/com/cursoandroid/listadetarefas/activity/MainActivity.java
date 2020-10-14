package com.cursoandroid.listadetarefas.activity;

import android.content.Intent;
import android.os.Bundle;

import com.cursoandroid.listadetarefas.R;
import com.cursoandroid.listadetarefas.RecyclerItemClickListener;
import com.cursoandroid.listadetarefas.adapter.Adapter;
import com.cursoandroid.listadetarefas.model.Tarefa;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Tarefa> listaTarefas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);

        //listagem de Tarefas
        this.criarTarefas();

        //Configurar Adapter
        Adapter adapter = new Adapter(listaTarefas);

        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setAdapter(adapter);

        //Evento de Click
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Tarefa tarefa = listaTarefas.get(position);
                                Toast.makeText(getApplicationContext(), "Item pressionado: " +tarefa.getTarefa(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                Tarefa tarefa = listaTarefas.get(position);
                                Toast.makeText(getApplicationContext(), "Clique longo: " +tarefa.getTarefa(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        })
        );

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdicionarTarefaActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent com.cursoandroid.listadetarefas.activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void criarTarefas(){
        Tarefa tarefa = new Tarefa("Homem Aranha - De volta ao lar");
        this.listaTarefas.add(tarefa);

        tarefa = new Tarefa("Mulher Maravilha");
        this.listaTarefas.add(tarefa);

        tarefa = new Tarefa("Liga da Justiça");
        this.listaTarefas.add(tarefa);

        tarefa = new Tarefa("Capitão América - Guerra Civil");
        this.listaTarefas.add(tarefa);

        tarefa = new Tarefa("It: A Coisa");
        this.listaTarefas.add(tarefa);

        tarefa = new Tarefa("Pica-Pau: O Filme");
        this.listaTarefas.add(tarefa);

        tarefa = new Tarefa("A Múmia");
        this.listaTarefas.add(tarefa);

        tarefa = new Tarefa("A Bela e A Fera");
        this.listaTarefas.add(tarefa);

        tarefa = new Tarefa("Meu Malvado Favorito 3");
        this.listaTarefas.add(tarefa);

        tarefa = new Tarefa("Carros 3");
        this.listaTarefas.add(tarefa);
    }
}