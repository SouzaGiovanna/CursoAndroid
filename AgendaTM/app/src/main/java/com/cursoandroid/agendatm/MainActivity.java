package com.cursoandroid.agendatm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView txtNome, txtEmail, txtTelefone, txtAssunto, txtMensagem;
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
    }

    public void salvar(View view){
        if(verificarCampoVazio()){
            cod += 1;
            Toast.makeText(this, "Código: " +cod, Toast.LENGTH_SHORT).show();
        }
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
}