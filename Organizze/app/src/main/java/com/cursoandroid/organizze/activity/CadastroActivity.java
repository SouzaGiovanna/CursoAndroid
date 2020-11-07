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

import static com.cursoandroid.organizze.R.id.btnCadastrar;

public class CadastroActivity extends AppCompatActivity {
    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnCadastrar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;
    private String nome;
    private String email;
    private String senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);



        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Definindo as Strings
                nome = edtNome.getText().toString();
                email = edtEmail.getText().toString();
                senha = edtSenha.getText().toString();

                if(verificarText(nome, email, senha)){
                    usuario = new Usuario();
                    usuario.setNome(nome);
                    usuario.setEmail(email);
                    usuario.setSenha(senha);

                    cadastrarUsuario();
                }
                else{ msgCampoVazio().show(); }
            }
        });
    }

    public boolean verificarText(String nome, String email, String senha){
        if(nome.isEmpty()){
            return false;
        }
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

    public Toast msgCadastroSucesso(){
        return Toast.makeText(getApplicationContext(), "Sucesso ao cadastrar usuário", Toast.LENGTH_SHORT);
    }

    public Toast msgCadastroErro(String erro){
        return Toast.makeText(getApplicationContext(), erro, Toast.LENGTH_SHORT);
    }

    public void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    msgCadastroSucesso().show();
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
}