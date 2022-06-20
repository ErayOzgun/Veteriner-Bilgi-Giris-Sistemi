package com.vuralsoftware.vbgs.fragments.hayvanbilgigenel;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vuralsoftware.vbgs.activity.BottomView;
import com.vuralsoftware.vbgs.R;

import java.util.HashMap;

public class Hayvanbilgigenel extends Fragment {

    private HayvanbilgigenelViewModel mViewModel;
    private NavController navController;
    private String kupe,had,htur,hcins,hyas,hcinsiyet,hsahip,hadres;
    private BottomView nvb;
    private TextView kupeno,tur,cins,yas,cinsiyet,sahip,adres;
    private FirebaseUser mUser;
    private HashMap<String,Object> mData ;
    private FirebaseFirestore mFireStore;
    private DocumentReference docref,docref2;

    public static Hayvanbilgigenel newInstance() {
        return new Hayvanbilgigenel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vw= inflater.inflate(R.layout.fragment_hayvanbilgigenel, container, false);

        // Hayvan bilgilerini aktaracağımız TEXTVİEW'leri İçeri Alma

        kupeno = vw.findViewById(R.id.bilgi_kupegenel);
        tur = vw.findViewById(R.id.bilgi_turgenel);
        cins = vw.findViewById(R.id.bilgi_cinsgenel);
        yas = vw.findViewById(R.id.bilgi_yasgenel);
        cinsiyet = vw.findViewById(R.id.bilgi_cinsiyetgenel);
        sahip = vw.findViewById(R.id.bilgi_sahipgenel);
        adres = vw.findViewById(R.id.bilgi_adresgenel);

        //Hayvanlar Sayfasından gelen Küpe no alma

        nvb = new BottomView();
        kupe = nvb.bilgialhayvan();


        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFireStore = FirebaseFirestore.getInstance();
        docref= mFireStore.collection("hayvanlar").document(kupe);

        // Küpe No ya göre Firestoredan  Hayvan Bilgileri Çekme

        docref.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot dc = task.getResult();
                            if (dc.exists()){
                                kupeno.setText(dc.getData().get("kupeno").toString());
                                tur.setText(dc.getData().get("tur").toString());
                                cins.setText(dc.getData().get("cins").toString());
                                yas.setText(dc.getData().get("yas").toString());
                                cinsiyet.setText(dc.getData().get("cinsiyet").toString());
                                sahip.setText(dc.getData().get("sahip").toString());
                                adres.setText(dc.getData().get("sahipadres").toString());

                                had=dc.getData().get("hayvanad").toString();
                                htur=dc.getData().get("tur").toString();
                                hcins=dc.getData().get("cins").toString();
                                hyas=dc.getData().get("yas").toString();
                                hcinsiyet=dc.getData().get("cinsiyet").toString();
                                hsahip=dc.getData().get("sahip").toString();
                                hadres=dc.getData().get("sahipadres").toString();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),e.getMessage() , Toast.LENGTH_SHORT).show();
                NavDirections action = new ActionOnlyNavDirections(R.id.action_nav_HayvanBilgiGenel_to_nav_HayvanSorgula);
                Navigation.findNavController(vw).navigate(action);
            }
        });

        //İlaclar Butonu Click Dinleme Fonksiyonu

        Button fab = vw.findViewById(R.id.btnilacgenel);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ilacekle(v);
            }
        });

        //Asilar Butonu Click Dinleme


        Button fab3 = vw.findViewById(R.id.btnasi);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                asilar(v);
            }
        });

        Button fab4 = vw.findViewById(R.id.btnhyvekle);
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                veri_kopyala(v);
            }
        });


        return vw;
    }

    // İlaçlar Sayfasına Yönlendirme

    public void ilacekle(View view){

        nvb.bilginot(kupe);
        NavDirections action = new ActionOnlyNavDirections(R.id.action_nav_HayvanBilgiGenel_to_nav_Ilaclar);
        Navigation.findNavController(view).navigate(action);
    }

    public void asilar(View view){

        //Küpe Bilgisi Gönderme
        nvb.bilginot(kupe);

        NavDirections action = new ActionOnlyNavDirections(R.id.action_nav_HayvanBilgiGenel_to_nav_Asilar);
        Navigation.findNavController(view).navigate(action);
    }

    public void veri_kopyala( View vw){

        nvb = new BottomView();
        kupe = nvb.bilgialhayvan();


        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFireStore = FirebaseFirestore.getInstance();

       mData = new HashMap<>();
       mData.put("hayvanad",had);
       mData.put("kupeno",kupe);
       mData.put("tur",htur);
       mData.put("cins",hcins);
       mData.put("yas",hyas);
       mData.put("cinsiyet",hcinsiyet);
       mData.put("sahip",hsahip);
       mData.put("sahipadres",hadres);



        mFireStore.collection("kullanicilar").document(mUser.getUid()).collection("hayvanlar").document(kupe)
                .set(mData)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {


                            Toast.makeText(getContext(), "Hayvanlarınıza Eklendi.", Toast.LENGTH_SHORT).show();
                            NavDirections action = new ActionOnlyNavDirections(R.id.action_nav_HayvanBilgiGenel_to_nav_Hayvanlar);
                            Navigation.findNavController(vw).navigate(action);

                        }
                        else{
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });



    }

}