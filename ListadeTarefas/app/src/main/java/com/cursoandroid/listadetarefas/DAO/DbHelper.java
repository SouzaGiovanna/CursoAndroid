package com.cursoandroid.listadetarefas.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public static int VERSION = 2;
    public static String NAME_DB = "DB_TAREFAS";
    public static String TABELA_TAREFAS = "tarefas";

    public DbHelper(@Nullable Context context) {
        super(context, NAME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS " +TABELA_TAREFAS+ " " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL); ";
        try{
            sqLiteDatabase.execSQL(sql);

            Log.i("INFO DB", "Sucesso ao criar a tabela");
        } catch(Exception e){
            Log.i("INFO DB", "Erro ao criar a tabela" + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(String.format("DROP TABLE IF EXISTS %s", TABELA_TAREFAS));
        onCreate(sqLiteDatabase);
    }
}