package com.gs.moneybook.Fragments;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.gs.moneybook.Database.DBHelper;
import com.gs.moneybook.MainActivity;
import com.gs.moneybook.R;
import com.gs.moneybook.databinding.FragmentProfileBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private FragmentProfileBinding binding;
    private Uri selectedImageUri;  // Store the selected image URI
    private boolean isImageSet = false;  // Track if the image is already set
    private DBHelper dbHelper;  // Add DBHelper instance
    private int loggedInUserId = 1; // Assuming a static user ID for now
    private View dialogView;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        dbHelper = new DBHelper(requireContext());  // Initialize the DBHelper

        loadProfileImage(loggedInUserId);  // Load the profile image from DB

        String userNameFromDB = dbHelper.getUserNameById(loggedInUserId);
        if (userNameFromDB != null){

            binding.userNameTextView.setText(userNameFromDB);
        }else {
            binding.userNameTextView.setText("Not Found");
        }


        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isImageSet) {
                    showImageOptions();  // Show options if image is already set
                } else {
                    openImagePicker();  // Directly open image picker if no image is set
                }
            }
        });

        // Other button click listeners
        setupButtonClickListeners();

        return view;
    }

    // Show options (View, Update, Delete) if image is set
    private void showImageOptions() {
        // You can use an AlertDialog or PopupMenu to display the options
        PopupMenu popupMenu = new PopupMenu(requireContext(), binding.profileImage);
        popupMenu.getMenuInflater().inflate(R.menu.profile_image_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_view_image) {
                // Handle view image
                showImageDialog();
                return true;
            } else if (item.getItemId() == R.id.action_update_image) {
                // Handle update image (open picker)
                openImagePicker();
                return true;
            } else if (item.getItemId() == R.id.action_delete_image) {
                // Handle delete image
                deleteImage();
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }


    private void showImageDialog() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.image_dialog, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
        dialogBuilder.setView(dialogView);


        // Create and show the dialog
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        ImageView profileImage = dialogView.findViewById(R.id.profile_imageDialog);
        AppCompatButton updateProfileImage = dialogView.findViewById(R.id.update_profileImage);
        AppCompatButton closeProfileImage = dialogView.findViewById(R.id.close_profileImage);

        String imagePath = dbHelper.getUserProfileImagePath(loggedInUserId);
        if (imagePath != null) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                profileImage.setImageURI(Uri.fromFile(imgFile));
            }
        }

        // Close dialog and open image picker when 'Update' button is clicked
        updateProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();  // Close the dialog
                openImagePicker();  // Open image picker to update image
            }
        });

        // Close dialog when 'Close' button is clicked
        closeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();  // Close the dialog
            }
        });
    }

    // Delete image function (Clear image from view and reset flag)
    private void deleteImage() {
        dbHelper.deleteUserProfileImage(loggedInUserId);  // Delete image from DB
        binding.profileImage.setImageResource(R.drawable.profile); // Reset to default placeholder image
        selectedImageUri = null;
        isImageSet = false;
        Toast.makeText(requireContext(), "Profile image deleted", Toast.LENGTH_SHORT).show();
    }

    // Open image picker to select a profile image
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            binding.profileImage.setImageURI(selectedImageUri);  // Set the selected image
            isImageSet = true;
            saveProfileImageToMoneyBook(selectedImageUri);  // Save the image locally
        }
    }

    // Method to save the profile image in the MoneyBook folder and update DB
    public void saveProfileImageToMoneyBook(Uri imageUri) {
        File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File moneyBookFolder = new File(downloadsFolder, "MoneyBook");

        // Create the MoneyBook folder if it doesn't exist
        if (!moneyBookFolder.exists()) {
            boolean folderCreated = moneyBookFolder.mkdirs();
            if (!folderCreated) {
                Toast.makeText(requireContext(), "Failed to create MoneyBook folder.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Generate a unique file name using the current date and time
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "ProfileImage_" + timeStamp + ".jpg";

        File imageFile = new File(moneyBookFolder, fileName);

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
            saveBitmapToFile(bitmap, imageFile);

            // Update image path in DB
            boolean isSuccess = dbHelper.insertOrUpdateUserProfileImage(loggedInUserId, imageFile.getAbsolutePath());
            if (isSuccess) {
                Toast.makeText(requireContext(), "Profile image saved to " + imageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(requireContext(), "Failed to save profile image.", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Failed to save profile image.", Toast.LENGTH_SHORT).show();
        }
    }

    // Save Bitmap to file
    private void saveBitmapToFile(Bitmap bitmap, File file) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);  // Save the image
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    // Load the profile image from the database
    private void loadProfileImage(int userId) {
        String imagePath = dbHelper.getUserProfileImagePath(userId);
        if (imagePath != null) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                binding.profileImage.setImageURI(Uri.fromFile(imgFile));
                isImageSet = true;

//                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserData",MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("profileImagePath",imagePath);
//                editor.apply();
            }
        } else {
            binding.profileImage.setImageResource(R.drawable.profile);  // Default image
        }
    }

    // Set up the other button click listeners
    private void setupButtonClickListeners() {
        binding.viewUserProfileButton.setOnClickListener(v -> ((MainActivity) getActivity()).loadFragment(new ViewProfileFragment(), "View Profile"));
        binding.addCategoryButton.setOnClickListener(v -> ((MainActivity) getActivity()).loadFragment(new AddCategoryFragment(), "Add Category"));
        binding.addPaymentModeButton.setOnClickListener(v -> ((MainActivity) getActivity()).loadFragment(new AddPaymentModeFragment(), "Add Payment Mode"));
        binding.transactionAnalyticsButton.setOnClickListener(v -> ((MainActivity) getActivity()).loadFragment(new TransactionAnalyticsFragment(), "Transaction Analytics"));
        binding.investmentHistoryButton.setOnClickListener(v -> ((MainActivity) getActivity()).loadFragment(new InvestmentHistoryFragment(), "Investment History"));
        binding.allTransactionsButton.setOnClickListener(v -> ((MainActivity) getActivity()).loadFragment(new AllTransactionsFragment(), "All Transactions"));
        binding.calendarButton.setOnClickListener(v -> ((MainActivity) getActivity()).loadFragment(new CalendarFragment(), "Calendar"));
    }

}
