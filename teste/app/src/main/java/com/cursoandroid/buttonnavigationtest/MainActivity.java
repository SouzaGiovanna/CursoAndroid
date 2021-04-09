package com.cursoandroid.buttonnavigationtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static androidx.navigation.Navigation.findNavController;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.firstFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.firstFragment:
                        FirstFragment fragment = (FirstFragment) getSupportFragmentManager().findFragmentById(R.id.firstFragment);

                        return true;
                    case R.id.secondFragment:
                        FirstFragment fragment2 = (FirstFragment) getSupportFragmentManager().findFragmentById(R.id.secondFragment);

                        return true;
                    case R.id.thirdFragment:
                        FirstFragment fragment3 = (FirstFragment) getSupportFragmentManager().findFragmentById(R.id.thirdFragment);

                        return true;
                }
                return false;
            }
        });
    }
}