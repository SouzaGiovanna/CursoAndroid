package com.cursoandroid.agendatm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText txtNome;
    private EditText txtEmail;
    private EditText txtTelefone;
    private EditText txtAssunto;
    private EditText txtMensagem;

    private ContatoDAO contatoDao;

    int cod = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNome = findViewById(R.id.txtNome);
        txtEmail = findViewById(R.id.txtEmail);
        txtTelefone = findViewById(R.id.txtTelefone);
        txtAssunto = findViewById(R.id.txtAssunto);
        txtMensagem = findViewById(R.id.txtMensagem);

        contatoDao = new ContatoDAO(this);
    }

    public void salvar(View view){
        if(verificarCampoVazio()){
            if(validarFormatoEmail(txtEmail.getText().toString())) {
                Contato contato = new Contato();

                contato.setNome(txtNome.getText().toString());
                contato.setEmail(txtEmail.getText().toString());
                contato.setTelefone(txtTelefone.getText().toString());
                contato.setAssunto(txtAssunto.getText().toString());
                contato.setMensagem(txtMensagem.getText().toString());

                long pegarId = contatoDao.inserir(contato);

                String resultado = String.format("id: %s", pegarId);

                //cod += 1;
                Toast.makeText(this, resultado, Toast.LENGTH_LONG).show();

                clear((ViewGroup)findViewById(R.id.containerConteudo));
                txtNome.requestFocus();
            }
            else mensagemEmailInvalido();
        }
        else mensagemCampoVazio();
    }

    public void cancelar(View view){
        clear((ViewGroup)findViewById(R.id.containerConteudo));
        txtNome.requestFocus();
    }

    public boolean verificarCampoVazio(){
        boolean preenchido;

        if(txtNome.getText().toString().trim().equals("")){
            preenchido = false;
        }
        else if(txtEmail.getText().toString().trim().equals("")){
            preenchido = false;
        }
        else if(txtTelefone.getText().toString().trim().equals("")){
            preenchido = false;
        }
        else if(txtAssunto.getText().toString().trim().equals("")){
            preenchido = false;
        }
        else if(txtMensagem.getText().toString().trim().equals("")){
            preenchido = false;
        }
        else{
            preenchido = true;
        }
        return preenchido;
    }

    public void mensagemCampoVazio(){
        Toast.makeText(this, "Preencha os campos vazios para continuar", Toast.LENGTH_SHORT).show();
    }

    public void mensagemEmailInvalido(){
        Toast.makeText(this, "Digite um email válido", Toast.LENGTH_SHORT).show();
    }

    public boolean validarFormatoEmail(final String email){
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return true;
        }
        else return false;
    }

    public void clear(ViewGroup group) {
        int count = group.getChildCount();

        for (int i = 0; i < count; i++) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }
        }
    }
}