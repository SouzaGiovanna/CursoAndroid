package com.cursoandroid.snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private Button buttonAbrir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAbrir = findViewById(R.id.buttonAbrir);

        buttonAbrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Botão pressionado", Snackbar.LENGTH_LONG).setAction("Confirmar", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        buttonAbrir.setText("Botão Abrir Alterado");
                    }
                }).setActionTextColor(getResources().getColor(R.color.colorPrimary)).show();
            }
        });
    }


}