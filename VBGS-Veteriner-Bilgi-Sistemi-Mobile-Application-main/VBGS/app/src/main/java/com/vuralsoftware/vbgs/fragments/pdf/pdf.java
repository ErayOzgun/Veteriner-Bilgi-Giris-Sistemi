package com.vuralsoftware.vbgs.fragments.pdf;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.github.barteksc.pdfviewer.PDFView;
import com.vuralsoftware.vbgs.activity.BottomView;
import com.vuralsoftware.vbgs.R;

import java.io.File;

public class pdf extends Fragment {

    private PdfViewModel mViewModel;
    private BottomView nvb;
    private String veri;

    public static pdf newInstance() {
        return new pdf();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pdf_fragment, container, false);
        nvb=new BottomView();
        veri=nvb.bilginotal();
        System.out.println(veri);
        PDFView as = v.findViewById(R.id.pdfviewYonetmelik);
        as.fromFile(new File(veri)).load();

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PdfViewModel.class);
        // TODO: Use the ViewModel
    }

}