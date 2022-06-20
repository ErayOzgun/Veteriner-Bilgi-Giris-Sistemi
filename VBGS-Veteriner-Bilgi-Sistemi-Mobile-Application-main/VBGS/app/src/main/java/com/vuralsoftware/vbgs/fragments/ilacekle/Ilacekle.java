package com.vuralsoftware.vbgs.fragments.ilacekle;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vuralsoftware.vbgs.activity.BottomView;
import com.vuralsoftware.vbgs.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Ilacekle extends Fragment {

    private IlacViewModel mViewModel;
    private FirebaseUser mUser;
    private HashMap<String, Object> mData;
    private FirebaseFirestore mFireStore;
    private BottomView nvb;
    private String veri, ilacadtxt, doztxt, tarihtxt, saattxt;
    private EditText ilacad, doz, tarih, saat;

    public static Ilacekle newInstance() {
        return new Ilacekle();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ilac, container, false);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFireStore = FirebaseFirestore.getInstance();

        //İlaclar Sayfasından gelen Veri alma

        nvb = new BottomView();
        veri = nvb.bilginotal();

        //Edittext içeri Aktarma;

        ilacad = v.findViewById(R.id.ilacad);
        tarih = v.findViewById(R.id.ilactarih);
        saat = v.findViewById(R.id.ilacsaat);
        doz = v.findViewById(R.id.dozmiktar);

        tarih.setText( new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
        saat.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));

        Button btnkaydet = v.findViewById(R.id.kaydetbtnilac);

        btnkaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(ilacad.getText().toString())){
                    if (!TextUtils.isEmpty(doz.getText().toString())){
                        if(!TextUtils.isEmpty(tarih.getText().toString())) {
                            if (!TextUtils.isEmpty(saat.getText().toString())) {

                                veriekleilac(v);

                            } else {Toast.makeText(getContext(),"Saat Boş Olamaz",Toast.LENGTH_SHORT).show();}
                        }else {Toast.makeText(getContext(),"Tarih Boş Olamaz",Toast.LENGTH_SHORT).show();}
                    }else {Toast.makeText(getContext(),"Doz Boş Olamaz",Toast.LENGTH_SHORT).show();}
                }else {Toast.makeText(getContext(),"İlac Adı Boş Olamaz",Toast.LENGTH_SHORT).show();}
            }
        });

        return v;
    }


    public void veriekleilac(View vw) {

        ilacadtxt = ilacad.getText().toString();
        tarihtxt = tarih.getText().toString();
        saattxt = saat.getText().toString();
        doztxt = doz.getText().toString();


        mData = new HashMap<>();
        mData.put("ilacad", ilacadtxt);
        mData.put("doz", doztxt);
        mData.put("tarih", tarihtxt);
        mData.put("saat", saattxt);


        mFireStore.collection("hayvanlar").document(veri).collection("ilaclar").document()
                .set(mData)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(getContext(), "ilac Kaydedildi.", Toast.LENGTH_SHORT).show();

                            NavDirections action = new ActionOnlyNavDirections(R.id.action_nav_IlacEkle_to_nav_Ilaclar);
                            Navigation.findNavController(vw).navigate(action);

                        } else {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}