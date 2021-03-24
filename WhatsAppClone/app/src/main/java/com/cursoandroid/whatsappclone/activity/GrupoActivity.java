package com.cursoandroid.whatsappclone.activity;

import android.content.Intent;
import android.os.Bundle;

import com.cursoandroid.whatsappclone.adapter.AdapterContatos;
import com.cursoandroid.whatsappclone.adapter.AdapterMembrosGrupo;
import com.cursoandroid.whatsappclone.config.ConfigFirebase;
import com.cursoandroid.whatsappclone.config.RecyclerItemClickListener;
import com.cursoandroid.whatsappclone.helper.UsuarioFirebase;
import com.cursoandroid.whatsappclone.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.cursoandroid.whatsappclone.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrupoActivity extends AppCompatActivity {
    private RecyclerView recyclerContatosSelecionados, recyclerContatos;
    private AdapterContatos adapterContatos;
    private AdapterMembrosGrupo adapterMembrosGrupo;
    private List<Usuario> listContatos, listContatosSelecionados = new ArrayList<>();
    private ValueEventListener valueEventListenerContatos;
    private DatabaseReference usuariosRef;
    private FirebaseUser usuarioAtual = UsuarioFirebase.getUsuarioAtual();
    private int qtdContatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Novo Grupo");
        getSupportActionBar().setSubtitle("0 de 0 selecionados");

        FloatingActionButton fab = findViewById(R.id.fabSalvarGrupo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listContatosSelecionados.size() >= 1){
                    Intent intent = new Intent(getApplicationContext(), ConfigGrupoCriacaoActivity.class);

                    intent.putExtra("integrantes", (Serializable) listContatosSelecionados);

                    startActivity(intent);
                }
                else{
                    Toast.makeText(GrupoActivity.this, "Escolha pelo menos um contato para continuar", Toast.LENGTH_LONG).show();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerContatos = findViewById(R.id.recyclerContatos2);
        recyclerContatosSelecionados = findViewById(R.id.recyclerContatosSelecionados);

        usuariosRef = ConfigFirebase.getFirebaseDatabse().child("usuarios");

        recuperarContatos();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuariosRef.removeEventListener(valueEventListenerContatos);
        finish();
    }

    private void recuperarContatos(){
        listContatos = new ArrayList<>();

        valueEventListenerContatos = usuariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dados : snapshot.getChildren()){
                    Usuario usuario = dados.getValue(Usuario.class);

                    if(!usuarioAtual.getEmail().equals(usuario.getEmail())) {
                        listContatos.add(usuario);
                    }
                }

                adapterContatos.notifyDataSetChanged();

                qtdContatos = listContatos.size();
                mostrarQtdContatosSelecionados();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        configurarRecyclerContatos();
        configurarRecyclerContatosSelecionados();
    }

    private void configurarRecyclerContatos(){
        adapterContatos = new AdapterContatos(listContatos, getApplicationContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerContatos.setLayoutManager(layoutManager);
        recyclerContatos.setHasFixedSize(true);
        recyclerContatos.setAdapter(adapterContatos);

        recyclerContatos.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerContatos, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Usuario usuarioSelecionado = listContatos.get(position);

                //Remover usuario selecionado da lista
                listContatos.remove(usuarioSelecionado);
                adapterContatos.notifyDataSetChanged();

                //Adicionando usuario na nova lista de selecionados
                listContatosSelecionados.add(usuarioSelecionado);
                adapterMembrosGrupo.notifyDataSetChanged();

                mostrarQtdContatosSelecionados();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));
    }

    private void configurarRecyclerContatosSelecionados(){
        adapterMembrosGrupo = new AdapterMembrosGrupo(listContatosSelecionados, getApplicationContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerContatosSelecionados.setLayoutManager(layoutManager);
        recyclerContatosSelecionados.setHasFixedSize(true);
        recyclerContatosSelecionados.setAdapter(adapterMembrosGrupo);

        recyclerContatosSelecionados.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerContatosSelecionados, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Usuario usuarioSelecionado = listContatosSelecionados.get(position);

                //Remover usuario selecionado da lista
                listContatosSelecionados.remove(usuarioSelecionado);
                adapterMembrosGrupo.notifyDataSetChanged();

                //Adicionando usuario na lista de contatos
                listContatos.add(usuarioSelecionado);
                adapterContatos.notifyDataSetChanged();

                mostrarQtdContatosSelecionados();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));
    }

    private void mostrarQtdContatosSelecionados(){
        int qtdMembrosGrupo = listContatosSelecionados.size();

        getSupportActionBar().setSubtitle(qtdMembrosGrupo+ " de " +qtdContatos+ " selecionados");
    }
}