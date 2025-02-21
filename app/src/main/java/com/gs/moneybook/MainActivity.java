package com.gs.moneybook;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

//import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gs.moneybook.Fragments.AddExpenseFragment;
import com.gs.moneybook.Fragments.AddIncomeFragment;
import com.gs.moneybook.Fragments.DashboardFragment;
import com.gs.moneybook.Fragments.ProfileFragment;
import com.gs.moneybook.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Load the default fragment (Home1Fragment) initially
        if (savedInstanceState == null) {
            loadFragment(new DashboardFragment());
        }

        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if(id == R.id.navDashboard){
                    loadFragment(new DashboardFragment());
                    return true;
                } else if (id == R.id.navAdd) {
                    showPopUpMenu();
                    return true;
                } else if (id == R.id.navProfile) {
                    loadFragment(new ProfileFragment());
                    return true;
                }else{

                }

                return false;
            }
        });


        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {


                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.containerMain);
                // If the current fragment is not Home1Fragment, go to Home1Fragment
                if (!(currentFragment instanceof DashboardFragment)) {
                    loadFragment(new DashboardFragment());
                    binding.bottomNav.setSelectedItemId(R.id.navDashboard);  // Set the home item selected

                } else {
                    // If already in Home1Fragment, handle double back press to exit
                    if (doubleBackToExitPressedOnce) {
                        finish();
                    } else {
                        doubleBackToExitPressedOnce = true;
                        Toast.makeText(MainActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();

                        // Reset the flag after 2 seconds
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                doubleBackToExitPressedOnce = false;
                            }
                        }, 2000);
                    }
                }
            }
        });


    }


    private void showPopUpMenu(){
        View anchor = findViewById(R.id.navAdd);
        PopupMenu popupMenu = new PopupMenu(this,anchor);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_b_nav,popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int id = item.getItemId();

                if(id == R.id.Income){
                    Toast.makeText(MainActivity.this, "Income", Toast.LENGTH_SHORT).show();
                    loadFragment(new AddIncomeFragment());
                    return true;
                } else if (id == R.id.expense) {
                    Toast.makeText(MainActivity.this, "Expense", Toast.LENGTH_SHORT).show();
                    loadFragment(new AddExpenseFragment());
                    return true;
                }
                return false;
            }
        });

        popupMenu.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void loadFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.containerMain,fragment,fragment.getClass().getSimpleName());
        ft.commit();


    }

}