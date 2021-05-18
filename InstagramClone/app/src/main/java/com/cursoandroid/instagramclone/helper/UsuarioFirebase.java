package com.cursoandroid.instagramclone.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cursoandroid.instagramclone.config.ConfigFirebase;
import com.cursoandroid.instagramclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsuarioFirebase {
    private static FirebaseAuth usuario = ConfigFirebase.getFirebaseAutenticacao();

    public static String getIdUsuario(){
        String idUsuario = usuario.getUid();

        return idUsuario;
    }

    public static FirebaseUser getUsuarioAtual(){
        return usuario.getCurrentUser();
    }

    public static boolean atualizarNomeUsuario(String nome){
        FirebaseUser user = getUsuarioAtual();
        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(nome).build();

        try{
            user.updateProfile(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("AtualizandoNome", "Nome Atualizado com Sucesso");
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

    public static Usuario getDadosUsuarioLogado(){
        FirebaseUser firebaseUser = getUsuarioAtual();
        Usuario usuario = new Usuario();

        usuario.setEmail(firebaseUser.getEmail());
        usuario.setNome(firebaseUser.getDisplayName());

        if(firebaseUser.getPhotoUrl() == null){
            usuario.setFoto("");
        }
        else{
            usuario.setFoto(firebaseUser.getPhotoUrl().toString());
        }

        return usuario;
    }
}
