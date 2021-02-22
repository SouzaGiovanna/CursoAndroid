package com.cursoandroid.aprendaingls.Fragment;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cursoandroid.aprendaingls.R;
import com.cursoandroid.aprendaingls.helper.PlayerAudio;

public class NumerosFragment extends Fragment {
    private ImageView btnUm, btnDois, btnTres, btnQuatro, btnCinco, btnSeis;
    private MediaPlayer mediaPlayer;
    private PlayerAudio player = new PlayerAudio();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_numeros, container, false);

        btnUm = root.findViewById(R.id.btnUm);
        btnDois = root.findViewById(R.id.btnDois);
        btnTres = root.findViewById(R.id.btnTres);
        btnQuatro = root.findViewById(R.id.btnQuatro);
        btnCinco = root.findViewById(R.id.btnCinco);
        btnSeis = root.findViewById(R.id.btnSeis);

        btnUm.setImageResource(R.drawable.um);
        btnDois.setImageResource(R.drawable.dois);
        btnTres.setImageResource(R.drawable.tres);
        btnQuatro.setImageResource(R.drawable.quatro);
        btnCinco.setImageResource(R.drawable.cinco);
        btnSeis.setImageResource(R.drawable.seis);

        btnUm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.one);

                player.executarSom(mediaPlayer);
            }
        });

        btnDois.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.two);

                player.executarSom(mediaPlayer);
            }
        });

        btnTres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.three);

                player.executarSom(mediaPlayer);
            }
        });

        btnQuatro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.four);

                player.executarSom(mediaPlayer);
            }
        });

        btnCinco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.five);

                player.executarSom(mediaPlayer);
            }
        });

        btnSeis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.six);

                player.executarSom(mediaPlayer);
            }
        });

        return root;
    }
}