package com.cursoandroid.vivadecorafilmes.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Conectividade {
    public static boolean Conectividade(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivity != null){
            NetworkInfo network = connectivity.getActiveNetworkInfo();

            return network != null && network.isConnected();
        }

        return false;
    }
}
