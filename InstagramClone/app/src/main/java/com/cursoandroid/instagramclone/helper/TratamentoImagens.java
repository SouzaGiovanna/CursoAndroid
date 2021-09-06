package com.cursoandroid.instagramclone.helper;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class TratamentoImagens {
    public static byte[] ConverterImagemByteArray(Bitmap bitmap){
        //Comprimo bitmap para um formato png/jpg
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 75, baos);

        //converte o baos para pixel brutos em uma matriz de bytes
        //(dados da imagem)
        return baos.toByteArray();
    }
}
