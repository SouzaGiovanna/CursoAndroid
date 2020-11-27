package com.cursoandroid.permissoes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String[] appPermissoes = {
            Manifest.permission.CAMERA
    };
    public static final int CODIGO_PERMISSOES_REQUERIDAS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if(verificarPermissoes()){
            
        }
    }

    private boolean verificarPermissoes() {
        List<String> permissoesRequeridas = new ArrayList<>();

        for(String permissao : appPermissoes){
            if(ContextCompat.checkSelfPermission(this, permissao) != PackageManager.PERMISSION_GRANTED){
                permissoesRequeridas.add(permissao);
            }
        }

        if(!permissoesRequeridas.isEmpty()){
            ActivityCompat.requestPermissions(this, permissoesRequeridas.toArray(new String[permissoesRequeridas.size()]), CODIGO_PERMISSOES_REQUERIDAS);

            return false;
        }

        return true;
    }
}