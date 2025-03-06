package com.gs.moneybook;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gs.moneybook.Fragments.AddExpenseFragment;
import com.gs.moneybook.Fragments.AddIncomeFragment;
import com.gs.moneybook.Fragments.BlankFragment;
import com.gs.moneybook.Fragments.DashboardFragment;
import com.gs.moneybook.Fragments.ProfileFragment;
import com.gs.moneybook.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private boolean doubleBackToExitPressedOnce = false;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.toolBarMain);
        setSupportActionBar(toolbar);


        // Load the default fragment (DashboardFragment) initially
        if (savedInstanceState == null) {
            loadFragment(new DashboardFragment(),"Dashboard");
        }

        // Set BottomNavigationView listener
        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.navDashboard) {
                    loadFragment(new DashboardFragment(),"Dashboard");

                    return true;
                } else if (id == R.id.navAdd) {
                    showPopUpMenu();
                    loadFragment(new BlankFragment(),"");
                    return true;
                } else if (id == R.id.navProfile) {
                    loadFragment(new ProfileFragment(),"Profile");
                    return true;
                }
                return false;
            }
        });

        // Handle back press logic
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.containerMain);
                if (!(currentFragment instanceof DashboardFragment)) {
                    loadFragment(new DashboardFragment(),"Dashboard");
                    binding.bottomNav.setSelectedItemId(R.id.navDashboard);  // Set the home item selected
                } else {
                    if (binding.drawerLayout.isDrawerOpen(binding.navigationView)) {
                        binding.drawerLayout.closeDrawer(binding.navigationView);
                    } else if (doubleBackToExitPressedOnce) {
                        finish();
                    } else {
                        doubleBackToExitPressedOnce = true;
                        Toast.makeText(MainActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
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

    // Show popup menu
    private void showPopUpMenu() {
        View anchor = findViewById(R.id.navAdd);
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_b_nav, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.Income) {
                    loadFragment(new AddIncomeFragment(),"Add Income");
                    return true;
                } else if (id == R.id.expense) {
                    loadFragment(new AddExpenseFragment(),"Add Expense");
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    // Load fragment method
    public void loadFragment(Fragment fragment, String toolBarTitle) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.containerMain, fragment, fragment.getClass().getSimpleName());
        ft.commit();



        binding.toolBarTitle.setText(toolBarTitle);


        // Enable the drawer only for DashboardFragment
        if (fragment instanceof DashboardFragment) {
            enableDrawer();


        } else {
            disableDrawer();

        }
    }

    // Enable drawer method
    private void enableDrawer() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        binding.navigationView.setVisibility(View.VISIBLE);  // Show the navigation view
        binding.drawerMenuIcon.setVisibility(View.VISIBLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle("");
        }
    }

    // Disable drawer method
    private void disableDrawer() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        binding.navigationView.setVisibility(View.GONE);  // Hide the navigation view
        binding.drawerMenuIcon.setVisibility(View.GONE);

        // Set toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
