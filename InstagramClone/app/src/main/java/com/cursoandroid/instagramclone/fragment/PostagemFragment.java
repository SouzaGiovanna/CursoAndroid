package com.cursoandroid.instagramclone.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cursoandroid.instagramclone.R;
import com.cursoandroid.instagramclone.activity.FiltroActivity;
import com.cursoandroid.instagramclone.helper.Permissoes;
import com.cursoandroid.instagramclone.helper.TratamentoImagens;

public class PostagemFragment extends Fragment {
    private String[] permissoesNecessarias = new String[]{
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    };
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private Button btnAbrirCamera, btnAbrirGaleria;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_postagem, container, false);

        btnAbrirCamera = root.findViewById(R.id.btnAbrirCamera);
        btnAbrirGaleria = root.findViewById(R.id.btnAbrirGaleria);

        //Valida permissões
        Permissoes.validarPermissoes(permissoesNecessarias, getActivity(), 1);

        //Adiciona evento de clique no botão da câmera
        btnAbrirCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivityForResult(intent, SELECAO_CAMERA);
                }
            }
        });

        btnAbrirGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivityForResult(intent, SELECAO_GALERIA);
                }
            }
        });

        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissaoResultado : grantResults){
            if(permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap imagem = null;

        try{
            if(resultCode == getActivity().RESULT_OK){
                //Valida tipo de selecção da imagem
                switch(requestCode){
                    case SELECAO_CAMERA :
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA :
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), localImagemSelecionada);
                }
            }

            //Valida imagem selecionada
            if(imagem != null){
                //Envia a imagem escolhida para a aplicação do filtro
                Intent intent = new Intent(getActivity(), FiltroActivity.class);
                intent.putExtra("fotoEscolhida", TratamentoImagens.ConverterImagemByteArray(imagem));
                startActivity(intent);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void alertaValidacaoPermissao(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

        //Configurar AlertDialog
        alertDialog.setTitle("Permissões Negadas");
        alertDialog.setMessage("Para utilizar o app é necessário aceitar as permissões");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                btnAbrirCamera.setEnabled(false);
                btnAbrirGaleria.setEnabled(false);
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }
}