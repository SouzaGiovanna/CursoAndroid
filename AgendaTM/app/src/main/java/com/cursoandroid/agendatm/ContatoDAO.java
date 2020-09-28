package com.cursoandroid.agendatm;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ContatoDAO {
    private Conexao conexao;
    private SQLiteDatabase banco;

    public ContatoDAO(Context context){
        conexao = new Conexao((context));
        banco = conexao.getWritableDatabase();
    }

    public long inserir(Contato contato){
        ContentValues values = new ContentValues();

        values.put("nome", contato.getNome());
        values.put("email", contato.getEmail());
        values.put("telefone", contato.getTelefone());
        values.put("assunto", contato.getAssunto());
        values.put("mensagem", contato.getMensagem());

        return banco.insert("tbContato", null, values);
    }
}