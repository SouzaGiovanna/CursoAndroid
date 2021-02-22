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

public class VogaisFragment extends Fragment {
    private ImageView btnA, btnE, btnI, btnO, btnU;
    private MediaPlayer mediaPlayer;
    private PlayerAudio player = new PlayerAudio();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vogais, container, false);

        btnA = root.findViewById(R.id.btnA);
        btnE = root.findViewById(R.id.btnE);
        btnI = root.findViewById(R.id.btnI);
        btnO = root.findViewById(R.id.btnO);
        btnU = root.findViewById(R.id.btnU);

        btnA.setImageResource(R.drawable.a_removebg_preview);
        btnE.setImageResource(R.drawable.e_removebg_preview);
        btnI.setImageResource(R.drawable.i_removebg_preview);
        btnO.setImageResource(R.drawable.o_removebg_preview);
        btnU.setImageResource(R.drawable.u_removebg_preview);

        btnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.a);

                player.executarSom(mediaPlayer);
            }
        });

        btnE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.e);

                player.executarSom(mediaPlayer);
            }
        });

        btnI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.i);

                player.executarSom(mediaPlayer);
            }
        });

        btnO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.o);

                player.executarSom(mediaPlayer);
            }
        });

        btnU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.u);

                player.executarSom(mediaPlayer);
            }
        });

        return root;
    }
}