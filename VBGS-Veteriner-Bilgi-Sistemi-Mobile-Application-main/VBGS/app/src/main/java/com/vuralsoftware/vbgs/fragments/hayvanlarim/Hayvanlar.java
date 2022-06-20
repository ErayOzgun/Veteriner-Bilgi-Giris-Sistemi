package com.vuralsoftware.vbgs.fragments.hayvanlarim;


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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.vuralsoftware.vbgs.R;
import com.vuralsoftware.vbgs.activity.BottomView;
import com.vuralsoftware.vbgs.adapters.hayvanlaradapter;
import com.vuralsoftware.vbgs.models.hayvaninfo;

public class Hayvanlar extends Fragment {

    private String id;
    private HayvanlarViewModel mViewModel;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private FirebaseUser mUser= FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference hayvanref=db.collection("kullanicilar").document(mUser.getUid()).collection("hayvanlar");
    private BottomView nvb;
    private hayvanlaradapter Hadapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        nvb = new BottomView();
        View v=inflater.inflate(R.layout.fragment_hayvanlar,container,false);


        setUpRecyclerView(v);


        // Hayvan Ekle Floating Button Click Dinleme Fonksiyonu

        FloatingActionButton flbtn = v.findViewById(R.id.hayvanlarhayvanekle);
        flbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = new ActionOnlyNavDirections(R.id.action_nav_Hayvanlar_to_nav_HayvanEkle);
                Navigation.findNavController(v).navigate(action);
            }
        });

        return v;
    }

    //Kullanıcı GETUid'sine göre Veri Tabanından Hayvanları Çekme

    private void setUpRecyclerView(View v) {
        Query query = hayvanref;
        FirestoreRecyclerOptions<hayvaninfo> options = new FirestoreRecyclerOptions.Builder<hayvaninfo>()
                .setQuery(query, hayvaninfo.class)
                .build();


        Hadapter = new hayvanlaradapter(options);

        RecyclerView recyclerView = v.findViewById(R.id.hayvanlar_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(Hadapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Hadapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

        Hadapter.setOnItemClickListener(new hayvanlaradapter.onItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                hayvaninfo hayvaninfo= documentSnapshot.toObject(com.vuralsoftware.vbgs.models.hayvaninfo.class);

                id = documentSnapshot.getId();
                String path=documentSnapshot.getReference().getPath();

                //Hayvan Bilgi Sayfasına Küpe No yani id gönderme
                nvb.bilgieklehayvan(id);

                NavDirections action = new ActionOnlyNavDirections(R.id.action_nav_Hayvanlar_to_nav_HayvanBilgi);
                Navigation.findNavController(v).navigate(action);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Hadapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        Hadapter.stopListening();
    }

    
}