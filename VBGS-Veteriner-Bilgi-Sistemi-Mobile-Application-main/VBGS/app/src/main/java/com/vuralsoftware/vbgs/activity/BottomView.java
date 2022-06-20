package com.vuralsoftware.vbgs.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vuralsoftware.vbgs.R;


public class BottomView extends AppCompatActivity {

    NavController navController;
    private static String tasi,tasi1,tasi2,tasi3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottomview);

        BottomNavigationView bottomnavigationmenu= findViewById(R.id.bottommenu);
        navController = Navigation.findNavController(this,R.id.Navhost);
        NavigationUI.setupWithNavController(bottomnavigationmenu,navController,false);

    }

    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, (DrawerLayout) null);
    }

    public void bilgieklehayvan(String a) { tasi = a; }
    public String bilgialhayvan() { return tasi; }
    public void bilginot(String a) { tasi1 = a; }
    public String bilginot1() { return tasi1; }
    public void bilginotekle(String a) { tasi2 = a; }
    public String bilginotal() { return tasi2; }
}