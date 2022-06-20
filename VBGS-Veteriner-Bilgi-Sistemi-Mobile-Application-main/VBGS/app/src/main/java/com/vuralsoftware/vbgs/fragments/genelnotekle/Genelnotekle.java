package com.vuralsoftware.vbgs.fragments.genelnotekle;

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

public class Genelnotekle extends Fragment {

    private GenelnotekleViewModel mViewModel;
    private FirebaseUser mUser;
    private HashMap<String,Object> mData ;
    private FirebaseFirestore mFireStore;
    private BottomView nvb;
    private String veri,basliktxt,nottxt,tarihtxt;
    private EditText baslik,not;
    public static Genelnotekle newInstance() {
        return new Genelnotekle();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_genelnotekle, container, false);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFireStore = FirebaseFirestore.getInstance();


        //Notlar Sayfasından gelen Veri alma

        nvb = new BottomView();
        veri=nvb.bilginotal();

        //Edittext içeri Aktarma;

        baslik = v.findViewById(R.id.notekle_baslik);
        not= v.findViewById(R.id.notekle_not);

        Button btnkaydet = v.findViewById(R.id.kaydetbtnnot);

        btnkaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(baslik.getText().toString())) {
                    if (!TextUtils.isEmpty(not.getText().toString())) {
                        verieklenot(v);
                    } else {Toast.makeText(getContext(),"Lütfen Notunuzu Yazınız",Toast.LENGTH_SHORT).show();}
                }else {Toast.makeText(getContext(),"Lütfen Not Başlığı Yazınız",Toast.LENGTH_SHORT).show();}
            }
        });

        return v;
    }

    //Veri Tabanına Veri Kayıt Fonksiyonu

    public void verieklenot(View vw){

        basliktxt = baslik.getText().toString();
        tarihtxt = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        nottxt = not.getText().toString();


        mData = new HashMap<>();
        mData.put("baslik", basliktxt);
        mData.put("not", nottxt);
        mData.put("tarih", tarihtxt);


        mFireStore.collection("kullanicilar").document(mUser.getUid()).collection("notlar").document()
                .set(mData)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Not Kaydedildi.", Toast.LENGTH_SHORT).show();

                            NavDirections action = new ActionOnlyNavDirections(R.id.action_nav_GenelNotEkle_to_nav_GenelNotlar);
                            Navigation.findNavController(vw).navigate(action);


                        }
                        else{
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

}