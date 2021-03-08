package com.cursoandroid.whatsappclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.cursoandroid.whatsappclone.R;
import com.cursoandroid.whatsappclone.config.ConfigFirebase;
import com.cursoandroid.whatsappclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtSenha;
    private String email, senha;
    private Button btnLogar;
    private FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();;
    private Usuario usuario;
    private CheckBox cbManterConectado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.txtEmail);
        edtSenha = findViewById(R.id.txtSenha);
        btnLogar = findViewById(R.id.btnLogar);
        cbManterConectado = findViewById(R.id.cbManterConectado);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setarStrings();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();
        if(usuarioAtual != null){
            //Abrir a tela principal
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    public void abrirTelaCadastro(View view){
        startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
    }

    private void setarStrings(){
        email = edtEmail.getText().toString();
        senha = edtSenha.getText().toString();

        verificarTexts();
    }

    private void verificarTexts(){
        if(email.isEmpty() || senha.isEmpty()){
            msgTextVazio().show();
        }
        else{
            setarUsuario();
        }
    }

    private void setarUsuario(){
        usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(senha);

        validarLogin();
    }

    private void validarLogin(){
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Abre a tela principal do app
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                    intent.putExtra("manterConectado", cbManterConectado.isChecked());

                    startActivity(intent);
                }
                else{
                    msgLoginErro(excessoes(task)).show();
                }
            }
        });
    }

    private Toast msgTextVazio(){
        return Toast.makeText(getApplicationContext(), "Preencha todos os campos para continuar", Toast.LENGTH_SHORT);
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