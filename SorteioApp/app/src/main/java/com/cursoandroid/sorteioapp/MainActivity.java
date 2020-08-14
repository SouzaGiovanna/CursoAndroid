package com.cursoandroid.sorteioapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sortearNum(View view){
        TextView texto = findViewById(R.id.textSorteado);
        int limite = 11;

        Random sorteio = new Random();
        int valSorteado = sorteio.nextInt(limite);

        String val = getString(R.string.val2)+ " " +Integer.toString(valSorteado);

        texto.setText(val);
    }
}