package com.vuralsoftware.vbgs.fragments.hayvanbilgi;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.vuralsoftware.vbgs.activity.BottomView;
import com.vuralsoftware.vbgs.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Hayvanbilgi extends Fragment {

    private HayvanbilgiViewModel mViewModel;
    private NavController navController;
    private String kupe,fas;
    private BottomView nvb;
    private static List dizi ,dizi2;
    private TextView kupeno,tur,cins,yas,cinsiyet,sahip,adres;
    private FirebaseUser mUser;
    private FirebaseFirestore mFireStore;
    private DocumentReference docref;
    private Font yazii,baslikfont;
    private int STORAGE_PERMISSION_CODE = 1;
    String[] aa= {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private CollectionReference noteref;
    public static Hayvanbilgi newInstance() {
        return new Hayvanbilgi();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.fragment_hayvanbilgi, container, false);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFireStore = FirebaseFirestore.getInstance();


        BaseFont arial=null;
        BaseFont baslik=null;


        try {
            baslik = BaseFont.createFont("assets/Trueno.otf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
            arial = BaseFont.createFont("assets/trone.otf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        yazii=new Font(arial);
        baslikfont=new Font(baslik);


        // Hayvan bilgilerini aktaracağımız TEXTVİEW'leri İçeri Alma

        kupeno = vw.findViewById(R.id.bilgi_kupegenel);
        tur = vw.findViewById(R.id.bilgi_turgenel);
        cins = vw.findViewById(R.id.bilgi_cinsgenel);
        yas = vw.findViewById(R.id.bilgi_yasgenel);
        cinsiyet = vw.findViewById(R.id.bilgi_cinsiyetgenel);
        sahip = vw.findViewById(R.id.bilgi_sahipgenel);
        adres = vw.findViewById(R.id.bilgi_adresgenel);

        //Hayvanlar Sayfasından gelen Küpe no alma

        nvb = new BottomView();
        kupe = nvb.bilgialhayvan();

        fas=Environment.getExternalStorageDirectory()+"/"+kupe+".pdf";
        nvb.bilginotekle(fas);


        dizi=new List();
        noteref=mFireStore.collection("hayvanlar").document(kupe).collection("asilar");

        noteref.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final boolean add = dizi.add(new ListItem("Aşı: "+document.getData().get("asiad").toString()+"           Durumu: " +document.getData().get("durum").toString()+"           Yapılan Tarih: " +document.getData().get("tarih").toString()+"    "+document.getData().get("saat").toString(),yazii));
                            }
                        }
                    }
                });

        dizi2=new List();
        noteref=mFireStore.collection("hayvanlar").document(kupe).collection("ilaclar");

        noteref.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final boolean add = dizi2.add(new ListItem("İlaç: "+document.getData().get("ilacad").toString()+"           Doz: " +document.getData().get("doz").toString()+"           Yapılan Tarih: " +document.getData().get("tarih").toString()+"    "+document.getData().get("saat").toString(),yazii));
                            }
                        }
                    }
                });

        docref= mFireStore.collection("hayvanlar").document(kupe);

        // Küpe No ya göre Firestoredan  Hayvan Bilgileri Çekme

        docref.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot dc = task.getResult();
                            if (dc.exists()){
                                kupeno.setText(dc.getData().get("kupeno").toString());
                                tur.setText(dc.getData().get("tur").toString());
                                cins.setText(dc.getData().get("cins").toString());
                                yas.setText(dc.getData().get("yas").toString());
                                cinsiyet.setText(dc.getData().get("cinsiyet").toString());
                                sahip.setText(dc.getData().get("sahip").toString());
                                adres.setText(dc.getData().get("sahipadres").toString());
                            }
                            else{
                                Toast.makeText(getContext(), "Hayvan Bulunamadı", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getContext(), "Hata Oluştu", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //İlaclar Butonu Click Dinleme Fonksiyonu

        Button fab = vw.findViewById(R.id.btnilacgenel);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ilacekle(v);
            }
        });

        //Notlar Butonu Click Dinleme Fonksiyonu

        Button fab2 = vw.findViewById(R.id.btnnot);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notlar(v);
            }
        });

        //Asilar Butonu Click Dinleme

        Button fab3 = vw.findViewById(R.id.btnasi);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                asilar(v);
            }
        });

        Button fab4 = vw.findViewById(R.id.dısaaktar);
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    dısaaktar(v);
                } else {
                    requestStoragePermission();
                }

            }
        });

        return vw;
    }

    // İlaçlar Sayfasına Yönlendirme

    public void ilacekle(View view){

        nvb.bilginot(kupe);
        NavDirections action = new ActionOnlyNavDirections(R.id.action_nav_HayvanBilgi_to_nav_Ilaclar);
        Navigation.findNavController(view).navigate(action);
    }

    //Notlar Sayfasına Yönlendirme

    public void notlar(View view){

        //Küpe Bilgisi Gönderme
        nvb.bilginot(kupe);

        NavDirections action = new ActionOnlyNavDirections(R.id.action_nav_HayvanBilgi_to_nav_Notlar);
        Navigation.findNavController(view).navigate(action);
    }

    public void asilar(View view){

        //Küpe Bilgisi Gönderme
        nvb.bilginot(kupe);

        NavDirections action = new ActionOnlyNavDirections(R.id.action_nav_HayvanBilgi_to_nav_Asilar);
        Navigation.findNavController(view).navigate(action);
    }

    private  void dısaaktar(View view){




        Document pdfDosyam = new Document();

            // Her Sayfayı A4 Boyutuna getirdik
            pdfDosyam.setPageSize(PageSize.A4);

            // Oluşturma tarihini girdik
            pdfDosyam.addCreationDate();

            // Yazar ve Kurucu adını ekledik
            pdfDosyam.addAuthor("PDF Yazar Adı");
            pdfDosyam.addCreator("PDF Oluşturucu Adı");

            pdfDosyam.addTitle("PDF Başlığı");
            pdfDosyam.addSubject("PDF Açıklaması");

            try {


                // Telefon belleğinde test.pdf isimli bir pdf dosyası oluşturduk ve açtık
                PdfWriter.getInstance(pdfDosyam, new FileOutputStream(new File(Environment.getExternalStorageDirectory(),""+kupe+".pdf")));


                pdfDosyam.open();

                // Başlığı tanımladığımız yer
                Paragraph baslik = new Paragraph("Hayvan Bilgileri",baslikfont);
                baslik.setAlignment(Element.ALIGN_CENTER);
                pdfDosyam.add(baslik);

                pdfDosyam.add(new Paragraph("\n"));

                List liste2 = new List();
                liste2.add(new ListItem("Küpe No: " + kupe.toString(),yazii));
                liste2.add(new ListItem("Tür: " + tur.getText().toString(),yazii));
                liste2.add(new ListItem("Cins: " + cins.getText().toString(),yazii));
                liste2.add(new ListItem("Yaş: " + yas.getText().toString(),yazii));
                liste2.add(new ListItem("Cinsiyet: " + cinsiyet.getText().toString(),yazii));
                liste2.add(new ListItem("Sahip: " + sahip.getText().toString(),yazii));
                liste2.add(new ListItem("Sahip Adres: " + adres.getText().toString(),yazii));


                pdfDosyam.add(liste2);

                // Separator tanımlama ve renk ekleme
                LineSeparator lineSeparator = new LineSeparator();
                lineSeparator.setLineColor(new BaseColor(255, 50, 50, 68));

                pdfDosyam.add(new Paragraph("\n"));
                // Separator ekleme
                pdfDosyam.add(lineSeparator);

                // Bir boşlukluk satır bırakma
                pdfDosyam.add(new Paragraph("\n"));

                // İçerik eklemek
                Paragraph veriler_icerik = new Paragraph("Aşı Bilgisi",baslikfont);
                veriler_icerik.setAlignment(Element.ALIGN_CENTER);
                pdfDosyam.add(veriler_icerik);
                pdfDosyam.add(new Paragraph("\n"));



                pdfDosyam.add(dizi);

                // Separator tanımlama ve renk ekleme
                LineSeparator lineSeparator2 = new LineSeparator();
                lineSeparator2.setLineColor(new BaseColor(255, 50, 50, 68));

                pdfDosyam.add(new Paragraph("\n"));
                // Separator ekleme
                pdfDosyam.add(lineSeparator2);

                // Bir boşlukluk satır bırakma
                pdfDosyam.add(new Paragraph("\n"));

                // İçerik eklemek
                Paragraph veriler_icerik2 = new Paragraph("İlaç Bilgisi",baslikfont);
                veriler_icerik2.setAlignment(Element.ALIGN_CENTER);
                pdfDosyam.add(veriler_icerik2);
                pdfDosyam.add(new Paragraph("\n"));

                pdfDosyam.add(dizi2);

                pdfDosyam.close();


                System.out.println(fas);
                NavDirections action = new ActionOnlyNavDirections(R.id.action_nav_HayvanBilgi_to_nav_PDF);
                Navigation.findNavController(view).navigate(action);


                Toast.makeText(getContext(),"İşlem Başarılı Pdf Oluştu. Dosya Klasörü:"+fas,Toast.LENGTH_LONG).show();

            } catch (DocumentException e) {
                Toast.makeText(getContext(),"İşlem Başarısız, Döküman Hatası!",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                Toast.makeText(getContext(),"İşlem Başarısız, Dosya Bulunamadı Hatası! Uygulamaya gerekli izinleri veriniz!",Toast.LENGTH_LONG).show();
                Log.e("hata","hata: "+e.toString());
                e.printStackTrace();
            }

        }

    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                String.valueOf(aa))) {

            new AlertDialog.Builder(getContext())
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }



}