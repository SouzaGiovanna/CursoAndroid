package com.cursoandroid.alcoolougasolina;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button button;
    TextView result;
    EditText gasolina, alcool;
    Double precoGasolina, precoAlcool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        result = findViewById(R.id.txtResult);

        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                gasolina = findViewById(R.id.edtPrecoGasolina);
                alcool = findViewById(R.id.edtPrecoAlcool);

                precoGasolina = Double.valueOf(gasolina.getText().toString());
                precoAlcool = Double.valueOf(alcool.getText().toString());

                if(precoGasolina.equals(precoAlcool)){
                    result.setText("Não há diferença entre os preços");
                }
                else if(precoGasolina > precoAlcool){
                    result.setText("Melhor escolher o Álcool");
                }
                else{
                    result.setText("Melhor escolher a Gasolina");
                }
            }
        });
    }
}