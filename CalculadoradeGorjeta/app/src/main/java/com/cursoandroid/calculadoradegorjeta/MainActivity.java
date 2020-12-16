package com.cursoandroid.calculadoradegorjeta;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private TextView txtGorjeta;
    private TextView txtValorTotal;
    private TextView txtPorcentagem;
    private TextView txtValorConta;
    private SeekBar seekBarGorjeta;
    private DecimalFormat df = new DecimalFormat("#.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtGorjeta = findViewById(R.id.txtGorjeta);
        txtPorcentagem = findViewById(R.id.txtPorcentagem);
        txtValorConta = findViewById(R.id.txtValorConta);
        txtValorTotal = findViewById(R.id.txtValorTotal);
        seekBarGorjeta = findViewById(R.id.seekBarGorjeta);

        txtValorConta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtValorTotal.setText("R$ " +charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        seekBarGorjeta.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(!(i == 0)){
                    txtPorcentagem.setText(i+ "%");
                    calcularGorjeta(i, Double.parseDouble(txtValorConta.getText().toString()));
                    valorTotal();
                }
                else{
                    txtPorcentagem.setText("0%");
                    txtGorjeta.setText("R$ 0,00");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void calcularGorjeta(int gorjeta, Double total){
        Double resultado = (total * gorjeta) / 100;

        txtGorjeta.setText("R$ " +df.format(resultado));
    }

    @SuppressLint("SetTextI18n")
    public void valorTotal(){
        Double valor = Double.parseDouble(txtGorjeta.getText().toString().substring(3).replace(",", ".")) + Double.parseDouble(txtValorConta.getText().toString().replace(",", "."));

        txtValorTotal.setText("R$ " +df.format(valor));
    }
}