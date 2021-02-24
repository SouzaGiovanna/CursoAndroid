package com.cursoandroid.whatsappclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cursoandroid.whatsappclone.Base64Custom;
import com.cursoandroid.whatsappclone.R;
import com.cursoandroid.whatsappclone.config.ConfigFirebase;
import com.cursoandroid.whatsappclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {
    private EditText edtNome, edtEmail, edtSenha;
    private FirebaseAuth autenticacao;
    private Usuario usuario;
    private String nome, email, senha;
    private Button btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        edtNome = findViewById(R.id.edt_nome);
        edtEmail = findViewById(R.id.edt_email);
        edtSenha = findViewById(R.id.edt_senha);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setarStrings();
            }
        });
    }

    private void setarStrings(){
        nome = edtNome.getText().toString();
        email = edtEmail.getText().toString();
        senha = edtSenha.getText().toString();

        verificarText();
    }

    private void verificarText(){
        if(nome.isEmpty() || email.isEmpty() || senha.isEmpty()){
            msgCamposVazios().show();
        }
        else{
            setarUsuario();
        }
    }

    private void setarUsuario(){
        usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);

        cadastrarUsuario();
    }

    private void cadastrarUsuario(){
        autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String id = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setId(id);
                    usuario.salvar();

                    finish();
                }
                else{
                    msgCadastroErro(excessoes(task)).show();
                }
            }
        });
    }

    public String excessoes(@NonNull Task<AuthResult> task){
        String excecao = "";
        try{
            throw task.getException();
        } catch (FirebaseAuthWeakPasswordException e) {
            excecao = "Digite uma senha mais forte!";
        } catch (FirebaseAuthInvalidCredentialsException e){
            excecao = "Por favor, digite um e-mail valido!";
        } catch (FirebaseAuthUserCollisionException e){
            excecao = "Essa conta já foi cadastrada";
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return excecao;
    }

    private Toast msgCadastroErro(String erro){
        return Toast.makeText(getApplicationContext(), erro, Toast.LENGTH_SHORT);
    }

    private Toast msgCamposVazios(){
        return Toast.makeText(getApplicationContext(), "Preencha todos os campos para continuar", Toast.LENGTH_SHORT);
    }
}