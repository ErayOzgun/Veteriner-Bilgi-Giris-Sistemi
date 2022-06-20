package com.vuralsoftware.vbgs.fragments.hayvanekle;


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
import com.vuralsoftware.vbgs.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class Hayvanekle extends Fragment {


    private HayvanViewModel mViewModel;
    private FirebaseUser mUser;
    private HashMap<String,Object> mData ;
    private FirebaseFirestore mFireStore;
    private EditText hayvanad,küpeno,tur,cins,yas,cinsiyet,sahip,adres,sad;
    private String txthayvanad,txtküpeno,txttur,txtcins,txtyas,txtcinsiyet,txtsahip,txtadres,tarih;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.fragment_hayvanekle, container, false);

        mUser= FirebaseAuth.getInstance().getCurrentUser();
        mFireStore = FirebaseFirestore.getInstance();

        // Verileri Alacağımız EditTextleri İçeri Aktarıyoruz

        hayvanad = vw.findViewById(R.id.hayvankayitad);
        küpeno = vw.findViewById(R.id.kupeno);
        tur = vw.findViewById(R.id.tur);
        cins = vw.findViewById(R.id.cins);
        yas = vw.findViewById(R.id.yas);
        cinsiyet = vw.findViewById(R.id.cinsiyet);
        sahip = vw.findViewById(R.id.hayvansahibi);
        adres = vw.findViewById(R.id.sahipiletisim);

        //Kaydet Butonu Click Dİnleme Fonksiyonu

        Button btn = vw.findViewById(R.id.kaydetbtnhayvan);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(TextUtils.isEmpty(hayvanad.getText().toString()))){
                    if (!(TextUtils.isEmpty(küpeno.getText().toString()))){
                        if(!(TextUtils.isEmpty(tur.getText().toString()))){
                            if(!(TextUtils.isEmpty(cins.getText().toString()))){
                                if(!(TextUtils.isEmpty(yas.getText().toString()))){
                                    if(!(TextUtils.isEmpty(cinsiyet.getText().toString()))){
                                        if(!(TextUtils.isEmpty(sahip.getText().toString()))){
                                            if(!(TextUtils.isEmpty(adres.getText().toString()))){
                                                veritabanihayvanekle(vw);
                                            }
                                            else{  Toast.makeText(getContext(), "Hayvan Sahibi İletişim Boş Olamaz", Toast.LENGTH_SHORT).show();}
                                        }
                                        else{  Toast.makeText(getContext(), "Hayvan Sahibi Boş Olamaz", Toast.LENGTH_SHORT).show();}
                                    }
                                    else{  Toast.makeText(getContext(), "Cinsiyet Boş Olamaz", Toast.LENGTH_SHORT).show();}
                                }
                                else{  Toast.makeText(getContext(), "Yaş Boş Olamaz", Toast.LENGTH_SHORT).show();}
                            }
                            else{  Toast.makeText(getContext(), "Cins Boş Olamaz", Toast.LENGTH_SHORT).show();}
                        }
                        else{  Toast.makeText(getContext(), "Tür Boş Olamaz", Toast.LENGTH_SHORT).show();}
                    }
                    else{  Toast.makeText(getContext(), "Küpe No Boş Olamaz", Toast.LENGTH_SHORT).show();}
                }
                else{  Toast.makeText(getContext(), "Hayvan Adı Boş Olamaz", Toast.LENGTH_SHORT).show();}

            }
        });



        return vw;
    }


    //Veri Tabanına hayvan ekleme Fonksiyonu

    public void veritabanihayvanekle(View vw){

        txthayvanad = hayvanad.getText().toString();
        txtküpeno = küpeno.getText().toString();
        txttur = tur.getText().toString();
        txtcins = cins.getText().toString();
        txtyas = yas.getText().toString();
        txtcinsiyet = cinsiyet.getText().toString();
        txtsahip = sahip.getText().toString();
        txtadres = adres.getText().toString();
        tarih = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());


        mData = new HashMap<>();
        mData.put("hayvanad", txthayvanad);
        mData.put("kupeno", txtküpeno);
        mData.put("tur", txttur);
        mData.put("cins", txtcins);
        mData.put("yas", txtyas);
        mData.put("cinsiyet", txtcinsiyet);
        mData.put("sahip", txtsahip);
        mData.put("sahipadres", txtadres);
        mData.put("Eklenme Tarihi", tarih);

        //firestore

        mFireStore.collection("kullanicilar").document(mUser.getUid()).collection("hayvanlar").document(txtküpeno)
                .set(mData)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            mFireStore.collection("hayvanlar").document(txtküpeno)
                                    .set(mData)
                                    .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {

                                                Toast.makeText(getContext(), "Hayvan Kayıt Edildi.", Toast.LENGTH_SHORT).show();

                                                NavDirections action = new ActionOnlyNavDirections(R.id.action_nav_HayvanEkle_to_nav_Hayvanlar);
                                                Navigation.findNavController(vw).navigate(action);


                                            } else {
                                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }


}