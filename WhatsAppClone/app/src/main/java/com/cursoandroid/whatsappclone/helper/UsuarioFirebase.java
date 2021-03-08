package com.cursoandroid.whatsappclone.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cursoandroid.whatsappclone.config.ConfigFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsuarioFirebase {
    private static FirebaseAuth usuario = ConfigFirebase.getFirebaseAutenticacao();
    private static String email = usuario.getCurrentUser().getEmail();

    public static String getIdentificadorUsuario(){
        String idResponsavel = Base64Custom.codificarBase64(email);

        return idResponsavel;
    }

    public static FirebaseUser getUsuarioAtual(){
        return usuario.getCurrentUser();
    }

    public static boolean atualizarNomeUsuario(String nome){
        FirebaseUser user = getUsuarioAtual();
        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(nome).build();

        try{
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar foto do usuário");
                    }
                }
            });
            return true;
        }
        catch (Exception e){
            e.printStackTrace();

            return false;
        }
    }

    public static boolean atualizarFotoUsuario(Uri url){
        FirebaseUser user = getUsuarioAtual();
        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setPhotoUri(url).build();

        try{
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar foto do usuário");
                    }
                }
            });
            return true;
        }
        catch (Exception e){
            e.printStackTrace();

            return false;
        }
    }
}
