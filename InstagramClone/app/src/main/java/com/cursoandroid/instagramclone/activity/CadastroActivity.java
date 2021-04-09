package com.cursoandroid.instagramclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cursoandroid.instagramclone.R;
import com.cursoandroid.instagramclone.config.ConfigFirebase;
import com.cursoandroid.instagramclone.helper.Base64Custom;
import com.cursoandroid.instagramclone.helper.UsuarioFirebase;
import com.cursoandroid.instagramclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {
    TextView txtComConta;
    EditText edtNome, edtEmail, edtSenha;
    String nome, email, senha;
    Usuario usuario;
    ProgressBar progressCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        txtComConta = findViewById(R.id.txtComConta);
        edtNome = findViewById(R.id.edtNomeCadastrar);
        edtEmail = findViewById(R.id.edtEmailCadastrar);
        edtSenha = findViewById(R.id.edtSenhaCadastrar);
        progressCadastrar = findViewById(R.id.progressCadastrar);

        txtComConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void setarUsuario(View view){
        progressCadastrar.setVisibility(View.VISIBLE);

        if(verificarCampos()) {
            usuario = new Usuario();

            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setSenha(senha);

            cadastrarUsuario();
        }
        else{
            mensagemErroCadastro("").show();

            progressCadastrar.setVisibility(View.INVISIBLE);
        }
    }

    private boolean verificarCampos(){
        nome = edtNome.getText().toString();
        email = edtEmail.getText().toString();
        senha = edtSenha.getText().toString();

        if(nome.isEmpty() || nome.equals(" ") || email.isEmpty() || email.equals(" ") || senha.isEmpty() || senha.equals(" ")){
            return false;
        }

        return true;
    }

    private Toast mensagemErroCadastro(String erro){
        if(erro.isEmpty()) {
            return Toast.makeText(this, "Verifique se todos os campos estão preenchidos", Toast.LENGTH_LONG);
        }

        return Toast.makeText(this, erro, Toast.LENGTH_LONG);
    }

    private void cadastrarUsuario(){
        FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();

        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String id = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setId(id);
                    usuario.salvar();

                    UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());

                    finish();
                }
                else{
                    mensagemErroCadastro(excessoes(task)).show();

                    progressCadastrar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private String excessoes(@NonNull Task<AuthResult> task){
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