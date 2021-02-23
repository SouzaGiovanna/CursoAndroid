package com.cursoandroid.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference("");
    private FirebaseAuth usuario = FirebaseAuth.getInstance();
    private ImageView imgFoto;
    private Button btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnUpload = findViewById(R.id.btnUpload);
        imgFoto = findViewById(R.id.imgFoto);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Configura para imagem ser salva em memória
                imgFoto.setDrawingCacheEnabled(true);
                imgFoto.buildDrawingCache();

                //Recupera bitmap da imagem (imagem a ser carregada)
                Bitmap bitmap = imgFoto.getDrawingCache();

                //Comprimo bitmap para um formato png/jpg
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 75, baos);

                //converte o baos para pixel brutos em uma matriz de bytes
                //(dados da imagem)
                byte[] dadosImagem = baos.toByteArray();

                //Define nós para storage
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference imagens = storageReference.child("imagens");

                //Nome da imagem
                String nomeArquivo = UUID.randomUUID().toString();
                StorageReference imagemRef = imagens.child("65c9ae24-f981-46ad-918d-a47b7f078365.png");

                Glide.with(MainActivity.this).load(imagemRef).into(imgFoto);

                /*imagemRef.delete().addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Erro ao deletar", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(MainActivity.this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Sucesso ao deletar", Toast.LENGTH_SHORT).show();
                    }
                });*/

                //Retorna objeto que irá controlar o upload
                /*UploadTask uploadTask = imagemRef.putBytes(dadosImagem);

                uploadTask.addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Upload da imagem falhou: " +e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(MainActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Uri url = taskSnapshot.getDownloadUrl();

                        Toast.makeText(MainActivity.this, "Sucesso ao fazer upload", Toast.LENGTH_LONG).show();
                    }
                });*/
            }
        });

        //usuario.signOut();

        usuario.signInWithEmailAndPassword("gi.souza03@gmail.com", "gi12345")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i("SignIn", "Sucesso ao logar usuario");
                }
                else{
                    Log.i("SignIn", "Erro ao logar usuario");
                }
            }
        });

        /*usuario.createUserWithEmailAndPassword("easouza.g@gmail.com", "ea12345")
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.i("CreateUser", "Sucesso ao cadastrar usuario");
                        }
                        else{
                            Log.i("CreateUser", "Erro ao cadastrar usuario");
                        }
                    }
                });*/

        /*if(usuario.getCurrentUser() != null){
            Log.i("CurrentUser", "Usuario logado");
        }
        else{
            Log.i("CurrentUser", "Usuario nao logado");
        }

        //referencia.child("usuarios").child("003").child("nome").setValue("Kanade");
        //referencia.child("usuarios").child("003").child("sobrenome").setValue("Tachibana");

        //DatabaseReference usuarios = referencia.child("usuarios");
        //DatabaseReference usuarioPesquisa = usuarios.child("-MLSQEboD4wpH-vvNbA0");
        //Query usuarioPesquisa = usuarios.orderByChild("nome").equalTo("Dimitri");
        //Query usuarioPesquisa = usuarios.orderByKey().limitToFirst(2);

        /*usuarioPesquisa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*Usuario dadosUsuario = dataSnapshot.getValue(Usuario.class);
                Log.i("Dados usuario ", "nome: " +dadosUsuario.getNome());
                Log.i("Dados usuario", dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/



        /*Usuario usuario = new Usuario();
        usuario.setNome("Dimitri");
        usuario.setSobrenome("Dioginis");
        usuario.setIdade(15);

        usuarios.push().setValue(usuario);

        /*Usuario usuario2 = new Usuario();
        usuario.setNome("Gustavo");
        usuario.setSobrenome("Cardozo");
        usuario.setIdade(17);

        usuarios.push().setValue(usuario2);

        Usuario usuario3 = new Usuario();
        usuario.setNome("Alexandre");
        usuario.setSobrenome("Alves");
        usuario.setIdade(34);

        usuarios.push().setValue(usuario3);

        Usuario usuario4 = new Usuario();
        usuario.setNome("Elisabete");
        usuario.setSobrenome("Aparecida");
        usuario.setIdade(39);

        usuarios.push().setValue(usuario4);

        /*DatabaseReference produtos = referencia.child("produtos");

        usuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("FIREBASE", dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Usuario usuario = new Usuario();
        usuario.setNome("Gustavo");
        usuario.setSobrenome("Cardozo");
        usuario.setIdade(17);

        usuarios.child("005").setValue(usuario);

        Produto produto = new Produto();
        produto.setDescricao("Macbook");
        produto.setMarca("Apple");
        produto.setPreco(1500.99);

        produtos.child("004").setValue(produto);*/
    }
}