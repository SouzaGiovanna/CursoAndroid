package com.cursoandroid.caraoucoroa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class ResultadoActivity extends AppCompatActivity {

    private Button btnVoltar;
    private ImageView imgResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        btnVoltar = findViewById(R.id.button2);
        imgResultado = findViewById(R.id.imageView2);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Random random = new Random();

        int moeda = random.nextInt(2);

        switch (moeda){
            case 0:
                imgResultado.setImageResource(R.drawable.moeda_cara);

                break;
            case 1:
                imgResultado.setImageResource(R.drawable.moeda_coroa);

                break;
        }
    }
}