package com.cursoandroid.abas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.cursoandroid.abas.Fragment.EmAltaFragment;
import com.cursoandroid.abas.Fragment.HomeFragment;
import com.cursoandroid.abas.Fragment.InscricoesFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class MainActivity extends AppCompatActivity {
    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smartTabLayout = findViewById(R.id.viewPagerTab);
        viewPager = findViewById(R.id.viewPager);

        //configurar adapter para abas
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Home", HomeFragment.class)
                        .add("Inscrições", InscricoesFragment.class)
                        .add("Em Alta", EmAltaFragment.class)
                        .create());

        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);

        getSupportActionBar().setElevation(0);
    }
}