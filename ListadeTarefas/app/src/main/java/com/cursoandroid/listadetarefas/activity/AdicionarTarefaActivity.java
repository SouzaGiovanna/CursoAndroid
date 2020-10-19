package com.cursoandroid.listadetarefas.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.cursoandroid.listadetarefas.R;
import com.cursoandroid.listadetarefas.DAO.TarefaDAO;
import com.cursoandroid.listadetarefas.model.Tarefa;
import com.google.android.material.textfield.TextInputEditText;

public class AdicionarTarefaActivity extends AppCompatActivity {

    private TextInputEditText txtTarefa;
    private TarefaDAO tarefaDAO;
    private MainActivity mainActivity;
    private Tarefa tarefaAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);

        txtTarefa = findViewById(R.id.txtTarefa);

        tarefaDAO = new TarefaDAO(getApplicationContext());
        mainActivity = new MainActivity();

        //Recuperar tarefa, caso seja edição
        tarefaAtual = (Tarefa) getIntent().getSerializableExtra("tarefaSelecionada");

        //Configurar tarefa na caixa de texto
        if(tarefaAtual != null){
            txtTarefa.setText(tarefaAtual.getTarefa());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adicionar_tarefa, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemSalvar :
                salvar();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void salvar() {
        Tarefa tarefa = new Tarefa();


        if (verificarCampoVazio()) {
            if (tarefaAtual != null) { //edição

                tarefa.setTarefa(txtTarefa.getText().toString());
                tarefa.setId(tarefaAtual.getId());

                //Atualizar no banco de dados
                if(tarefaDAO.update(tarefa)){
                    Toast.makeText(getApplicationContext(), "Tarefa atualizada com sucesso!", Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Não foi possível atualizar a tarefa", Toast.LENGTH_SHORT).show();

                    finish();
                }
            } else { //salvar
                tarefa.setTarefa(txtTarefa.getText().toString());

                if (tarefaDAO.insert(tarefa)) {
                    Toast.makeText(getApplicationContext(), "Tarefa salva com sucesso!", Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Não foi possível salvar a tarefa", Toast.LENGTH_SHORT).show();

                    finish();
                }
            }
        }
        else{
            mensagemCampoVazio();
        }
    }

    public boolean verificarCampoVazio(){
        if(txtTarefa.getText().toString().trim().equals("")){
            return false;
        }
        else{
            return true;
        }
    }

    public void mensagemCampoVazio(){
        Toast.makeText(getApplicationContext(), "Digite o nome da tarefa para salvar", Toast.LENGTH_SHORT).show();
    }

    public void clear(){
        txtTarefa.setText("");
    }
}