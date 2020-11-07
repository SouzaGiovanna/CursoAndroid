package com.cursoandroid.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cursoandroid.organizze.R;
import com.cursoandroid.organizze.config.ConfiguracaoFirebase;
import com.cursoandroid.organizze.helper.Base64Custom;
import com.cursoandroid.organizze.helper.DateCustom;
import com.cursoandroid.organizze.model.Movimentacao;
import com.cursoandroid.organizze.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReceitasActivity extends AppCompatActivity {
    private EditText edtValor;
    private TextInputEditText edtData;
    private TextInputEditText edtCategoria;
    private TextInputEditText edtDescCategoria;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Double receitaTotal;
    private Double receitaGerada;
    private Double receitaAtualizada;
    private String emailUsuario;
    private String idUsuario;
    private DatabaseReference usuarioRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        edtValor = findViewById(R.id.edtValor);
        edtData = findViewById(R.id.edtData);
        edtCategoria = findViewById(R.id.edtCategoria);
        edtDescCategoria = findViewById(R.id.edtDescCategoria);

        //preenche o campo data com a data atual
        edtData.setText(DateCustom.dataAtual());

        emailUsuario = autenticacao.getCurrentUser().getEmail();
        idUsuario = Base64Custom.codificarBase64(emailUsuario);
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        recuperarReceitaTotal();
    }

    public void salvarReceita(View view) {
        String data = edtData.getText().toString();
        receitaGerada = Double.parseDouble(edtValor.getText().toString());
        movimentacao = new Movimentacao();

        if(validarCampos()) {
            movimentacao.setValor(receitaGerada);
            movimentacao.setCategoria(edtCategoria.getText().toString());
            movimentacao.setDescricao(edtDescCategoria.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("r");

            receitaAtualizada = receitaTotal + receitaGerada;

            atualizarReceita(receitaAtualizada);

            movimentacao.salvar(data);
        }
        else { msgCampoVazio(); }
    }

    public boolean validarCampos(){
        if(edtValor.getText().toString().isEmpty()){
            return false;
        }
        if(edtData.getText().toString().isEmpty()){
            return false;
        }
        if(edtDescCategoria.getText().toString().isEmpty()){
            return false;
        }
        if(edtCategoria.getText().toString().isEmpty()){
            return false;
        }

        return true;
    }

    public Toast msgCampoVazio(){
        return Toast.makeText(getApplicationContext(), "Preencha todos os campos para continuar", Toast.LENGTH_SHORT);
    }

    public void recuperarReceitaTotal(){
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void atualizarReceita(Double receita){
        usuarioRef.child("receitaTotal").setValue(receita);
    }
}