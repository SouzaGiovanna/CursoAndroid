package com.cursoandroid.vivadecorafilmes.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView imagem;
    private ProgressBar progressBar;

    public DownloadImageTask(ImageView imagem, ProgressBar progressBar) {
        this.imagem = imagem;
        this.progressBar = progressBar;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String url = strings[0];
        Bitmap icon = null;

        try{
            InputStream in = new java.net.URL(url).openStream();
            icon = BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return icon;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imagem.setImageBitmap(bitmap);
        imagem.setBackground(null);
        progressBar.setVisibility(View.INVISIBLE);
    }
}
