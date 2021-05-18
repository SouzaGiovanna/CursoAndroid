package com.cursoandroid.instagramclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cursoandroid.instagramclone.R;
import com.cursoandroid.instagramclone.config.ConfigFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {
    private TextView txtSemConta;
    private EditText edtEmail, edtSenha;
    private String email, senha;
    private ProgressBar progressLogin;
    private FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado();

        txtSemConta = findViewById(R.id.txtSemConta);
        edtEmail = findViewById(R.id.edtEmailLogin);
        edtSenha = findViewById(R.id.edtSenhaLogin);
        progressLogin = findViewById(R.id.progressEntrar);

        txtSemConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
            }
        });
    }

    private void verificarUsuarioLogado(){
        if(autenticacao.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    public void fazerLogin(View view){
        progressLogin.setVisibility(View.VISIBLE);

        if(verificarText()){
            autenticacao.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                    else{
                        msgLoginErro(excessoes(task)).show();

                        progressLogin.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
        else{
            msgLoginErro("").show();

            progressLogin.setVisibility(View.INVISIBLE);
        }
    }

    private boolean verificarText(){
        email = edtEmail.getText().toString();
        senha = edtSenha.getText().toString();

        if(email.isEmpty() || email.equals(" ") || senha.isEmpty() || senha.equals(" ")){
            return false;
        }

        return true;
    }

    public Toast msgLoginErro(String erro){
        if(erro.equals(" ") || erro.isEmpty()){
            return Toast.makeText(getApplicationContext(), "Preencha todos os campos para continuar", Toast.LENGTH_SHORT);
        }
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