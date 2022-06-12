package com.example.eldroid_fernandez;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottom_navigation_View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottom_navigation_View=findViewById(R.id.bottom_navigation_View);

        NavController navController = Navigation.findNavController(this,R.id.nav_fragment);
        NavigationUI.setupWithNavController(bottom_navigation_View,navController);
    }




}