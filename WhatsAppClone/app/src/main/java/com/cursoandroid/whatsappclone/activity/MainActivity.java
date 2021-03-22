package com.cursoandroid.whatsappclone.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.cursoandroid.whatsappclone.R;
import com.cursoandroid.whatsappclone.config.ConfigFirebase;
import com.cursoandroid.whatsappclone.fragment.ContatosFragment;
import com.cursoandroid.whatsappclone.fragment.ConversasFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;
    private Bundle dados;
    private boolean manterConectado = false;
    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;
    private MaterialSearchView searchview;
    private FragmentPagerItemAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        smartTabLayout = findViewById(R.id.viewPagerTab);
        viewPager = findViewById(R.id.viewPager);
        searchview = findViewById(R.id.search_view);

        autenticacao = ConfigFirebase.getFirebaseAutenticacao();

        dados = getIntent().getExtras();

        if(dados != null) {
            manterConectado = (boolean) dados.getSerializable("manterConectado");
        }

        configAdapterAbas();

        //Listener para caixa de texto
        searchview.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ConversasFragment conversasFragment = (ConversasFragment) adapter.getPage(0);

                if(newText != null && !newText.isEmpty()){
                    conversasFragment.pesquisarConversas(newText.toLowerCase());
                }

                return true;
            }
        });

        //Listener para searchview
        searchview.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                ConversasFragment conversasFragment = (ConversasFragment) adapter.getPage(0);

                conversasFragment.recarregarConversas();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(!manterConectado){
            deslogar();
        }
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        //Configurar botao de pesquisa
        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchview.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair :
                deslogar();
                finish();
                break;
            case R.id.menuConfig :
                abrirConfiguracoes();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deslogar(){
        try{
            autenticacao.signOut();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void configAdapterAbas(){
        adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Conversas", ConversasFragment.class)
                        .add("Contatos", ContatosFragment.class)
                        .create());

        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);
    }

    private void abrirConfiguracoes(){
        startActivity(new Intent(MainActivity.this, ConfiguracoesActivity.class));
    }
}