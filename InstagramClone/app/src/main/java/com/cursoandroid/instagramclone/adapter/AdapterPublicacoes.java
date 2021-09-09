package com.cursoandroid.instagramclone.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.cursoandroid.instagramclone.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

public class AdapterPublicacoes extends ArrayAdapter<String> {
    private Context context;
    private int layoutResouce;
    private List<String> urlFotos;

    public AdapterPublicacoes(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResouce = resource;
        this.urlFotos = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layoutResouce, parent, false);

            viewHolder.progressBar = convertView.findViewById(R.id.progressPublicacao);
            viewHolder.img = convertView.findViewById(R.id.imgPublicacao);
            viewHolder.erro = convertView.findViewById(R.id.img_erro_carregamento);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //Recuperar os dados da imagem
        String urlImagem = getItem(position);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(urlImagem, viewHolder.img, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                viewHolder.progressBar.setVisibility(View.VISIBLE);
                viewHolder.erro.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                viewHolder.progressBar.setVisibility(View.GONE);
                viewHolder.erro.setVisibility(View.VISIBLE);
                viewHolder.img.setImageResource(R.drawable.background_img_erro);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                viewHolder.progressBar.setVisibility(View.GONE);
                viewHolder.erro.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                viewHolder.progressBar.setVisibility(View.GONE);
                //viewHolder.erro.setVisibility(View.VISIBLE);
                //viewHolder.img.setImageResource(R.drawable.background_img_erro);
            }
        });

        return convertView;
    }

    public class ViewHolder{
        ImageView img, erro;
        ProgressBar progressBar;
    }
}
