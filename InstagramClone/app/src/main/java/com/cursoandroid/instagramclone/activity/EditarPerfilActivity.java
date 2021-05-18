package com.cursoandroid.instagramclone.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import com.cursoandroid.instagramclone.R;
import com.cursoandroid.instagramclone.helper.Permissoes;
import com.cursoandroid.instagramclone.helper.SalvarFotoFirebase;
import com.cursoandroid.instagramclone.helper.UsuarioFirebase;
import com.cursoandroid.instagramclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarPerfilActivity extends AppCompatActivity {
    private String[] permissoesNecessarias = new String[]{
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    };
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private TextView txtAlterarFoto;
    private CircleImageView imgPerfil;
    private TextInputEditText edtNomeUsuario;
    private Usuario usuarioAtual;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle(R.string.editar_perfil);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        //Valida permissões
        Permissoes.validarPermissoes(permissoesNecessarias, this, 1);

        txtAlterarFoto = findViewById(R.id.txt_alterar_foto);
        imgPerfil = findViewById(R.id.img_perfil_editar);
        edtNomeUsuario = findViewById(R.id.edt_nome_usuario_editar);

        txtAlterarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertCameraGaleria();
            }
        });

        recuperarDadosUsuario();
    }

    private void recuperarDadosUsuario(){
        usuarioAtual = UsuarioFirebase.getDadosUsuarioLogado();
        edtNomeUsuario.setText(usuarioAtual.getNome());

        if(usuarioAtual.getFoto() != null && !usuarioAtual.getFoto().isEmpty()){
            Uri uri = Uri.parse(usuarioAtual.getFoto());
            Glide.with(this).load(uri).into(imgPerfil);
        }
        else{
            imgPerfil.setImageResource(R.drawable.raposinha);
        }
    }

    private boolean salvarNomeAlterado(){
        if(!edtNomeUsuario.getText().toString().equals(usuarioAtual.getNome()) && !edtNomeUsuario.getText().toString().isEmpty()){
            UsuarioFirebase.atualizarNomeUsuario(edtNomeUsuario.getText().toString());

            usuarioAtual.setNome(edtNomeUsuario.getText().toString());
            usuarioAtual.atualizar();

            return true;
        } else {
            Toast.makeText(this, "Digite um nome diferente para alterá-lo", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editar_perfil, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.salvar_alteracao_perfil) {
            if(salvarNomeAlterado()) {
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
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

    private void alertCameraGaleria(){
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
        alert.show();
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
                    imgPerfil.setImageBitmap(imagem);

                    SalvarFotoFirebase salvarFoto = new SalvarFotoFirebase("perfil", UsuarioFirebase.getIdUsuario());
                    salvarFoto.salvar(imgPerfil, EditarPerfilActivity.this);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}