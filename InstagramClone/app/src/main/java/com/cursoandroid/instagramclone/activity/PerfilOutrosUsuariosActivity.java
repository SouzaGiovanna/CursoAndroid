package com.cursoandroid.instagramclone.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.widget.GridView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cursoandroid.instagramclone.R;
import com.cursoandroid.instagramclone.adapter.AdapterPublicacoes;
import com.cursoandroid.instagramclone.config.ConfigFirebase;
import com.cursoandroid.instagramclone.helper.UsuarioFirebase;
import com.cursoandroid.instagramclone.model.Postagem;
import com.cursoandroid.instagramclone.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilOutrosUsuariosActivity extends AppCompatActivity {
    private String idUsuarioLogado = UsuarioFirebase.getIdUsuario();

    private Usuario usuarioSelecionado, usuarioLogado;
    private Button btnSeguir;
    private CircleImageView imgPerfil;
    private TextView txtSeguidores, txtPublicacoes, txtSeguindo;
    private GridView gridPublicacoes;

    private DatabaseReference firebase = ConfigFirebase.getFirebaseDatabse();
    private DatabaseReference usuarioRef, usuarioLogadoRef, seguidoresRef, postagensRef;

    private ValueEventListener valueEventListenerDadosUsuario, valueEventListenerDadosUsuarioLogado;
    private AdapterPublicacoes adapterPublicacoes;

    private List<Postagem> postagens = new ArrayList<>();
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_outros_usuarios);

        recuperarBundle();

        // Configurações da Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle(usuarioSelecionado.getNome());
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        // Configurações dos elementos da página
        btnSeguir = findViewById(R.id.btn_perfil);
        imgPerfil = findViewById(R.id.img_perfil);
        txtPublicacoes = findViewById(R.id.txt_qtd_publicacoes);
        txtSeguidores = findViewById(R.id.txt_qtd_seguidores);
        txtSeguindo = findViewById(R.id.txt_qtd_pessoas_seguindo);
        gridPublicacoes = findViewById(R.id.grid_publicacoes);

        usuarioRef = firebase.child("usuarios");
        seguidoresRef = firebase.child("seguidores");

        preencherDadosUsuarioSelecionado();
    }

    private void inicializarImageLoader(){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).memoryCache(new LruMemoryCache(2 * 1024 * 1024)).memoryCacheSize(2 * 1024 * 1024).diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100).diskCacheFileNameGenerator(new HashCodeFileNameGenerator()).build();
        ImageLoader.getInstance().init(config);
    }

    private void carregarFotosPostagem(){
        postagensRef = firebase.child("postagens").child(usuarioSelecionado.getId());

        postagensRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Configurar o tamanho da GridView
                int tamanhoGrid = getResources().getDisplayMetrics().widthPixels;
                int tamanhoImagem = tamanhoGrid / 3;
                gridPublicacoes.setColumnWidth(tamanhoImagem);

                List<String> urlFotos = new ArrayList<>();
                for(DataSnapshot data : snapshot.getChildren()){
                    Postagem postagem = data.getValue(Postagem.class);
                    postagens.add(postagem);
                    urlFotos.add(postagem.getFoto());
                }

                //Log.i("teste", String.valueOf(urlFotos));

                adapterPublicacoes = new AdapterPublicacoes(getApplicationContext(), R.layout.adapter_publicacoes, urlFotos);
                gridPublicacoes.setAdapter(adapterPublicacoes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void abrirPostagemSelecionada(){
        gridPublicacoes.setOnItemClickListener((adapterView, view, i, l) -> {
            Postagem postagem = postagens.get(i);

            Intent intent = new Intent(getApplicationContext(), VisualizarPostagemActivity.class);

            //Log.i("teste", String.valueOf(postagem.getDescricao()));
            intent.putExtra("postagem", postagem.getId());
            intent.putExtra("usuario", usuarioSelecionado);

            startActivity(intent);
        });
    }

    private void recuperarDadosUsuarioLogado(){
        usuarioLogadoRef = usuarioRef.child(idUsuarioLogado);

        usuarioLogadoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuarioLogado = snapshot.getValue(Usuario.class);

                verificaSegueOutroUsuario();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void verificaSegueOutroUsuario(){
        DatabaseReference seguidorRef = seguidoresRef.child(idUsuarioLogado).child(usuarioSelecionado.getId());

        seguidorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                habilitarBotaoSeguir(snapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void habilitarBotaoSeguir(boolean segueUsuario){
        if(segueUsuario){
            btnSeguir.setText(R.string.seguindo);
            btnSeguir.setClickable(false);
        }
        else{
            btnSeguir.setText(R.string.seguir);

            //Adiciona evento para seguir usuário
            btnSeguir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    salvarSeguidor(usuarioLogado, usuarioSelecionado);
                }
            });
        }
    }

    private void salvarSeguidor(Usuario usuarioLogado, Usuario usuarioSelecionado){
        HashMap<String, Object> dadosUsuarioSelecionado = new HashMap<>();
        dadosUsuarioSelecionado.put("nome", usuarioSelecionado.getNome());
        dadosUsuarioSelecionado.put("foto", usuarioSelecionado.getFoto());

        DatabaseReference seguidorRef = seguidoresRef.child(usuarioLogado.getId()).child(usuarioSelecionado.getId());
        seguidorRef.setValue(dadosUsuarioSelecionado);

        habilitarBotaoSeguir(true);

        //Incrementar seguindo do usuário logado
        int qtdSeguindo = usuarioLogado.getSeguindo() + 1;

        HashMap<String, Object> dadosUsuarioSeguindo = new HashMap<>();
        dadosUsuarioSeguindo.put("seguindo", qtdSeguindo);

        //Atualizando os dados
        DatabaseReference usuarioSeguindoRef = usuarioRef.child(usuarioLogado.getId());
        usuarioSeguindoRef.updateChildren(dadosUsuarioSeguindo);

        //Incrementar seguidores do outro usuário
        int qtdSeguidores = usuarioSelecionado.getSeguidores() + 1;

        HashMap<String, Object> dadosUsuarioSeguidores = new HashMap<>();
        dadosUsuarioSeguidores.put("seguidores", qtdSeguidores);

        //Atualizando os dados
        DatabaseReference usuarioSelecionadoRef = usuarioRef.child(usuarioSelecionado.getId());
        usuarioSelecionadoRef.updateChildren(dadosUsuarioSeguidores);
    }

    @Override
    protected void onStart() {
        recuperarDadosUsuario();
        recuperarDadosUsuarioLogado();
        inicializarImageLoader();
        carregarFotosPostagem();
        abrirPostagemSelecionada();
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        usuarioRef.removeEventListener(valueEventListenerDadosUsuario);
    }

    @SuppressLint("SetTextI18n")
    private void preencherDadosUsuarioSelecionado(){
        //Carregar foto de perfil
        if(usuarioSelecionado.getFoto() != null && !usuarioSelecionado.getFoto().isEmpty()){
            Uri uri = Uri.parse(usuarioSelecionado.getFoto());
            Glide.with(this).load(uri).into(imgPerfil);
        }
        else{
            imgPerfil.setImageResource(R.drawable.raposinha);
        }
    }

    private void recuperarDadosUsuario(){
        valueEventListenerDadosUsuario = usuarioRef.child(usuarioSelecionado.getId()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);

                //Carregar a quantidade de seguidores, publicações e pessoas que este usuário segue
                txtPublicacoes.setText(Integer.toString(usuario.getPublicacoes()));
                txtSeguidores.setText(Integer.toString(usuario.getSeguidores()));
                txtSeguindo.setText(Integer.toString(usuario.getSeguindo()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    private void recuperarBundle(){
        //recuperar os dados do usuário selecionado
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            usuarioSelecionado = (Usuario) bundle.getSerializable("usuarioSelecionado");
        } else {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            //Configurar AlertDialog
            alertDialog.setTitle("Ocorreu um erro");
            alertDialog.setMessage("Ocorreu um erro ao carregar o usuário selecionado, tente novamente mais tarde!");
            alertDialog.setCancelable(false);

            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });

            final AlertDialog alert = alertDialog.create();
            alert.show();
        }
    }
}