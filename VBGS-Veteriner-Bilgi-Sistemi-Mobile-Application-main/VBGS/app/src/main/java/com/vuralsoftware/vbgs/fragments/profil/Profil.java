package com.vuralsoftware.vbgs.fragments.profil;

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
import com.vuralsoftware.vbgs.R;

public class Profil extends Fragment {

    private ProfilViewModel mViewModel;
    private TextView ad,email;
    private FirebaseUser mUser;
    private FirebaseFirestore mFireStore;
    private DocumentReference docref;

    public Profil() {
    }


    public static Profil newInstance() {
        return new Profil();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profil, container, false);

        ad =v.findViewById(R.id.profil_ad);
        email = v.findViewById(R.id.profil_email);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFireStore = FirebaseFirestore.getInstance();

       docref= mFireStore.collection("kullanicilar").document(mUser.getUid());

       docref.get()
               .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                       if (task.isSuccessful()){
                           DocumentSnapshot dc = task.getResult();
                           if (dc.exists()){
                               ad.setText(dc.getData().get("ad").toString());
                               email.setText(dc.getData().get("mail").toString());
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