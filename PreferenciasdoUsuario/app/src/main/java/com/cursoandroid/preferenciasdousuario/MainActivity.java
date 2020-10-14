package com.cursoandroid.preferenciasdousuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private Button btnSalvar;
    private TextInputEditText txtNome;
    private TextView txtOla;
    private static final String ARQUIVO_PREFERENCIA = "ArquivoPreferencia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSalvar = findViewById(R.id.btnSalvar);
        txtNome = findViewById(R.id.txtNome);
        txtOla = findViewById(R.id.txtOla);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
                SharedPreferences.Editor editor = preferences.edit();

                //Validar o nome
                if(txtNome.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Preencha o nome", Toast.LENGTH_LONG).show();
                }
                else{
                    String nome = txtNome.getText().toString();
                    editor.putString("nome", nome);
                    editor.commit();

                    txtOla.setText("Olá, " +nome);
                }
            }
        });

        //Recuperar os dados
        SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);

        //Valida se temos o nome em preferencias
        if(preferences.contains("nome")){
            String nome = preferences.getString("nome", "usuário não definido");
            txtOla.setText("Olá, " +nome);
        }
        else{
            txtOla.setText("Olá, usuário não definido");
        }
    }
}