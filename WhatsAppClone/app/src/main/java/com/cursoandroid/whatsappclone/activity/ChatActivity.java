package com.cursoandroid.whatsappclone.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.cursoandroid.whatsappclone.adapter.AdapterMensagens;
import com.cursoandroid.whatsappclone.config.ConfigFirebase;
import com.cursoandroid.whatsappclone.helper.Base64Custom;
import com.cursoandroid.whatsappclone.helper.Permissoes;
import com.cursoandroid.whatsappclone.helper.UsuarioFirebase;
import com.cursoandroid.whatsappclone.model.Conversa;
import com.cursoandroid.whatsappclone.model.Grupo;
import com.cursoandroid.whatsappclone.model.Mensagem;
import com.cursoandroid.whatsappclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cursoandroid.whatsappclone.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private TextView txtNomeContato;
    private EditText txtMensagem;
    private CircleImageView imgFotoContato;
    private Usuario usuarioDestinatario;
    private Usuario usuarioRemetente;
    private RecyclerView recyclerMensagens;
    private AdapterMensagens adapter;
    private List<Mensagem> listMensagens = new ArrayList<>();
    private DatabaseReference database = ConfigFirebase.getFirebaseDatabse();
    private DatabaseReference mensagemRef;
    private StorageReference storage = ConfigFirebase.getStorage();
    private ChildEventListener childEventListener;
    private Grupo grupo;

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

        //Valida permissões
        Permissoes.validarPermissoes(permissoesNecessarias, this, 1);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //recupera dados do usuario remetente
        idRemetente = UsuarioFirebase.getIdentificadorUsuario();
        usuarioRemetente = UsuarioFirebase.getDadosUsuarioLogado();

        //recuperar os dados do usuário destinatário
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            if(bundle.containsKey("chatGrupo")){
                grupo = (Grupo) bundle.getSerializable("chatGrupo");
                txtNomeContato.setText(grupo.getNome());

                idDestinatario = grupo.getId();

                if (grupo.getFoto() != null) {
                    Uri url = Uri.parse(grupo.getFoto());

                    Glide.with(getApplicationContext()).load(url).into(imgFotoContato);
                } else {
                    imgFotoContato.setImageResource(R.drawable.undraw_group_selfie_ijc6);
                }
            }
            else {
                usuarioDestinatario = (Usuario) bundle.getSerializable("chatContato");
                txtNomeContato.setText(usuarioDestinatario.getNome());

                //recupera dados do usuario destinatário
                idDestinatario = Base64Custom.codificarBase64(usuarioDestinatario.getEmail());

                if (usuarioDestinatario.getFoto() != null) {
                    Uri url = Uri.parse(usuarioDestinatario.getFoto());

                    Glide.with(getApplicationContext()).load(url).into(imgFotoContato);
                } else {
                    imgFotoContato.setImageResource(R.drawable.profile);
                }
            }
        }

        mensagemRef = database.child("mensagens");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Bitmap imagem = null;

            try{
                switch(requestCode){
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");

                        break;

                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();

                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);

                        break;
                }

                if(imagem != null){
                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.PNG, 75, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Criar nome imagem
                    String nomeImagem = UUID.randomUUID().toString();

                    //Configurar referencia do firebase
                    final StorageReference imagemRef = storage.child("fotos").child("chat").child(idRemetente).child(nomeImagem+ ".png");

                    //Retorna objeto que irá controlar o upload
                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);

                    uploadTask.addOnFailureListener(ChatActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatActivity.this, "Upload da imagem falhou: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(ChatActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Uri url = taskSnapshot.getDownloadUrl();
                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();

                                    if(usuarioDestinatario != null){//Mensagem normal
                                        Mensagem mensagem = new Mensagem();

                                        mensagem.setIdUsuario(idRemetente);
                                        mensagem.setMensagem("imagem.png");
                                        mensagem.setImagem(url.toString());

                                        salvarMensagem(idRemetente, idDestinatario, mensagem);
                                        salvarMensagem(idDestinatario, idRemetente, mensagem);
                                    }
                                    else{//Mensagem em grupo
                                        for(Usuario integrante : grupo.getIntegrantes()){
                                            String idRemetenteGrupo = Base64Custom.codificarBase64(integrante.getEmail());
                                            String idUsuarioLogadoGrupo = UsuarioFirebase.getIdentificadorUsuario();

                                            Mensagem mensagem = new Mensagem();
                                            mensagem.setIdUsuario(idUsuarioLogadoGrupo);
                                            mensagem.setMensagem("imagem.png");
                                            mensagem.setNome(usuarioRemetente.getNome());
                                            mensagem.setImagem(url.toString());

                                            //salvar mensagem para integrante do grupo
                                            salvarMensagem(idRemetenteGrupo, idDestinatario, mensagem);

                                            //Salvar conversa
                                            salvarConversa(idRemetenteGrupo, idDestinatario, usuarioDestinatario, mensagem, true);
                                        }
                                    }
                                }
                            });

                            Toast.makeText(ChatActivity.this, "Sucesso ao fazer upload", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissaoResultado : grantResults){
            if(permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configurar AlertDialog
        alertDialog.setTitle("Permissões Negadas");
        alertDialog.setMessage("Para utilizar o app é necessário aceitar as permissões");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void alertCameraGaleria(View view){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configurar AlertDialog
        alertDialog.setTitle("Selecionar foto de perfil");
        alertDialog.setMessage("Escolha uma das opções abaixo para escolher uma foto de perfil");
        alertDialog.setCancelable(true);

        alertDialog.setPositiveButton("Câmera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if(intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, SELECAO_CAMERA);
                }
            }
        });

        alertDialog.setNegativeButton("Galeria", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                if(intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, SELECAO_GALERIA);
                }
            }
        });

        final AlertDialog alert = alertDialog.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.verdin));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.verdin));
            }
        });
        alert.show();
    }

    public void enviarMensagem(View view){
        String textoMensagem = txtMensagem.getText().toString();

        if(!textoMensagem.isEmpty()){
            if(usuarioDestinatario != null) {
                Mensagem mensagem = new Mensagem();

                mensagem.setIdUsuario(idRemetente);
                mensagem.setMensagem(textoMensagem.trim());

                //Salvar mensagem para o remetente
                salvarMensagem(idRemetente, idDestinatario, mensagem);

                //Salvar mensagem para o destinatario
                salvarMensagem(idDestinatario, idRemetente, mensagem);

                //Salvar Conversa remetente
                salvarConversa(idRemetente, idDestinatario, usuarioDestinatario, mensagem, false);

                //Salvar Conversa destinatario
                salvarConversa(idDestinatario, idRemetente, usuarioRemetente, mensagem, false);
            }
            else{
                for(Usuario integrante : grupo.getIntegrantes()){
                    String idRemetenteGrupo = Base64Custom.codificarBase64(integrante.getEmail());
                    String idUsuarioLogadoGrupo = UsuarioFirebase.getIdentificadorUsuario();

                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioLogadoGrupo);
                    mensagem.setMensagem(textoMensagem);
                    mensagem.setNome(usuarioRemetente.getNome());

                    //salvar mensagem para integrante do grupo
                    salvarMensagem(idRemetenteGrupo, idDestinatario, mensagem);

                    //Salvar conversa
                    salvarConversa(idRemetenteGrupo, idDestinatario, usuarioDestinatario, mensagem, true);
                }
            }

            //Limpar texto
            txtMensagem.setText("");
        }else{
            Toast.makeText(this, "Digite uma mensagem!", Toast.LENGTH_SHORT).show();
        }
    }

    private void salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem){
        mensagemRef.child(idRemetente).child(idDestinatario).push().setValue(mensagem);
    }

    private void salvarConversa(String idRemetente, String idDestinatario, Usuario usuarioExibicao, Mensagem mensagem, boolean isGroup){
        Conversa conversaRemetente = new Conversa();
        conversaRemetente.setIdRemetente(idRemetente);
        conversaRemetente.setIdDestinatario(idDestinatario);
        conversaRemetente.setUltimaMensagem(mensagem.getMensagem());

        if(isGroup){
            conversaRemetente.setIsGroup("true");
            conversaRemetente.setGrupo(grupo);
        }
        else {
            conversaRemetente.setIsGroup("false");
            conversaRemetente.setUsuarioExibicao(usuarioExibicao);
        }

        conversaRemetente.salvar();
    }

    private void recuperarMensagens(){
        listMensagens.clear();

        childEventListener = mensagemRef.child(idRemetente).child(idDestinatario).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Mensagem mensagem = snapshot.getValue(Mensagem.class);

                listMensagens.add(mensagem);

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