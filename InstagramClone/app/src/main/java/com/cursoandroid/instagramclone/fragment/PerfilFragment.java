package com.cursoandroid.instagramclone.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cursoandroid.instagramclone.R;
import com.cursoandroid.instagramclone.activity.EditarPerfilActivity;
import com.cursoandroid.instagramclone.adapter.AdapterPublicacoes;
import com.cursoandroid.instagramclone.config.ConfigFirebase;
import com.cursoandroid.instagramclone.helper.UsuarioFirebase;
import com.cursoandroid.instagramclone.model.Postagem;
import com.cursoandroid.instagramclone.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragment extends Fragment {
    private Button btnEditarPerfil;
    private CircleImageView imgPerfil;
    private Usuario usuarioAtual = UsuarioFirebase.getDadosUsuarioLogado();;
    private TextView txtSeguidores, txtPublicacoes, txtSeguindo;
    private GridView gridPublicacoes;

    private AdapterPublicacoes adapterPublicacoes;
    private DatabaseReference postagensRef;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        btnEditarPerfil = root.findViewById(R.id.btn_perfil);
        imgPerfil = root.findViewById(R.id.img_perfil);
        txtPublicacoes = root.findViewById(R.id.txt_qtd_publicacoes);
        txtSeguidores = root.findViewById(R.id.txt_qtd_seguidores);
        txtSeguindo = root.findViewById(R.id.txt_qtd_pessoas_seguindo);
        gridPublicacoes = root.findViewById(R.id.grid_publicacoes);

        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditarPerfilActivity.class));
            }
        });

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();

        recuperarDadosUsuario();
        inicializarImageLoader();
        carregarFotosPostagem();
    }

    private void inicializarImageLoader(){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext()).memoryCache(new LruMemoryCache(2 * 1024 * 1024)).memoryCacheSize(2 * 1024 * 1024).diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100).diskCacheFileNameGenerator(new HashCodeFileNameGenerator()).build();
        ImageLoader.getInstance().init(config);
    }

    private void carregarFotosPostagem(){
        postagensRef = ConfigFirebase.getFirebaseDatabse().child("postagens").child(usuarioAtual.getId());

        postagensRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Configurar o tamnaho da GridView
                int tamanhoGrid = getResources().getDisplayMetrics().widthPixels;
                int tamanhoImagem = tamanhoGrid / 3;
                gridPublicacoes.setColumnWidth(tamanhoImagem);

                List<String> urlFotos = new ArrayList<>();
                for(DataSnapshot data : snapshot.getChildren()){
                    Postagem postagem = data.getValue(Postagem.class);
                    urlFotos.add(postagem.getFoto());
                }

                Log.i("teste", String.valueOf(urlFotos));

                adapterPublicacoes = new AdapterPublicacoes(getContext(), R.layout.adapter_publicacoes, urlFotos);
                gridPublicacoes.setAdapter(adapterPublicacoes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void recuperarDadosUsuario(){
        DatabaseReference firebase = ConfigFirebase.getFirebaseDatabse();
        DatabaseReference usuarioRef = firebase.child("usuarios").child(usuarioAtual.getId());
        if(usuarioAtual.getFoto() != null && !usuarioAtual.getFoto().isEmpty()){
            Uri uri = Uri.parse(usuarioAtual.getFoto());
            Glide.with(requireActivity()).load(uri).into(imgPerfil);
        }
        else{
            imgPerfil.setImageResource(R.drawable.raposinha);
        }

        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txtPublicacoes.setText(dataSnapshot.child("publicacoes").getValue().toString());
                txtSeguidores.setText(dataSnapshot.child("seguidores").getValue().toString());
                txtSeguindo.setText(dataSnapshot.child("seguindo").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}