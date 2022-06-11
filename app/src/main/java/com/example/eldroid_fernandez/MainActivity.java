package com.example.eldroid_fernandez;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.databinding.DataBindingUtil;
import com.example.ELDROIDFernandez.databinding.ActivityMainBinding;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    ActivyMainBinding binding;

    BottomNavigationView bottom_navigation_View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding.ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottom_navigation_View.setOnItemSelectedListener(item) {
            switch (item.getItemID()) {

                case R.id.homeFragment:
                    break;
                case R.id.addFragment:
                    break;

            }

        }

        return true;




//        BottomNavigationView bottom_navigation_View = findViewById(R.id.bottom_navigation_View);
//        NavController navController = Navigation.findNavController(this, R.id.nav_fragment);
//
//        NavigationUI.setupActionBarWithNavController(this, navController);
//        NavigationUI.setupWithNavController(bottom_navigation_View, navController);
    }


}