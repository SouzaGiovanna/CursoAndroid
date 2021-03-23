package com.cursoandroid.whatsappclone.activity;

import android.os.Bundle;

import com.cursoandroid.whatsappclone.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.cursoandroid.whatsappclone.R;

import java.util.ArrayList;
import java.util.List;

public class ConfigGrupoCriacaoActivity extends AppCompatActivity {
    private List<Usuario> integrantes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_grupo_criacao);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Novo Grupo");
        getSupportActionBar().setSubtitle("Adicionar nome");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if(getIntent().getExtras() != null){
            integrantes = (List<Usuario>) getIntent().getExtras().getSerializable("integrantes");
        }
    }
}