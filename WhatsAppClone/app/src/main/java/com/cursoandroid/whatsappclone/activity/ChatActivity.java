package com.cursoandroid.whatsappclone.activity;

import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.cursoandroid.whatsappclone.adapter.AdapterMensagens;
import com.cursoandroid.whatsappclone.config.ConfigFirebase;
import com.cursoandroid.whatsappclone.helper.Base64Custom;
import com.cursoandroid.whatsappclone.helper.UsuarioFirebase;
import com.cursoandroid.whatsappclone.model.Mensagem;
import com.cursoandroid.whatsappclone.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cursoandroid.whatsappclone.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private TextView txtNomeContato;
    private EditText txtMensagem;
    private CircleImageView imgFotoContato;
    private Usuario usuarioDestinatario;
    private RecyclerView recyclerMensagens;
    private AdapterMensagens adapter;
    private List<Mensagem> listMensagens = new ArrayList<>();
    private DatabaseReference database = ConfigFirebase.getFirebaseDatabse();
    private DatabaseReference mensagemRef;
    private DatabaseReference mensagemRefDestinatario;
    private ChildEventListener childEventListener;

    //Identificador remetende e destinatário
    private String idRemetente;
    private String idDestinatario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.include);
        txtNomeContato = findViewById(R.id.nome_contato);
        txtMensagem = findViewById(R.id.txtMensagem);
        imgFotoContato = findViewById(R.id.foto_contato);
        recyclerMensagens = findViewById(R.id.recyclerMensagens);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //recupera dados do usuario remetente
        idRemetente = UsuarioFirebase.getIdentificadorUsuario();

        //recuperar os dados do usuário destinatário
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            usuarioDestinatario = (Usuario) bundle.getSerializable("contato");
            txtNomeContato.setText(usuarioDestinatario.getNome());

            //recupera dados do usuario destinatário
            idDestinatario = Base64Custom.codificarBase64(usuarioDestinatario.getEmail());

            if (usuarioDestinatario.getFoto() != null) {
                Uri url = Uri.parse(usuarioDestinatario.getFoto());

                Glide.with(getApplicationContext()).load(url).into(imgFotoContato);
            }
            else{
                imgFotoContato.setImageResource(R.drawable.profile);
            }
        }

        mensagemRef = database.child("mensagens").child(idRemetente).child(idDestinatario);
        mensagemRefDestinatario = database.child("mensagens").child(idDestinatario).child(idRemetente);
    }

    @Override
    protected void onStart() {
        super.onStart();
        configurarRecycler();
        recuperarMensagens();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mensagemRef.removeEventListener(childEventListener);
    }

    public void enviarMensagem(View view){
        String textoMensagem = txtMensagem.getText().toString();

        if(!textoMensagem.isEmpty()){
            Mensagem mensagem = new Mensagem();

            mensagem.setIdUsuario(idRemetente);
            mensagem.setMensagem(textoMensagem.trim());

            //Salvar mensagem para o remetente
            salvarMensagem(mensagem);

            //Limpar texto
            txtMensagem.setText("");
        }else{
            Toast.makeText(this, "Digite uma mensagem!", Toast.LENGTH_SHORT).show();
        }
    }

    private void salvarMensagem(Mensagem mensagem){
        mensagemRef.push().setValue(mensagem);
        mensagemRefDestinatario.push().setValue(mensagem);
    }

    private void recuperarMensagens(){
        childEventListener = mensagemRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Mensagem mensagem = snapshot.getValue(Mensagem.class);

                listMensagens.add(mensagem);

                Log.i("teste", mensagem.toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configurarRecycler(){
        //Configuração adapter
        adapter = new AdapterMensagens(listMensagens, getApplicationContext());

        //Configuração recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMensagens.setLayoutManager(layoutManager);
        recyclerMensagens.setHasFixedSize(true);
        recyclerMensagens.setVerticalScrollBarEnabled(true);
        recyclerMensagens.setAdapter(adapter);
    }
}