package com.vuralsoftware.vbgs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vuralsoftware.vbgs.R;


import java.util.HashMap;


public class Kayit extends AppCompatActivity {

    private EditText mail,sifre,ad;
    private String txtmail,txtsifre,txtad;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private HashMap<String,Object> mData ;
    private FirebaseFirestore mFireStore;
    private DatabaseReference mReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);

        //EditText İçeri Aktarma

        mail = (EditText)findViewById(R.id.kayit_ol_email);
        sifre = (EditText)findViewById(R.id.kayit_ol_sifre);
        ad = (EditText)findViewById(R.id.kayit_ol_ad);
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        mFireStore = FirebaseFirestore.getInstance();

    }

    //Geri Butonu Fonksiyonu

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Kayit.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //Kayıtol sayfası Kayitol butonu fonksiyonu

    public void kayitol(View view){
        txtmail = mail.getText().toString();
        txtsifre = sifre.getText().toString();
        txtad = ad.getText().toString();

        if (!TextUtils.isEmpty(txtmail) && !TextUtils.isEmpty(txtsifre) && !TextUtils.isEmpty(txtad)) {

            mAuth.createUserWithEmailAndPassword(txtmail, txtsifre)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mUser = mAuth.getCurrentUser();
                                mData = new HashMap<>();
                                mData.put("ad", txtad);
                                mData.put("mail", txtmail);
                                mData.put("sifre", txtsifre);
                                mData.put("id", mUser.getUid());

                                //firestore

                                mFireStore.collection("kullanicilar").document(mUser.getUid())
                                        .set(mData)
                                            .addOnCompleteListener(Kayit.this, new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(Kayit.this, "Başarı ile Kayıt Oldunuz", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(Kayit.this,MainActivity.class);
                                                        startActivity(intent);
                                                        finish();

                                                    }
                                                    else{
                                                        Toast.makeText(Kayit.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });

                                //RealTime Database


/*
                                mReference.child("kullanicilar").child(mUser.getUid())
                                        .setValue(mData)
                                        .addOnCompleteListener(Kayit.this, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Kayit.this, "Başarı ile Kayıt Oldunuz", Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(Kayit.this,MainActivity.class);
                                                    startActivity(intent);
                                                    finish();

                                                }
                                                else{
                                                    Toast.makeText(Kayit.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });*/

                            } else {
                                Toast.makeText(Kayit.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }else{

            Toast.makeText(Kayit.this, "Ad,Mail Veya Şifre Boş Olamaz.", Toast.LENGTH_SHORT).show();

        }

    }
}