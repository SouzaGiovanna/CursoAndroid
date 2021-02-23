package com.cursoandroid.minhasanotaes;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private AnotacaoPreferencias preferencias;
    private EditText anotacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferencias = new AnotacaoPreferencias(getApplicationContext());
        anotacao = findViewById(R.id.edtAnotacao);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Validar se foi digitado algo
                String texto = anotacao.getText().toString();
                if(texto.isEmpty()){
                    Snackbar.make(view, "Digite algo para salvar", Snackbar.LENGTH_LONG).show();
                }
                else{
                    preferencias.salvarAnotacao(texto);

                    Snackbar.make(view, "Anotação salva com sucesso!", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        //recuperar anotacao
        String text = preferencias.recuperarAnotacao();
        if(!text.isEmpty()){
            anotacao.setText(text);
        }
    }
}