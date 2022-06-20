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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vuralsoftware.vbgs.R;


public class MainActivity extends AppCompatActivity {

    private String packageContext;
    private EditText email,sifre;
    private String txtEmail,txtSifre,aa;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFireStore;
    private DatabaseReference mReference;
    private DocumentReference doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //EditTextleri İçeri Aktarma

        email = (EditText)findViewById(R.id.giris_yap_email);
        sifre = (EditText)findViewById(R.id.giris_yap_sifre);
        mAuth = FirebaseAuth.getInstance();
        mUser =  FirebaseAuth.getInstance().getCurrentUser();
        mFireStore = FirebaseFirestore.getInstance();

    }

    //Kayit ol butonu Fonksiyonu

    public void kayit_ol(View view){
        Intent intent = new Intent(MainActivity.this, Kayit.class);
        startActivity(intent);
        finish();
    }

    //Giriş Yap Butonu Fonksiyonu ve Veri Tabanından kontrol

    public void giris_yap(View view){
        txtEmail = email.getText().toString();
        txtSifre = sifre.getText().toString();

        if (!TextUtils.isEmpty(txtEmail) && !TextUtils.isEmpty(txtSifre)){

                mAuth.signInWithEmailAndPassword(txtEmail,txtSifre)
                        .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                mUser = mAuth.getCurrentUser();


                                //Navbar Sayfasına Yönlendirme

                                Intent intent = new Intent(MainActivity.this,BottomView.class);
                                startActivity(intent);
                                finish();
                            }
                        }) .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
        }else{
            Toast.makeText(this,"Email ve Şifre Alanı Boş Olamaz.",Toast.LENGTH_SHORT).show();
        }
    }
    //Geri Butonu Fonksiyon
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}