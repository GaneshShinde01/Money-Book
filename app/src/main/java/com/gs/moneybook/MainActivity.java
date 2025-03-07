package com.gs.moneybook;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.android.material.navigation.NavigationView;
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
    private DrawerLayout drawerLayout;
    private int loggedInUserId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.toolBarMain);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layoutMain);
        binding.drawerMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });

        View headerView = null;
        if (binding.sideNavView != null) {
            headerView = binding.sideNavView.getHeaderView(0);
        }
        if (headerView != null) {
            ImageView profileImage = headerView.findViewById(R.id.profile_image);
            TextView userName = headerView.findViewById(R.id.usernameSideNav);
            TextView userEmail = headerView.findViewById(R.id.useremailSideNav);
        }





        binding.sideNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                if(itemId == R.id.side_nav_account){
                    Toast.makeText(MainActivity.this, "Account", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.side_nav_calendar) {
                    Toast.makeText(MainActivity.this, "Calendar", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.side_nav_notification) {
                    Toast.makeText(MainActivity.this, "Notification", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.side_nav_help) {
                    Toast.makeText(MainActivity.this, "Help", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.side_nav_backup) {
                    Toast.makeText(MainActivity.this, "Back-up", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                }

                drawerLayout.close();

                return false;
            }
        });


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
                    if (binding.navigationView != null && binding.drawerLayoutMain.isDrawerOpen(binding.navigationView)) {
                        binding.drawerLayoutMain.closeDrawer(binding.navigationView);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        if(android.R.id.home == itemId){
            onBackPress();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void onBackPress() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 1){
            getSupportFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }

    }

    // Enable drawer method
    private void enableDrawer() {
        binding.drawerLayoutMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        if (binding.navigationView != null) {
            binding.navigationView.setVisibility(View.VISIBLE);  // Show the navigation view
        }
        binding.drawerMenuIcon.setVisibility(View.VISIBLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle("");
        }
    }

    // Disable drawer method
    private void disableDrawer() {
        binding.drawerLayoutMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        if (binding.navigationView != null) {
            binding.navigationView.setVisibility(View.GONE);  // Hide the navigation view
        }
        binding.drawerMenuIcon.setVisibility(View.GONE);

        // Set toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
