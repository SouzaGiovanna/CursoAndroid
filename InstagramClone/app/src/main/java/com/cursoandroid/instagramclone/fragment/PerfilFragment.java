package com.cursoandroid.instagramclone.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.cursoandroid.instagramclone.R;
import com.cursoandroid.instagramclone.activity.EditarPerfilActivity;
import com.cursoandroid.instagramclone.activity.MainActivity;
import com.cursoandroid.instagramclone.config.ConfigFirebase;
import com.cursoandroid.instagramclone.helper.UsuarioFirebase;
import com.cursoandroid.instagramclone.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragment extends Fragment {
    private Button btnEditarPerfil;
    private CircleImageView imgPerfil;
    private Usuario usuarioAtual;
    private ValueEventListener eventListener;
    private DatabaseReference usuarioRef;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        btnEditarPerfil = root.findViewById(R.id.btn_editar_perfil);
        imgPerfil = root.findViewById(R.id.img_perfil);

        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditarPerfilActivity.class));
            }
        });

        recuperarDadosUsuario();

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();

        recuperarDadosUsuario();
    }

    @Override
    public void onDestroy() {
        usuarioRef.removeEventListener(eventListener);
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void recuperarDadosUsuario(){
        usuarioAtual = UsuarioFirebase.getDadosUsuarioLogado();
        usuarioRef = ConfigFirebase.getFirebaseDatabse().child("usuarios").child(UsuarioFirebase.getIdUsuario());

        eventListener = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(usuarioAtual.getFoto() != null && !usuarioAtual.getFoto().isEmpty()){
                    Uri uri = Uri.parse(usuarioAtual.getFoto());
                    Glide.with(requireActivity()).load(uri).into(imgPerfil);
                }
                else{
                    imgPerfil.setImageResource(R.drawable.raposinha);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}