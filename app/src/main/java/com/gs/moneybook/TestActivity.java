package com.gs.moneybook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gs.moneybook.Fragments.LoginFragment;
import com.gs.moneybook.Fragments.RegisterFragment;
import com.gs.moneybook.databinding.ActivityTestBinding;

public class TestActivity extends AppCompatActivity {
    ActivityTestBinding binding;
    private static final String SHARED_PREF_NAME = "loggedInUser";
    public static final String KEY_LOGGEDIN_USERID = "userId";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

 /*       SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String user = prefs.getString(KEY_LOGGEDIN_USERID, null);

        if (user != null) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, TestActivity.class));
            //loadFragment(new LoginFragment(),"LoginFragment");
        }
        finish(); // Don't show MainActivity again

*/

       // startActivity(new Intent(this, MainActivity.class));
        //finish();

        loadFragment(new LoginFragment(),"LoginFragment");

    }

    public void loadFragment(Fragment fragment, String tag){
        FragmentManager fm = getSupportFragmentManager();
        Fragment existingFragment = fm.findFragmentByTag(tag);

        if (existingFragment != null) {
            // Fragment is already in the container, just show it
            fm.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.containerTest, fragment,tag);
            ft.addToBackStack(tag);
            ft.commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}