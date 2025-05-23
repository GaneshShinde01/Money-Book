package com.gs.moneybook.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.gs.moneybook.Database.DBHelper;
import com.gs.moneybook.MainActivity;
import com.gs.moneybook.R;
import com.gs.moneybook.TestActivity;
import com.gs.moneybook.databinding.FragmentLoginBinding;
import com.gs.moneybook.Model.UserModel;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private DBHelper dbHelper;
    private final int loggedInUserId = 1;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "user_session";
    public static final String KEY_LOGGEDIN_USREMAIL = "userEmail";
    public static final String KEY_LOGIN_SUCCESS = "loginSuccess";


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using ViewBinding
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        dbHelper = DBHelper.getInstance(getContext());

        binding.skipToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        // Set up the login button click listener
        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (validateInputs(email, password)) {
                if (dbHelper.checkUserExists(email)) {
                    UserModel user = dbHelper.getUserByEmail(email);
                    if (user != null && user.getPassword().equals(password)) {

                        storeToSharedPreferences();
                        // Show popup dialog for login successful
                        showLoginSuccessPopup();

                    } else {
                        Toast.makeText(getContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                    showLoginFailPopup();
                }
            }
        });

        binding.tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TestActivity)getActivity()).loadFragment(new ForgetPasswordFragment(),"ForgetPasswordFragment");
            }
        });

        binding.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TestActivity)getActivity()).loadFragment(new RegisterFragment(),"RegisterFragment");
            }
        });


        //Handling back press
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                // Check if the back stack is empty
                if (fragmentManager.getBackStackEntryCount() > 1) {
                    // If there are fragments in the back stack, pop the current fragment
                    fragmentManager.popBackStack();
                } else {
                    // If no fragments in the back stack, finish the activity
                    requireActivity().finish();
                }
            }
        });




        return binding.getRoot();
    }

    private void showLoginSuccessPopup() {

        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.login_dialog,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);
//        builder.setTitle("Login Successful");
//        builder.setMessage("You have successfully logged in.");
//        builder.setCancelable(false); // Make it non-dismissible
        AlertDialog dialog = builder.create();
        dialog.show();

        /*Window window = dialog.getWindow();
        if(window != null){
            window.setLayout(600,1200);
        }
*/

        TextView userName = dialogView.findViewById(R.id.userNameLoginSuccess);
        String userNameFromDb = dbHelper.getUserNameById(loggedInUserId);
        userName.setText(userNameFromDb);

        // Automatically dismiss the dialog and redirect after 2 seconds (2000 milliseconds)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            dialog.dismiss(); // Dismiss the dialog
            // Redirect to MainActivity after successful login
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish(); // Close the login activity so the user can't go back to it
        }, 2000); // Delay of 2 seconds (2000 milliseconds)
    }

    private void showLoginFailPopup(){

        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.login_failed_dialog,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);
        builder.setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.show();

        TextView userEmail = dialogView.findViewById(R.id.userEmailLoginFail);
        String userEmailFromLoginPage = binding.etEmail.getText().toString();
        userEmail.setText(userEmailFromLoginPage);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            dialog.dismiss(); // Dismiss the dialog

        }, 2000); // Delay of 2 seconds (2000 milliseconds)

    }

    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            binding.etEmail.setError("Email is required");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            binding.etPassword.setError("Password is required");
            return false;
        }

        return true;
    }

    private void storeToSharedPreferences(){

        sharedPreferences = getContext().getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String userEmail = binding.etEmail.getText().toString().trim();
       // int userId = dbHelper.getUserIdByEmail(userEmail);
        editor.putString(KEY_LOGGEDIN_USREMAIL,userEmail);
        editor.putBoolean(KEY_LOGIN_SUCCESS,true);
        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    //check
}
