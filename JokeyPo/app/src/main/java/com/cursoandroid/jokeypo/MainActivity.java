package com.cursoandroid.jokeypo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView result;
    ImageView app, pedra, papel, tesoura;
    ArrayList<String> resultados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultados.add("Você perdeu :(");
        resultados.add("Empatou :/");
        resultados.add("Você ganhou :)");

        result = findViewById(R.id.txtResultado);
        app = findViewById(R.id.imgResultApp);
        pedra = findViewById(R.id.imgPedra);
        papel = findViewById(R.id.imgPapel);
        tesoura = findViewById(R.id.imgTesoura);

        pedra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortearAppResult(0);
            }
        });

        papel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortearAppResult(1);
            }
        });

        tesoura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortearAppResult(2);
            }
        });
    }

    private void sortearAppResult(int usuario){
        Random random = new Random();

        int result = random.nextInt(3);

        switch (result){
            case 0:
                app.setImageResource(R.drawable.pedra);

                break;
            case 1:
                app.setImageResource(R.drawable.papel);

                break;
            case 2:
                app.setImageResource(R.drawable.tesoura);

                break;
        }

        imprimirResult(usuario, result);
    }

    private void imprimirResult(int escolha, int app){
        if(escolha == app){
            result.setText(resultados.get(1));
        }
        else if(escolha == 0 && app == 1){
            result.setText(resultados.get(0));
        }
        else if(escolha == 0 && app == 2){
            result.setText(resultados.get(2));
        }
        else if(escolha == 1 && app == 0){
            result.setText(resultados.get(2));
        }
        else if(escolha == 1 && app == 2){
            result.setText(resultados.get(0));
        }
        else if(escolha == 2 && app == 0){
            result.setText(resultados.get(0));
        }
        else if(escolha == 2 && app == 1){
            result.setText(resultados.get(2));
        }

        //Log.i("teste", "usuario: "+escolha);
    }
}