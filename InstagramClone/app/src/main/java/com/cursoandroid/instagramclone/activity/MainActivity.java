package com.cursoandroid.instagramclone.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.cursoandroid.instagramclone.R;
import com.cursoandroid.instagramclone.config.ConfigFirebase;
import com.cursoandroid.instagramclone.fragment.FeedFragment;
import com.cursoandroid.instagramclone.fragment.PerfilFragment;
import com.cursoandroid.instagramclone.fragment.PesquisaFragment;
import com.cursoandroid.instagramclone.fragment.PostagemFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    private Toolbar toolbar;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        user = autenticacao.getCurrentUser();

        configuraBottomNavigationView();
    }

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void configuraBottomNavigationView(){
        BottomNavigationViewEx bottomNavigation = findViewById(R.id.bottomNavigation);
        fragmentTransaction.replace(R.id.view_pager, new FeedFragment()).commit();

        if(bottomNavigation != null) {
            habilitarNavegacao(bottomNavigation);

            //configura item selecionado inicialmente
            Menu menu = bottomNavigation.getMenu();
            MenuItem menuItem = menu.getItem(0);
            menuItem.setChecked(true);
        }
        else{
            Log.i("erro", "BottomNavigation null");
        }
    }

    private void habilitarNavegacao(BottomNavigationViewEx viewEx){
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_home:
                        toolbar.setTitle(R.string.app_name);
                        setSupportActionBar(toolbar);

                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.view_pager, new FeedFragment()).commit();
                        return true;
                    case R.id.menu_pesquisa:
                        toolbar.setTitle(R.string.app_name);
                        setSupportActionBar(toolbar);

                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.view_pager, new PesquisaFragment()).commit();
                        return true;
                    case R.id.menu_nova_postagem:
                        toolbar.setTitle(R.string.app_name);
                        setSupportActionBar(toolbar);

                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.view_pager, new PostagemFragment()).commit();
                        return true;
                    case R.id.menu_perfil:
                        toolbar.setTitle(user.getDisplayName());;
                        setSupportActionBar(toolbar);

                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.view_pager, new PerfilFragment()).commit();
                        return true;
                }
                return false;
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuSair) {
            deslogar();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void deslogar(){
        try{
            autenticacao.signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}