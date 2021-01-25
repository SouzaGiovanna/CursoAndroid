package com.cursoandroid.fragments.activity;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.cursoandroid.fragments.R;
import com.cursoandroid.fragments.fragment.ContatosFragment;
import com.cursoandroid.fragments.fragment.ConversasFragment;

public class MainActivity extends AppCompatActivity {
    private Button btnConversa, btnContato;
    private ConversasFragment conversasFragment;
    private ContatosFragment contatosFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Remover sombra ActionBar
        getSupportActionBar().setElevation(0);

        conversasFragment = new ConversasFragment();

        //Configurar objeto para o Fragmento
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frameConteudo, conversasFragment);
        transaction.commit();
    }
}