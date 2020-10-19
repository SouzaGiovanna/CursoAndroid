package com.cursoandroid.listadetarefas.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cursoandroid.listadetarefas.model.Tarefa;

import java.util.ArrayList;
import java.util.List;

public class TarefaDAO {
    private DbHelper conexao;
    private SQLiteDatabase banco;
    private SQLiteDatabase le;

    public TarefaDAO(Context context) {
        conexao = new DbHelper(context);
        banco = conexao.getWritableDatabase();
        le = conexao.getReadableDatabase();
    }

    public boolean insert(Tarefa tarefa){
        ContentValues values = new ContentValues();

        values.put("nome", tarefa.getTarefa());
        try {
            banco.insert(DbHelper.TABELA_TAREFAS, null, values);
            Log.i("INFO", "Tarefa salva com sucesso!");
        } catch (Exception e){
            Log.e("INFO", "Erro ao salvar tarefa " +e.getMessage());

            return false;
        }
        return true;
    }

    public boolean update(Tarefa tarefa){
        ContentValues values = new ContentValues();

        values.put("nome", tarefa.getTarefa());

        try {
            String[] args = {String.valueOf(tarefa.getId())};

            banco.update(DbHelper.TABELA_TAREFAS, values, "id=?", args);
            Log.i("INFO", "Tarefa atualizada com sucesso!");
        } catch (Exception e){
            Log.e("INFO", "Erro ao atualizada tarefa " +e.getMessage());

            return false;
        }

        return true;
    }

    public boolean delete(Tarefa tarefa){
        try {
            String[] args = {String.valueOf(tarefa.getId())};

            banco.delete(DbHelper.TABELA_TAREFAS,"id=?", args);
            Log.i("INFO", "Tarefa removida com sucesso!");
        } catch (Exception e){
            Log.e("INFO", "Erro ao remover a tarefa " +e.getMessage());

            return false;
        }

        return true;
    }

    public List<Tarefa> listar(){
        List<Tarefa> tarefas = new ArrayList<>();

        String sql = "SELECT * FROM " +DbHelper.TABELA_TAREFAS+ " ;";

        Cursor c = le.rawQuery(sql, null);

        while(c.moveToNext()){
            Tarefa tarefa = new Tarefa();

            Long id = c.getLong(c.getColumnIndex("id"));
            String nomeTarefa = c.getString(c.getColumnIndex("nome"));

            tarefa.setId(id);
            tarefa.setTarefa(nomeTarefa);

            tarefas.add(tarefa);
        }

        return tarefas;
    }
}