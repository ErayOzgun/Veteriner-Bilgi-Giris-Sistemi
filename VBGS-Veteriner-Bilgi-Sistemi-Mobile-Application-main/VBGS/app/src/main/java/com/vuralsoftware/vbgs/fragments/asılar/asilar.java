package com.vuralsoftware.vbgs.fragments.asılar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.vuralsoftware.vbgs.activity.BottomView;
import com.vuralsoftware.vbgs.adapters.AsilarAdapter;
import com.vuralsoftware.vbgs.models.asimodel;
import com.vuralsoftware.vbgs.R;

public class asilar extends Fragment {

    private AsilarViewModel mViewModel;

    //VeriTabanı Veri Çekme

    BottomView nvb = new BottomView();
    String veri;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private FirebaseUser mUser= FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference noteref;

    private AsilarAdapter adapterNote;

    public static asilar newInstance() {
        return new asilar();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_asilar,container,false);

        //küpe verisi çekme
        veri=nvb.bilginot1();


        noteref=db.collection("hayvanlar").document(veri).collection("asilar");
        setUpRecyclerView(v);

        //Not Ekle Hayvan Verisi Gönderme ve Yönlendirme

        FloatingActionButton fbtn=v.findViewById(R.id.asieklebtn);
        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nvb.bilginotekle(veri);
                NavDirections action = new ActionOnlyNavDirections(R.id.action_nav_Asilar_to_nav_AsiEkle);
                Navigation.findNavController(v).navigate(action);
            }
        });

        return v;
    }



    //Hayvana ait Notları Veri Tabanından Çekme

    private void setUpRecyclerView(View v) {
        Query query = noteref;
        FirestoreRecyclerOptions<asimodel> options = new FirestoreRecyclerOptions.Builder<asimodel>()
                .setQuery(query,asimodel.class)
                .build();


        adapterNote = new AsilarAdapter(options);

        RecyclerView recyclerView = v.findViewById(R.id.asilar_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterNote);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapterNote.deleteItem(viewHolder.getAdapterPosition());
            }


        }).attachToRecyclerView(recyclerView);



    }

    //Yeni Not Ekleme Dinleme

    @Override
    public void onStart() {
        super.onStart();
        adapterNote.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterNote.stopListening();
    }

}