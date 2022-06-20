package com.vuralsoftware.vbgs.fragments.asiekle;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
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

public class Asiekle extends Fragment {

    private AsiekleViewModel mViewModel;
    private FirebaseUser mUser;
    private HashMap<String, Object> mData;
    private FirebaseFirestore mFireStore;
    private BottomView nvb;
    private RadioGroup durumasi;
    private String veri, asiadtxt, tarihtxt, saattxt;
    private EditText asiad, tarih, saat;

    public static Asiekle newInstance() {
        return new Asiekle();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_asiekle, container, false);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFireStore = FirebaseFirestore.getInstance();

        //İlaclar Sayfasından gelen Veri alma

        nvb = new BottomView();
        veri = nvb.bilginotal();

        //Edittext içeri Aktarma;
        durumasi = v.findViewById(R.id.durumasi);
        asiad = v.findViewById(R.id.asiad);
        tarih = v.findViewById(R.id.asitarih);
        saat = v.findViewById(R.id.asisaat);

        tarih.setText( new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
        saat.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));

        Button btnkaydet = v.findViewById(R.id.kaydetbtnasi);

        btnkaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(asiad.getText().toString())){
                    if (durumasi.getCheckedRadioButtonId() != -1){
                        if(!TextUtils.isEmpty(tarih.getText().toString())) {
                            if (!TextUtils.isEmpty(saat.getText().toString())) {

                                veriekleasi(v);

                            } else {Toast.makeText(getContext(),"Saat Boş Olamaz",Toast.LENGTH_SHORT).show();}
                        }else {Toast.makeText(getContext(),"Tarih Boş Olamaz",Toast.LENGTH_SHORT).show();}
                    }else {Toast.makeText(getContext(),"Durum Boş Olamaz",Toast.LENGTH_SHORT).show();}
                }else {Toast.makeText(getContext(),"Aşı Adı Boş Olamaz",Toast.LENGTH_SHORT).show();}
            }
        });

        return v;
    }


    public void veriekleasi(View vw) {

        asiadtxt = asiad.getText().toString();
        tarihtxt = tarih.getText().toString();
        saattxt = saat.getText().toString();


        mData = new HashMap<>();
        mData.put("asiad", asiadtxt);
        if (durumasi.getCheckedRadioButtonId()==R.id.yapildi){
            mData.put("durum", "Yapıldı");
        }
        else if(durumasi.getCheckedRadioButtonId()==R.id.yapilmadi){
            mData.put("durum", "Yapılmadı");
        }

        mData.put("tarih", tarihtxt);
        mData.put("saat", saattxt);


        mFireStore.collection("hayvanlar").document(veri).collection("asilar").document()
                .set(mData)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(getContext(), "Aşı Kaydedildi.", Toast.LENGTH_SHORT).show();

                            NavDirections action = new ActionOnlyNavDirections(R.id.action_nav_AsiEkle_to_nav_Asilar);
                            Navigation.findNavController(vw).navigate(action);

                        } else {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                });

    }
}