package com.cursoandroid.instagramclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cursoandroid.instagramclone.R;
import com.cursoandroid.instagramclone.config.ConfigFirebase;
import com.cursoandroid.instagramclone.model.Postagem;
import com.cursoandroid.instagramclone.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisualizarPostagemActivity extends AppCompatActivity {
    private CircleImageView imgPerfil;
    private TextView txtNomeUsuarioPostagem;
    private ImageView imgPostagem;
    private TextView txtCurtidas;
    private TextView txtLegenda;

    private Usuario usuarioPostagem;
    private Postagem postagem;

    private ValueEventListener valueEventListenerPostagem;

    private DatabaseReference firebase = ConfigFirebase.getFirebaseDatabse();
    private DatabaseReference postagemRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_postagem);

        inicializarComponentes();
        recuperarBundle();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Visualizar Postagem");
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    private void inicializarComponentes(){
        imgPostagem = findViewById(R.id.img_postagem);
        imgPerfil = findViewById(R.id.img_perfil);
        txtNomeUsuarioPostagem = findViewById(R.id.txt_nome_usuario_postagem);
        txtCurtidas = findViewById(R.id.txt_curtidas);
        txtLegenda = findViewById(R.id.txt_legenda_visualizacao_publicacao);
    }

    private void recuperarBundle(){
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            usuarioPostagem = (Usuario) bundle.getSerializable("usuario");
            recuperarPostagem((String) bundle.getSerializable("postagem"));
        }
    }

    private void recuperarPostagem(String id){
        postagemRef = firebase.child("postagens").child(usuarioPostagem.getId()).child(id);
        valueEventListenerPostagem = postagemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postagem = snapshot.getValue(Postagem.class);
                preencherDados();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void preencherDados(){
        txtNomeUsuarioPostagem.setText(usuarioPostagem.getNome());
        txtLegenda.setText(postagem.getDescricao());

        carregarImagens(usuarioPostagem.getFoto(), imgPerfil);
        carregarImagens(postagem.getFoto(), imgPostagem);

        //Log.i("teste", usuarioPostagem.getFoto());
    }

    private void carregarImagens(String urlImagem, CircleImageView imgview){
        if(urlImagem != null && !urlImagem.isEmpty()){
            Uri uri = Uri.parse(urlImagem);
            Glide.with(this).load(uri).into(imgview);
        } else {
            imgview.setImageResource(R.drawable.raposinha);
        }
    }

    private void carregarImagens(String urlImagem, ImageView imgview){
        if(urlImagem != null && !urlImagem.isEmpty()){
            Uri uri = Uri.parse(urlImagem);
            Glide.with(this).load(uri).into(imgview);
        } else {
            imgview.setImageResource(R.drawable.raposinha);
        }
    }
}