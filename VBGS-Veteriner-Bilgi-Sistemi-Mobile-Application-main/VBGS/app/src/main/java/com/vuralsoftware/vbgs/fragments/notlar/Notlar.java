package com.vuralsoftware.vbgs.fragments.notlar;

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
import com.vuralsoftware.vbgs.adapters.NoteAdapter;
import com.vuralsoftware.vbgs.models.note;
import com.vuralsoftware.vbgs.R;

public class Notlar extends Fragment {

    private NotlarViewModel mViewModel;

    //VeriTabanı Veri Çekme

    BottomView nvb = new BottomView();
    String veri;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private FirebaseUser mUser= FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference noteref;

    private NoteAdapter adapterNote;

    public static Notlar newInstance() {
        return new Notlar();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_notlar,container,false);

        //küpe verisi çekme
        veri=nvb.bilginot1();


        noteref=db.collection("kullanicilar").document(mUser.getUid()).collection("hayvanlar").document(veri).collection("notlar");
        setUpRecyclerView(v);

        //Not Ekle Hayvan Verisi Gönderme ve Yönlendirme

        FloatingActionButton fbtn=v.findViewById(R.id.notlareklebtn);
        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                nvb.bilginotekle(veri);
                NavDirections action = new ActionOnlyNavDirections(R.id.action_nav_Notlar_to_nav_Notekle);
                Navigation.findNavController(v).navigate(action);
            }
        });

        return v;
    }



    //Hayvana ait Notları Veri Tabanından Çekme

    private void setUpRecyclerView(View v) {
        Query query = noteref;
        FirestoreRecyclerOptions<note> options = new FirestoreRecyclerOptions.Builder<note>()
                .setQuery(query,note.class)
                .build();


        adapterNote = new NoteAdapter(options);

        RecyclerView recyclerView = v.findViewById(R.id.notlar_recyclerview);
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

        //Notlar Click Fonksiyonu

        adapterNote.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                note not =documentSnapshot.toObject(com.vuralsoftware.vbgs.models.note.class);
                String id=documentSnapshot.getReference().getId();
                String path=documentSnapshot.getReference().getPath();

                nvb.bilginotekle(id);

                NavDirections action = new ActionOnlyNavDirections(R.id.action_nav_Notlar_to_nav_NotDetay);
                Navigation.findNavController(v).navigate(action);

            }
        });
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