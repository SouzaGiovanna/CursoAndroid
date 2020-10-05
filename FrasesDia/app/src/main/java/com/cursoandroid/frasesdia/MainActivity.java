package com.cursoandroid.frasesdia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.sql.ParameterMetaData;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gerarFrase(View view){
        String[] frases = {
                "A amizade é um amor que nunca morre",
                "A amizade desenvolve a felicidade e reduz o sofrimento, duplicando a nossa alegria e dividindo a nossa dor",
                "A amizade duplica as alegrias e divide as tristezas",
                "A melhor maneira de começar uma amizade é com uma boa gargalhada. De terminar com ela, também",
                "Não é amigo aquele que alardeia a amizade: é traficante; a amizade sente-se, não se diz...",
                "Quando defendemos os nossos amigos, justificamos a nossa amizade",
                "O amor pode morrer na verdade, a amizade na mentira",
                "A amizade é semelhante a um bom café; uma vez frio, não se aquece sem perder bastante do primeiro sabor",
                "A amizade é, acima de tudo, certeza – é isso que a distingue do amor",
                "A amizade é um meio de nos isolarmos da humanidade cultivando algumas pessoas"
        };

        TextView texto = findViewById(R.id.txt_frase_dia);
        texto.setText(frases[sortearId()]);

    }

    public int sortearId(){
        int limite = 10;

        Random sorteio = new Random();
        int id = sorteio.nextInt(limite);

        return id;
    }
}