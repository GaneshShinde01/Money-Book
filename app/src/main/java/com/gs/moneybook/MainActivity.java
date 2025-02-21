package com.gs.moneybook;

import android.os.Bundle;
import android.view.MenuItem;

//import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gs.moneybook.Fragments.DashboardFragment;
import com.gs.moneybook.Fragments.ProfileFragment;
import com.gs.moneybook.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if(id == R.id.navDashboard){
                    loadFragment(new DashboardFragment());
                } else if (id == R.id.navAdd) {
                    loadFragment(new AddIEFragment());
                } else if (id == R.id.navProfile) {
                    loadFragment(new ProfileFragment());
                }else{

                }

                return false;
            }
        });


    }

    private void loadFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.containerMain,fragment);
        ft.commit();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}