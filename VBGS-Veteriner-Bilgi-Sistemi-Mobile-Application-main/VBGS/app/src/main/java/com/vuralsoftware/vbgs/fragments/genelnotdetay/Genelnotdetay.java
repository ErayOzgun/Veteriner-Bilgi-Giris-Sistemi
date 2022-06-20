package com.vuralsoftware.vbgs.fragments.genelnotdetay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vuralsoftware.vbgs.activity.BottomView;
import com.vuralsoftware.vbgs.R;

public class Genelnotdetay extends Fragment {

    private GenelnotdetayViewModel mViewModel;
    private TextView baslik,not;
    private FirebaseUser mUser;
    private FirebaseFirestore mFireStore;
    private String veri,veri1;
    private DocumentReference docref;
    private BottomView nvb;

    public static Genelnotdetay newInstance() {
        return new Genelnotdetay();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_genelnotdetay, container, false);
        nvb= new BottomView();
        veri1 = nvb.bilginotal();

        baslik = v.findViewById(R.id.NotDetayBasliktxt);
        not = v.findViewById(R.id.NotDetaynottxt);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFireStore = FirebaseFirestore.getInstance();

        docref= mFireStore.collection("kullanicilar").document(mUser.getUid()).collection("notlar").document(veri1);
        docref.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot dc = task.getResult();
                            if (dc.exists()){
                                baslik.setText(dc.getData().get("baslik").toString());
                                not.setText(dc.getData().get("not").toString());
                            }
                            else{
                                Toast.makeText(getContext(), "Hata Oluştuuuu", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getContext(), "Hata Oluştu", Toast.LENGTH_SHORT).show();
                        }
                    }
                });






        return v;
    }



}