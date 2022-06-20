package com.vuralsoftware.vbgs.fragments.ilaclar;

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
import com.vuralsoftware.vbgs.activity.BottomView;
import com.vuralsoftware.vbgs.adapters.IlaclarAdapter;
import com.vuralsoftware.vbgs.models.ilacmodel;
import com.vuralsoftware.vbgs.R;

public class Ilaclar extends Fragment {

    private IlaclarViewModel mViewModel;

    //VeriTabanı Veri Çekme

    BottomView nvb = new BottomView();
    String veri;

    private FirebaseFirestore db;
    private FirebaseUser mUser;
    private CollectionReference ilacref;

    private IlaclarAdapter adapterIlaclar;

    public static Ilaclar newInstance() {
        return new Ilaclar();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_ilaclar,container,false);
        db=FirebaseFirestore.getInstance();
        mUser= FirebaseAuth.getInstance().getCurrentUser();

        //küpe verisi çekme
        veri=nvb.bilginot1();


        ilacref=db.collection("hayvanlar").document(veri).collection("ilaclar");
        setUpRecyclerView(v);

        //Not Ekle Hayvan Verisi Gönderme ve Yönlendirme

        FloatingActionButton fbtn=v.findViewById(R.id.ilaclareklebtn);
        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nvb.bilginotekle(veri);
                NavDirections action = new ActionOnlyNavDirections(R.id.action_nav_Ilaclar_to_nav_IlacEkle);
                Navigation.findNavController(v).navigate(action);
            }
        });

        return v;
    }



    //Hayvana ait Notları Veri Tabanından Çekme

    private void setUpRecyclerView(View v) {
        Query query = ilacref;
        FirestoreRecyclerOptions<ilacmodel> options = new FirestoreRecyclerOptions.Builder<ilacmodel>()
                .setQuery(query,ilacmodel.class)
                .build();


        adapterIlaclar = new IlaclarAdapter(options);

        RecyclerView recyclerView = v.findViewById(R.id.ilaclar_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterIlaclar);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapterIlaclar.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

        //Notlar Click Fonksiyonu

        adapterIlaclar.setOnItemClickListener(new IlaclarAdapter.onItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                ilacmodel not =documentSnapshot.toObject(com.vuralsoftware.vbgs.models.ilacmodel.class);
                String id=documentSnapshot.getReference().getId();
                String path=documentSnapshot.getReference().getPath();

            }
        });
    }

    //Yeni Not Ekleme Dinleme

    @Override
    public void onStart() {
        super.onStart();
        adapterIlaclar.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterIlaclar.stopListening();
    }

}