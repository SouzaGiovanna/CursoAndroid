package com.cursoandroid.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.cursoandroid.organizze.R;
import com.cursoandroid.organizze.helper.DateCustom;
import com.cursoandroid.organizze.model.Movimentacao;
import com.google.android.material.textfield.TextInputEditText;

public class DespesasActivity extends AppCompatActivity {
    private EditText edtValor;
    private TextInputEditText edtData;
    private TextInputEditText edtCategoria;
    private TextInputEditText edtDescCategoria;
    private Movimentacao movimentacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        edtValor = findViewById(R.id.edtValor);
        edtData = findViewById(R.id.edtData);
        edtCategoria = findViewById(R.id.edtCategoria);
        edtDescCategoria = findViewById(R.id.edtDescCategoria);

        //preenche o campo data com a data atual
        edtData.setText(DateCustom.dataAtual());
    }

    public void salvarDespesa(View view){
        String data = edtData.getText().toString();
        movimentacao = new Movimentacao();

        movimentacao.setValor(Double.parseDouble(edtValor.getText().toString()));
        movimentacao.setCategoria(edtCategoria.getText().toString());
        movimentacao.setDescricao(edtDescCategoria.getText().toString());
        movimentacao.setData(data);
        movimentacao.setTipo("d");

        movimentacao.salvar(data);
    }
}