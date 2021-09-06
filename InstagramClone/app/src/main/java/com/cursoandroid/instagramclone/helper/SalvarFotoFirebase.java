package com.cursoandroid.instagramclone.helper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cursoandroid.instagramclone.config.ConfigFirebase;
import com.cursoandroid.instagramclone.model.Postagem;
import com.cursoandroid.instagramclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class SalvarFotoFirebase {
    private StorageReference storageRef = ConfigFirebase.getStorage();
    private StorageReference pastaFoto;
    private StorageReference imgRef;

    public SalvarFotoFirebase(String pastaFoto, String nomeArquivo) {
        this.pastaFoto = storageRef.child("fotos").child(pastaFoto);
        imgRef = this.pastaFoto.child(nomeArquivo+ ".png");
    }

    public void salvar(CircleImageView foto, Activity activity){
        //Configura para imagem ser salva em memória
        foto.setDrawingCacheEnabled(true);
        foto.buildDrawingCache();

        //Recupera bitmap da imagem (imagem a ser carregada)
        Bitmap bitmap = foto.getDrawingCache();

        //Retorna objeto que irá controlar o upload
        UploadTask uploadTask = imgRef.putBytes(TratamentoImagens.ConverterImagemByteArray(bitmap));

        uploadTask.addOnSuccessListener(activity, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri url = task.getResult();

                        atualizaFotoUsuario(url);
                    }
                });
            }
        });
    }

    public void salvar(ImageView foto, final Activity activity, final Postagem postagem){
        //Configura para imagem ser salva em memória
        foto.setDrawingCacheEnabled(true);
        foto.buildDrawingCache();

        //Recupera bitmap da imagem (imagem a ser carregada)
        Bitmap bitmap = foto.getDrawingCache();

        //Retorna objeto que irá controlar o upload
        UploadTask uploadTask = imgRef.putBytes(TratamentoImagens.ConverterImagemByteArray(bitmap));

        uploadTask.addOnSuccessListener(activity, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri url = task.getResult();

                        Toast.makeText(activity, "Imagem salva com sucesso!", Toast.LENGTH_LONG).show();
                        postagem.setFoto(url.toString());
                        if (postagem.salvar()) {
                            Toast.makeText(activity, "Postagem salva com sucesso!", Toast.LENGTH_SHORT).show();

                            activity.finish();
                        }
                    }
                });
            }
        }).addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Erro ao salvar a imagem, tente novamente!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void atualizaFotoUsuario(Uri url) {
        Usuario usuario = UsuarioFirebase.getDadosUsuarioLogado();
        UsuarioFirebase.atualizarFotoUsuario(url);

        usuario.setFoto(url.toString());
        usuario.atualizar();
    }
}
