package com.cursoandroid.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cursoandroid.organizze.R;
import com.cursoandroid.organizze.activity.CadastroActivity;
import com.cursoandroid.organizze.activity.LoginActivity;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new FragmentSlide.Builder().background(R.color.colorPrimary).fragment(R.layout.intro_1).build());
        addSlide(new FragmentSlide.Builder().background(R.color.colorPrimary).fragment(R.layout.intro_2).build());
        addSlide(new FragmentSlide.Builder().background(R.color.colorPrimary).fragment(R.layout.intro_3).build());
        addSlide(new FragmentSlide.Builder().background(R.color.colorPrimary).fragment(R.layout.intro_4).build());
        addSlide(new FragmentSlide.Builder().background(R.color.colorPrimary).fragment(R.layout.intro_cadastro).canGoForward(false).build());
    }

    public void btEntrar(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void btCadastrar(View view){
        startActivity(new Intent(this, CadastroActivity.class));
    }
}