package com.example.thecupcakefactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.adapter.CupcakeAdapter;
import com.example.model.CupcakeHelperClass;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class AdminCupcakeActivity extends AppCompatActivity {

    TextInputEditText newCupcakeNameTxt, newCupcakePriceTxt, newCupcakeIngredientsTxt;
    public TextInputEditText updateNewCupcakeNameTxt, updateNewCupcakePriceTxt, updateNewCupcakeIngredientsTxt;
    ImageView newCupcakeImg;
    public ImageView updateNewCupcakeImg;
    SearchView cupcakeSearchTxt;
    ImageView close;
    FloatingActionButton adminAddNewCupcakeBtn;
    MaterialButton saveCupcake, updateCupcake;
    AutoCompleteTextView autoCompleteCategoryTxt;
    public AutoCompleteTextView updateAutoCompleteCategoryTxt;
    ProgressBar progressBar;
    RecyclerView cupcakeRecyclerView;
    DatabaseReference databaseReference;
    ArrayAdapter<String> adapterItems;

    CupcakeAdapter cupcakeAdapter;
    List<CupcakeHelperClass> cupcakeList;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String[] ACCEPTED_FILE_TYPES = {"image/jpeg", "image/png"};
    Uri uri;
    boolean update;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_cupcake);

        cupcakeSearchTxt = findViewById(R.id.cupcakeSearchTxt);
        cupcakeRecyclerView = findViewById(R.id.cupcakeRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        cupcakeSearchTxt.clearFocus();

        adminAddNewCupcakeBtn = findViewById(R.id.adminAddNewCupcakeBtn);
        adminAddNewCupcakeBtn.setOnClickListener(view -> addNewCupcake());
        showCupcake();
//        search();
    }

    private void addNewCupcake() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_new_cupcake);

        progressBar = dialog.findViewById(R.id.progressBar);
        close = dialog.findViewById(R.id.close);

        autoCompleteCategoryTxt = dialog.findViewById(R.id.autoCompleteCategoryTxt);
        newCupcakeImg = dialog.findViewById(R.id.newCupcakeImg);
        newCupcakeNameTxt = dialog.findViewById(R.id.newCupcakeNameTxt);
        newCupcakePriceTxt = dialog.findViewById(R.id.newCupcakePriceTxt);
        newCupcakeIngredientsTxt = dialog.findViewById(R.id.newCupcakeIngredientsTxt);
        saveCupcake = dialog.findViewById(R.id.saveCupcakeBtn);

        dropDownLists(false);

        autoCompleteCategoryTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                progressBar.setVisibility(View.GONE);
                String item = adapterView.getItemAtPosition(i).toString();
            }
        });

        newCupcakeImg.setOnClickListener(view -> {
            update=false;
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        saveCupcake.setOnClickListener(view -> {
            update=false;
            String cupcakeName = newCupcakeNameTxt.getText().toString().trim();
            String listedCategory = autoCompleteCategoryTxt.getText().toString().trim();
            String cupcakePrice = newCupcakePriceTxt.getText().toString().trim();
            String cupcakeIngredients = newCupcakeIngredientsTxt.getText().toString().trim();

            if (uri == null) {
                new AlertDialog.Builder(AdminCupcakeActivity.this)
                        .setMessage("Insert an image!")
                        .setPositiveButton("Close", null)
                        .show();
                Toast.makeText(AdminCupcakeActivity.this, "", Toast.LENGTH_SHORT).show();
            } else if (cupcakeName.isEmpty()) {
                new AlertDialog.Builder(AdminCupcakeActivity.this)
                        .setMessage("Enter the cupcake's name!")
                        .setPositiveButton("Close", null)
                        .show();
            } else if (listedCategory.isEmpty()) {
                new AlertDialog.Builder(AdminCupcakeActivity.this)
                        .setMessage("Select the cupcake's category!")
                        .setPositiveButton("Close", null)
                        .show();
            } else if (cupcakePrice.isEmpty()) {
                new AlertDialog.Builder(AdminCupcakeActivity.this)
                        .setMessage("Enter cupcake's price!")
                        .setPositiveButton("Close", null)
                        .show();
            } else if (cupcakeIngredients.isEmpty()) {
                new AlertDialog.Builder(AdminCupcakeActivity.this)
                        .setMessage("Enter cupcake's Ingredients!")
                        .setPositiveButton("Close", null)
                        .show();
            }else {
                uploadImage(cupcakeName, listedCategory, cupcakePrice, cupcakeIngredients, uri, false);
            }
        });

        close.setOnClickListener(view -> dialog.dismiss());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
    }

    public void onUpdateCupcake() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.update_cupcake);


        progressBar = dialog.findViewById(R.id.progressBar);
        close = dialog.findViewById(R.id.close);
        updateNewCupcakeImg = dialog.findViewById(R.id.updateNewCupcakeImg);
        updateNewCupcakeNameTxt = dialog.findViewById(R.id.updateNewCupcakeNameTxt);
        updateAutoCompleteCategoryTxt = dialog.findViewById(R.id.autoCompleteCategoryTxt);
        updateNewCupcakePriceTxt = dialog.findViewById(R.id.updateNewCupcakePriceTxt);
        updateNewCupcakeIngredientsTxt = dialog.findViewById(R.id.updateNewCupcakeIngredientsTxt);
        updateCupcake = dialog.findViewById(R.id.updateCupcake);

        dropDownLists(true);
        updateAutoCompleteCategoryTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                progressBar.setVisibility(View.GONE);
                String item = adapterView.getItemAtPosition(i).toString();
            }
        });

        updateNewCupcakeImg.setOnClickListener(view -> {
            update = true;
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        updateCupcake.setOnClickListener(view -> {
            update = true;
            String cupcakeName = updateNewCupcakeNameTxt.getText().toString().trim();
            String cupcakeCategory = updateAutoCompleteCategoryTxt.getText().toString().trim();
            String cupcakePrice = updateNewCupcakePriceTxt.getText().toString().trim();
            String cupcakeIngredients = updateNewCupcakeIngredientsTxt.getText().toString().trim();
            if (uri == null) {
                uploadImage(cupcakeName, cupcakeCategory, cupcakePrice, cupcakeIngredients, uri, false);
            } else {
                uploadImage(cupcakeName, cupcakeCategory, cupcakePrice, cupcakeIngredients, uri, true);
            }
        });

        close.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

    }

    public void dropDownLists(Boolean updateCategory){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("categories");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> categoryNames = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String categoryName = snapshot.child("categoryName").getValue(String.class);
                    if (categoryName != null) {
                        categoryNames.add(categoryName);
                    }
                }

                String[] categoryArray = categoryNames.toArray(new String[0]);

                adapterItems = new ArrayAdapter<>(AdminCupcakeActivity.this, R.layout.list_item, categoryArray);

                if(updateCategory){
                    updateAutoCompleteCategoryTxt.setAdapter(adapterItems);
                }else {
                    autoCompleteCategoryTxt.setAdapter(adapterItems);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error retrieving data", databaseError.toException());
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (update) {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                uri = data.getData();
                if (isAcceptedFileType(uri)) {
                    Glide.with(this)
                            .load(uri)
                            .apply(new RequestOptions().placeholder(R.drawable.upload_img))
                            .into(updateNewCupcakeImg);
                } else {
                    new AlertDialog.Builder(AdminCupcakeActivity.this)
                            .setMessage("Please select a JPG or PNG image")
                            .setPositiveButton("Close", null)
                            .show();
                    uri = null;
                }
            }
        }else {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                uri = data.getData();
                if (isAcceptedFileType(uri)) {
                    Glide.with(this)
                            .load(uri)
                            .apply(new RequestOptions().placeholder(R.drawable.upload_img))
                            .into(newCupcakeImg);
                } else {
                    new AlertDialog.Builder(AdminCupcakeActivity.this)
                            .setMessage("Please select a JPG or PNG image")
                            .setPositiveButton("Close", null)
                            .show();
                    uri = null;
                }
            }
        }
    }

    private boolean isAcceptedFileType(Uri uri) {
        String contentType = getContentResolver().getType(uri);
        if (contentType != null) {
            for (String type : ACCEPTED_FILE_TYPES) {
                if (contentType.equals(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void getPosition(int position){
        this.position = position;
    }
    private void uploadImage(String cupcakeName, String category, String cupcakePrice, String cupcakeIngredients,  Uri imageUri, boolean updateImage) {
        progressBar.setVisibility(View.VISIBLE);
            try {
                if(update) {
                     if (updateImage) {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        int quality = calculateQuality(bitmap);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

                        StorageReference newImageRef = FirebaseStorage.getInstance().getReference().child("cupcakes").child(cupcakeName);
                        newImageRef.putFile(imageUri)
                                .addOnSuccessListener(taskSnapshot -> {
                                    newImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                        String newImageUrl = uri.toString();
                                        saveCupcake(cupcakeName, category, cupcakePrice, cupcakeIngredients, newImageUrl, true);
                                        progressBar.setVisibility(View.GONE);
                                    });
                                })
                                .addOnFailureListener(e -> {
                                    progressBar.setVisibility(View.GONE);
                                    new AlertDialog.Builder(AdminCupcakeActivity.this)
                                            .setMessage("Failed to upload image")
                                            .setPositiveButton("Close", null)
                                            .show();
                                });
                    } else {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("cupcakeName", cupcakeName);
                        map.put("cupcakeCategory", category);
                        map.put("cupcakePrice", cupcakePrice);
                        map.put("cupcakeIngredients", cupcakeIngredients);

                        String key = cupcakeAdapter.getRef(position).getKey();
                        DatabaseReference cupcakeRef = FirebaseDatabase.getInstance().getReference().child("cupcakes");
                        cupcakeRef.child(key).updateChildren(map)
                                .addOnSuccessListener(aVoid -> {
                                    new AlertDialog.Builder(AdminCupcakeActivity.this)
                                            .setMessage("Successfully updated the cupcake!")
                                            .setPositiveButton("Close", null)
                                            .show();
                                    progressBar.setVisibility(View.GONE);
                                })
                                .addOnFailureListener(e -> {
                                    progressBar.setVisibility(View.GONE);
                                    new AlertDialog.Builder(AdminCupcakeActivity.this)
                                            .setMessage("Failed to update the cupcake!")
                                            .setPositiveButton("Close", null)
                                            .show();
                                });
                    }
                }
                else{
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    int quality = calculateQuality(bitmap);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                    byte[] data = outputStream.toByteArray();

                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("cupcakes/" + cupcakeName);
                    storageRef.putFile(imageUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();
                                saveCupcake(cupcakeName, category, cupcakePrice, cupcakeIngredients, imageUrl, true);
                                progressBar.setVisibility(View.GONE);
                            });
                        })
                        .addOnFailureListener(e -> {
                            progressBar.setVisibility(View.GONE);
                            new AlertDialog.Builder(AdminCupcakeActivity.this)
                                    .setMessage("Failed to upload image")
                                    .setPositiveButton("Close", null)
                                    .show();
                        });
                }
            } catch (IOException e) {
                e.printStackTrace();
                progressBar.setVisibility(View.GONE);
                new AlertDialog.Builder(AdminCupcakeActivity.this)
                        .setMessage("Failed to upload image")
                        .setPositiveButton("Close", null)
                        .show();
            }
    }

    private int calculateQuality(Bitmap bitmap) {
        int maxSize = 1024 * 1024;
        int currentSize;
        int quality = 100;
        do {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            currentSize = outputStream.size();
            quality -= 5;
        } while (currentSize > maxSize && quality > 0);
        return quality + 5;
    }

    private void saveCupcake(String cupcakeName, String category, String cupcakePrice, String cupcakeIngredients, String imageUrl, boolean updateImage) {
        if(update) {
            if (updateImage) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("cupcakeImage", imageUrl);
                map.put("cupcakeName", cupcakeName);
                map.put("cupcakeCategory", category);
                map.put("cupcakePrice", cupcakePrice);
                map.put("cupcakeIngredients", cupcakeIngredients);

                String key = cupcakeAdapter.getRef(position).getKey();
                DatabaseReference cupcakeRef = FirebaseDatabase.getInstance().getReference().child("cupcakes");
                CupcakeHelperClass cupcake = new CupcakeHelperClass(imageUrl, cupcakeName, category, cupcakePrice, cupcakeIngredients);
                cupcakeRef.child(key).updateChildren(map)
                        .addOnSuccessListener(aVoid -> {
                            new AlertDialog.Builder(AdminCupcakeActivity.this)
                                    .setMessage("Successfully updated the cupcake!")
                                    .setPositiveButton("Close", null)
                                    .show();
                            progressBar.setVisibility(View.GONE);
                        })
                        .addOnFailureListener(e -> {
                            new AlertDialog.Builder(AdminCupcakeActivity.this)
                                    .setMessage("Failed to update cupcake!")
                                    .setPositiveButton("Close", null)
                                    .show();
                            progressBar.setVisibility(View.GONE);
                        });
            } else {
                DatabaseReference cupcakeRef = FirebaseDatabase.getInstance().getReference().child("cupcakes");
                String cupcakeId = cupcakeRef.push().getKey();

                CupcakeHelperClass cupcake = new CupcakeHelperClass(imageUrl, cupcakeName, category, cupcakePrice, cupcakeIngredients);
                if (cupcakeId != null) {
                    cupcakeRef.child(cupcakeId).setValue(cupcake)
                            .addOnSuccessListener(aVoid -> {
                                new AlertDialog.Builder(AdminCupcakeActivity.this)
                                        .setMessage("Successfully saved Cupcake!")
                                        .setPositiveButton("Close", null)
                                        .show();
                                progressBar.setVisibility(View.GONE);
                            })
                            .addOnFailureListener(e -> {
                                new AlertDialog.Builder(AdminCupcakeActivity.this)
                                        .setMessage("Failed to save Cupcake")
                                        .setPositiveButton("Close", null)
                                        .show();
                                progressBar.setVisibility(View.GONE);
                            });
                }
            }
        }else {
            DatabaseReference cupcakeRef = FirebaseDatabase.getInstance().getReference().child("cupcakes");
            String cupcakeId = cupcakeRef.push().getKey();

            CupcakeHelperClass cupcake = new CupcakeHelperClass(imageUrl, cupcakeName, category, cupcakePrice, cupcakeIngredients);
            if (cupcakeId != null) {
                cupcakeRef.child(cupcakeId).setValue(cupcake)
                        .addOnSuccessListener(aVoid -> {
                            new AlertDialog.Builder(AdminCupcakeActivity.this)
                                    .setMessage("Cupcake saved successfully")
                                    .setPositiveButton("Close", null)
                                    .show();
                            newCupcakeNameTxt.setText("");
                            newCupcakeNameTxt.setText("");
                            newCupcakePriceTxt.setText("");
                            newCupcakeIngredientsTxt.setText("");
                            Glide.with(this).load(R.drawable.upload_img).into(newCupcakeImg);
                            autoCompleteCategoryTxt.setText("");
                            uri = null;
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(AdminCupcakeActivity.this, "Failed to save cupcake", Toast.LENGTH_SHORT).show();
                        });
            }
        }

    }

    private void showCupcake() {
        cupcakeRecyclerView = findViewById(R.id.cupcakeRecyclerView);
        cupcakeRecyclerView.setHasFixedSize(true);
        cupcakeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<CupcakeHelperClass> options = new FirebaseRecyclerOptions.Builder<CupcakeHelperClass>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("cupcakes").orderByChild("cupcakeCategory"), CupcakeHelperClass.class)
                .build();

        cupcakeAdapter = new CupcakeAdapter(options);
        cupcakeRecyclerView.setAdapter(cupcakeAdapter);

        cupcakeAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void search() {
        cupcakeSearchTxt.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchCupcake(newText);
                return true;
            }
        });
    }

    public void searchCupcake(String query) {
        ArrayList<CupcakeHelperClass> filteredCupcakeList = new ArrayList<>();
            for (CupcakeHelperClass cupcake : cupcakeList) {
                if (cupcake.getCupcakeName().toLowerCase().contains(query.toLowerCase()) ||
                        cupcake.getCupcakeCategory().toLowerCase().contains(query.toLowerCase()) || cupcake.getCupcakeIngredients().toLowerCase().contains(query.toLowerCase())
                        || cupcake.getCupcakePrice().toLowerCase().contains(query.toLowerCase())) {
                    filteredCupcakeList.add(cupcake);
                }
            }
            cupcakeAdapter.searchCupcake(filteredCupcakeList);
    }

    protected void onStart() {
        super.onStart();
        cupcakeAdapter.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cupcakeAdapter.stopListening();
    }
}
