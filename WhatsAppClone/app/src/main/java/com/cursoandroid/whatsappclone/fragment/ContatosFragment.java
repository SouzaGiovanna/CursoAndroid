package com.cursoandroid.whatsappclone.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cursoandroid.whatsappclone.R;
import com.cursoandroid.whatsappclone.adapter.AdapterContatos;
import com.cursoandroid.whatsappclone.config.ConfigFirebase;
import com.cursoandroid.whatsappclone.helper.UsuarioFirebase;
import com.cursoandroid.whatsappclone.model.Usuario;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ContatosFragment extends Fragment {
    private List<Usuario> listUsuarios;
    private RecyclerView recyclerContatos;
    private DatabaseReference usuariosRef;
    private AdapterContatos adapter;
    private ValueEventListener valueEventListenerContatos;
    private static FirebaseUser usuarioAtual;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contatos, container, false);

        recyclerContatos = root.findViewById(R.id.recyclerContatos);
        usuariosRef = ConfigFirebase.getFirebaseDatabse().child("usuarios");

        recuperarContatos();

        return root;
    }

    @Override
    public void onStop() {
        super.onStop();
        usuariosRef.removeEventListener(valueEventListenerContatos);
    }

    public void recuperarContatos(){
        listUsuarios = new ArrayList<>();
        usuarioAtual = UsuarioFirebase.getUsuarioAtual();

        valueEventListenerContatos = usuariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dados : snapshot.getChildren()){
                    Usuario usuario = dados.getValue(Usuario.class);

                    if(!usuarioAtual.getEmail().equals(usuario.getEmail())) {
                        listUsuarios.add(usuario);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        configurarRecyclerView();
    }

    private void configurarRecyclerView(){
        adapter = new AdapterContatos(listUsuarios, getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerContatos.setLayoutManager(layoutManager);
        recyclerContatos.setHasFixedSize(true);
        recyclerContatos.setAdapter(adapter);
    }
}