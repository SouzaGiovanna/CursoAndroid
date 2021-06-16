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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cursoandroid.instagramclone.R;
import com.cursoandroid.instagramclone.activity.EditarPerfilActivity;
import com.cursoandroid.instagramclone.config.ConfigFirebase;
import com.cursoandroid.instagramclone.helper.UsuarioFirebase;
import com.cursoandroid.instagramclone.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragment extends Fragment {
    private Button btnEditarPerfil;
    private CircleImageView imgPerfil;
    private Usuario usuarioAtual = UsuarioFirebase.getDadosUsuarioLogado();;
    private TextView txtSeguidores, txtPublicacoes, txtSeguindo;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        btnEditarPerfil = root.findViewById(R.id.btn_perfil);
        imgPerfil = root.findViewById(R.id.img_perfil);
        txtPublicacoes = root.findViewById(R.id.txt_qtd_publicacoes);
        txtSeguidores = root.findViewById(R.id.txt_qtd_seguidores);
        txtSeguindo = root.findViewById(R.id.txt_qtd_pessoas_seguindo);

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