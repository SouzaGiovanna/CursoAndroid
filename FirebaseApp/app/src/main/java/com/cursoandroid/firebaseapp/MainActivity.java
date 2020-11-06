package com.cursoandroid.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference("");
    private FirebaseAuth usuario = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //usuario.signOut();

        /*usuario.signInWithEmailAndPassword("gi.souza03@gmail.com", "gi12345")
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

        if(usuario.getCurrentUser() != null){
            Log.i("CurrentUser", "Usuario logado");
        }
        else{
            Log.i("CurrentUser", "Usuario nao logado");
        }

        //referencia.child("usuarios").child("003").child("nome").setValue("Kanade");
        //referencia.child("usuarios").child("003").child("sobrenome").setValue("Tachibana");

        DatabaseReference usuarios = referencia.child("usuarios");
        //DatabaseReference usuarioPesquisa = usuarios.child("-MLSQEboD4wpH-vvNbA0");
        Query usuarioPesquisa = usuarios.orderByChild("nome").equalTo("Dimitri");
        //Query usuarioPesquisa = usuarios.orderByKey().limitToFirst(2);

        usuarioPesquisa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*Usuario dadosUsuario = dataSnapshot.getValue(Usuario.class);
                Log.i("Dados usuario ", "nome: " +dadosUsuario.getNome());*/
                Log.i("Dados usuario", dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Usuario usuario = new Usuario();
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