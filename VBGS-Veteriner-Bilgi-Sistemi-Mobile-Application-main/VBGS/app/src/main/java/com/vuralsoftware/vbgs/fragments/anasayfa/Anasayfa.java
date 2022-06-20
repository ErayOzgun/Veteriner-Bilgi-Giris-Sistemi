package com.vuralsoftware.vbgs.fragments.anasayfa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;


import com.vuralsoftware.vbgs.R;
import com.vuralsoftware.vbgs.activity.BottomView;

public class Anasayfa extends Fragment {

    private AnasayfaViewModel mViewModel;
    private BottomView nvb;
    private View v;
    private String sayi;
    private Button btn;

    public static Anasayfa newInstance() {
        return new Anasayfa();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.fragment_anasayfa, container, false);


        btn= v.findViewById(R.id.hyvnlar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavDirections action = new ActionOnlyNavDirections(R.id.action_nav_Anasayfa_to_nav_Hayvanlar);
                Navigation.findNavController(v).navigate(action);
            }
        });




        Button btn2 = v.findViewById(R.id.hyvsorgula);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               NavDirections action = new ActionOnlyNavDirections(R.id.action_nav_Anasayfa_to_nav_HayvanSorgula);
               Navigation.findNavController(v).navigate(action);
            }
        });

        Button btn3 = v.findViewById(R.id.ntlar);

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               NavDirections action = new ActionOnlyNavDirections(R.id.action_nav_Anasayfa_to_nav_GenelNotlar);
               Navigation.findNavController(v).navigate(action);
            }
        });

        return v;


    }



}