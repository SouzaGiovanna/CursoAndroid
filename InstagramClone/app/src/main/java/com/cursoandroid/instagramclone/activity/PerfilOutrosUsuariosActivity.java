package com.cursoandroid.instagramclone.activity;

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
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cursoandroid.instagramclone.R;
import com.cursoandroid.instagramclone.model.Usuario;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilOutrosUsuariosActivity extends AppCompatActivity {
    private Usuario usuarioSelecionado;
    private Button btnSeguir;
    private CircleImageView imgPerfil;
    private TextView txtSeguidores, txtPublicacoes, txtSeguindo;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_outros_usuarios);

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

        btnSeguir.setText(R.string.seguir);

        preencherDadosUsuarioSelecionado();
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

        if(usuarioSelecionado.getId() != null){
            Log.i("teste", usuarioSelecionado.getId());
        }

        //Carregar a quantidade de seguidores, publicações e pessoas que este usuário segue
        txtPublicacoes.setText(Integer.toString(usuarioSelecionado.getPublicacoes()));
        txtSeguidores.setText(Integer.toString(usuarioSelecionado.getSeguidores()));
        txtSeguindo.setText(Integer.toString(usuarioSelecionado.getSeguindo()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}