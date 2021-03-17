package com.cursoandroid.whatsappclone.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.cursoandroid.whatsappclone.R;
import com.cursoandroid.whatsappclone.activity.ChatActivity;
import com.cursoandroid.whatsappclone.adapter.AdapterContatos;
import com.cursoandroid.whatsappclone.config.ConfigFirebase;
import com.cursoandroid.whatsappclone.config.RecyclerItemClickListener;
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
    private FirebaseUser usuarioAtual = UsuarioFirebase.getUsuarioAtual();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contatos, container, false);

        recyclerContatos = root.findViewById(R.id.recyclerContatos);
        usuariosRef = ConfigFirebase.getFirebaseDatabse().child("usuarios");

        //recuperarContatos();

        return root;
    }

    @Override
    public void onStop() {
        super.onStop();
        usuariosRef.removeEventListener(valueEventListenerContatos);
    }

    @Override
    public void onStart() {
        super.onStart();

        recuperarContatos();
    }

    public void recuperarContatos(){
        listUsuarios = new ArrayList<>();

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

        recyclerContatos.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerContatos, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Usuario usuarioSelecionado = listUsuarios.get(position);
                Intent intent = new Intent(getActivity(), ChatActivity.class);

                intent.putExtra("contato", usuarioSelecionado);

                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));
    }
}