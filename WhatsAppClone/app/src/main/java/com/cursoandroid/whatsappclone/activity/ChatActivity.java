package com.cursoandroid.whatsappclone.activity;

import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.cursoandroid.whatsappclone.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import com.cursoandroid.whatsappclone.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private TextView txtNomeContato;
    private CircleImageView imgFotoContato;
    private Usuario usuarioDestinatario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.include);
        txtNomeContato = findViewById(R.id.nome_contato);
        imgFotoContato = findViewById(R.id.foto_contato);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //recuperar os dados do usuário destinatário
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            usuarioDestinatario = (Usuario) bundle.getSerializable("contato");
            txtNomeContato.setText(usuarioDestinatario.getNome());

            Uri url = Uri.parse(usuarioDestinatario.getFoto());

            if (url != null) {
                Glide.with(getApplicationContext()).load(url).into(imgFotoContato);
            }
            else{
                imgFotoContato.setImageResource(R.drawable.profile);
            }
        }
    }
}