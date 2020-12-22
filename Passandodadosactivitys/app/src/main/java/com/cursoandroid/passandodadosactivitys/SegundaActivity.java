package com.cursoandroid.passandodadosactivitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SegundaActivity extends AppCompatActivity {
    private TextView txtIdade, txtNome, txtNome2, txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        txtIdade = findViewById(R.id.txtIdade);
        txtNome = findViewById(R.id.txtNome);
        txtNome2 = findViewById(R.id.txtNome2);
        txtEmail = findViewById(R.id.txtEmail);

        Bundle dados = getIntent().getExtras();
        Usuario usuario = (Usuario) dados.getSerializable("objeto");

        txtNome.setText(dados.getString("nome"));
        txtIdade.setText(String.valueOf(dados.getInt("idade")));

        txtNome2.setText(usuario.getNome());
        txtEmail.setText(usuario.getEmail());
    }
}