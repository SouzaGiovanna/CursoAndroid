package com.cursoandroid.whatsappclone.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.cursoandroid.whatsappclone.adapter.AdapterMembrosGrupo;
import com.cursoandroid.whatsappclone.config.ConfigFirebase;
import com.cursoandroid.whatsappclone.helper.Permissoes;
import com.cursoandroid.whatsappclone.helper.UsuarioFirebase;
import com.cursoandroid.whatsappclone.model.Grupo;
import com.cursoandroid.whatsappclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cursoandroid.whatsappclone.R;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfigGrupoCriacaoActivity extends AppCompatActivity {
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private List<Usuario> listIntegrantes;
    private TextView txtTotalParticipantes;
    private EditText edtNomeGrupo;
    private CircleImageView imgGrupo;
    private RecyclerView recyclerIntegrantesGrupo;
    private AdapterMembrosGrupo adapterMembrosGrupo;
    private StorageReference storage = ConfigFirebase.getStorage();
    private StorageReference imagemRef;
    private Grupo grupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_grupo_criacao);

        //Valida permissões
        Permissoes.validarPermissoes(permissoesNecessarias, this, 1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Novo Grupo");
        getSupportActionBar().setSubtitle("Adicionar nome");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtTotalParticipantes = findViewById(R.id.txtTotalParticipantes);
        edtNomeGrupo = findViewById(R.id.edtNomeGrupo);
        imgGrupo = findViewById(R.id.imgGrupo);
        recyclerIntegrantesGrupo = findViewById(R.id.recyclerIntegrantesGrupo);
        grupo = new Grupo();

        FloatingActionButton fab = findViewById(R.id.fabSalvarGrupo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarGrupo();

                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("chatGrupo", grupo);
                startActivity(intent);
            }
        });

        if(getIntent().getExtras() != null){
            listIntegrantes = (List<Usuario>) getIntent().getExtras().getSerializable("integrantes");
            txtTotalParticipantes.setText("Participantes: " +listIntegrantes.size());
        }

        configurarRecycler();

        imgGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertCameraGaleria();
            }
        });
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
                    imgGrupo.setImageBitmap(imagem);

                    salvarImgBanco();
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

    public void alertCameraGaleria(){
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

    private void configurarRecycler(){
        adapterMembrosGrupo = new AdapterMembrosGrupo(listIntegrantes, getApplicationContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerIntegrantesGrupo.setLayoutManager(layoutManager);
        recyclerIntegrantesGrupo.setHasFixedSize(true);
        recyclerIntegrantesGrupo.setAdapter(adapterMembrosGrupo);
    }

    private void salvarImgBanco(){
        //Configura para imagem ser salva em memória
        imgGrupo.setDrawingCacheEnabled(true);
        imgGrupo.buildDrawingCache();

        //Recupera bitmap da imagem (imagem a ser carregada)
        Bitmap bitmap = imgGrupo.getDrawingCache();

        //Comprimo bitmap para um formato png/jpg
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 75, baos);

        //converte o baos para pixel brutos em uma matriz de bytes
        //(dados da imagem)
        byte[] dadosImagem = baos.toByteArray();

        imagemRef = storage.child("fotos").child("grupo").child(grupo.getId()+ ".png");

        //Retorna objeto que irá controlar o upload
        UploadTask uploadTask = imagemRef.putBytes(dadosImagem);

        uploadTask.addOnFailureListener(ConfigGrupoCriacaoActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ConfigGrupoCriacaoActivity.this, "Upload da imagem falhou: " +e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(ConfigGrupoCriacaoActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Uri url = taskSnapshot.getDownloadUrl();

                Toast.makeText(ConfigGrupoCriacaoActivity.this, "Sucesso ao fazer upload", Toast.LENGTH_LONG).show();

                imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String url = task.getResult().toString();
                        grupo.setFoto(url);
                    }
                });
            }
        });
    }

    private void salvarGrupo(){
        String nomeGrupo = edtNomeGrupo.getText().toString();

        if(!nomeGrupo.isEmpty()){
            //adiciona à lista de membros o usuário que está logado
            listIntegrantes.add(UsuarioFirebase.getDadosUsuarioLogado());

            grupo.setIntegrantes(listIntegrantes);
            grupo.setNome(nomeGrupo);
            grupo.salvar();
        }
        else{
            Toast.makeText(this, "Dê um nome ao grupo", Toast.LENGTH_SHORT).show();
        }
    }
}