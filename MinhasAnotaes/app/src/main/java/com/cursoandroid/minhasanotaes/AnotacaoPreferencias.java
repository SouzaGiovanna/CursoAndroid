package com.cursoandroid.minhasanotaes;

import android.content.Context;
import android.content.SharedPreferences;

public class AnotacaoPreferencias {
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private final String NOME_ARQUIVO = "anotacao.preferencias";
    private final String CHAVE_NOME = "nome";

    public AnotacaoPreferencias(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(NOME_ARQUIVO, 0);
        editor = preferences.edit();
    }

    public void salvarAnotacao(String anotacao){
        editor.putString(CHAVE_NOME, anotacao);
        editor.commit();
    }

    public String recuperarAnotacao(){
        return preferences.getString(CHAVE_NOME, "");
    }
}
