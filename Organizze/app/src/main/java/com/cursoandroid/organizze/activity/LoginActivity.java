package com.cursoandroid.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cursoandroid.organizze.R;
import com.cursoandroid.organizze.config.ConfiguracaoFirebase;
import com.cursoandroid.organizze.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnEntrar;
    private String senha;
    private String email;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnEntrar = findViewById(R.id.btnEntrar);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Definindo as Strings
                email = edtEmail.getText().toString();
                senha = edtSenha.getText().toString();

                if(verificarText(email, senha)){
                    usuario = new Usuario();
                    usuario.setEmail(email);
                    usuario.setSenha(senha);

                    validarLogin();
                }
                else{ msgCampoVazio().show(); }
            }
        });
    }

    public boolean verificarText(String email, String senha){
        if(email.isEmpty()){
            return false;
        }
        if(senha.isEmpty()){
            return false;
        }

        return true;
    }

    public Toast msgCampoVazio(){
        return Toast.makeText(getApplicationContext(), "Preencha todos os campos para continuar", Toast.LENGTH_SHORT);
    }

    public void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    msgLoginSucesso().show();
                }
                else{
                    msgLoginErro(excessoes(task)).show();
                }
            }
        });
    }

    public Toast msgLoginSucesso(){
        return Toast.makeText(getApplicationContext(), "Sucesso ao logar usuário", Toast.LENGTH_SHORT);
    }

    public Toast msgLoginErro(String erro){
        return Toast.makeText(getApplicationContext(), erro, Toast.LENGTH_SHORT);
    }

    public String excessoes(@NonNull Task<AuthResult> task){
        String excecao = "";
        try{
            throw task.getException();
        } catch (FirebaseAuthInvalidUserException e) {
            excecao = "Usuário não cadastrado";
        } catch (FirebaseAuthInvalidCredentialsException e){
            excecao = "E-mail e senha não correspondem a um usuário cadastrado";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return excecao;
    }
}