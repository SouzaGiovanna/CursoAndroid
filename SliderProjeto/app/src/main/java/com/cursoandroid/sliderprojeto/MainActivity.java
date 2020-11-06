package com.cursoandroid.sliderprojeto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class MainActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        setButtonBackVisible(false);
        setButtonNextVisible(false);

        //Slides de fragments
        addSlide(new FragmentSlide.Builder().background(android.R.color.white).fragment(R.layout.intro_1).build());
        addSlide(new FragmentSlide.Builder().background(android.R.color.white).fragment(R.layout.intro_2).build());
        addSlide(new FragmentSlide.Builder().background(android.R.color.white).fragment(R.layout.intro_3).build());
        addSlide(new FragmentSlide.Builder().background(android.R.color.white).fragment(R.layout.intro_4).build());

        //Slides padrão
        /*addSlide(new SimpleSlide.Builder().title("Titulo").description("Descrição").image(R.drawable.um).background(android.R.color.holo_blue_light).build());
        addSlide(new SimpleSlide.Builder().title("Titulo2").description("Descrição2").image(R.drawable.dois).background(android.R.color.holo_blue_light).build());*/
    }
}