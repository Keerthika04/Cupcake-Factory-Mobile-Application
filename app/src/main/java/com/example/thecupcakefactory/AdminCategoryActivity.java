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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.adapter.CategoryAdapter;
import com.example.model.CategoryHelperClass;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class AdminCategoryActivity extends AppCompatActivity {

    EditText newCategoryNameTxt;
    public EditText updateCategoryNameTxt;
    ImageView newCategoryImg, close;
    public ImageView updateCategoryImg;
    FloatingActionButton adminAddNewCategoryBtn;
    MaterialButton saveCategory, updateCategory;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String[] ACCEPTED_FILE_TYPES = {"image/jpeg", "image/png"};
    Uri uri;
    boolean update;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_categories);

        recyclerView = findViewById(R.id.cupcakeRecyclerView);
        progressBar = findViewById(R.id.progressBar);

        adminAddNewCategoryBtn = findViewById(R.id.adminAddNewCategoryBtn);
        adminAddNewCategoryBtn.setOnClickListener(view -> addNewCategory());
        showCategory();
    }

    private void addNewCategory() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_new_category);

        progressBar = dialog.findViewById(R.id.progressBar);
        close = dialog.findViewById(R.id.close);
        newCategoryImg = dialog.findViewById(R.id.newCategoryImg);
        newCategoryNameTxt = dialog.findViewById(R.id.newCategoryNameTxt);
        saveCategory = dialog.findViewById(R.id.saveCategoryBtn);

        newCategoryImg.setOnClickListener(view -> {
            update=false;
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        saveCategory.setOnClickListener(view -> {
            update=false;
            String categoryName = newCategoryNameTxt.getText().toString().trim();
            if (uri == null) {
                new AlertDialog.Builder(AdminCategoryActivity.this)
                        .setMessage("Insert an image!")
                        .setPositiveButton("Close", null)
                        .show();
            } else if (categoryName.isEmpty()) {
                new AlertDialog.Builder(AdminCategoryActivity.this)
                        .setMessage("Enter the category name!")
                        .setPositiveButton("Close", null)
                        .show();
            } else {
                uploadImage(categoryName, uri, false);
            }
        });

        close.setOnClickListener(view -> dialog.dismiss());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
    }
    public void onUpdateCategory() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.update_category);


        progressBar = dialog.findViewById(R.id.progressBar);
        close = dialog.findViewById(R.id.close);
        updateCategoryImg = dialog.findViewById(R.id.updateCategoryImg);
        updateCategoryNameTxt = dialog.findViewById(R.id.updateCategoryNameTxt);
        updateCategory = dialog.findViewById(R.id.updateCategoryBtn);


        updateCategoryImg.setOnClickListener(view -> {
            update = true;
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        updateCategory.setOnClickListener(view -> {
            update = true;
            String categoryName = updateCategoryNameTxt.getText().toString().trim();
            if (uri == null) {
                uploadImage(categoryName, null, false);
            } else {
                uploadImage(categoryName, uri, true);
            }
        });

        close.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

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
                            .into(updateCategoryImg);
                } else {
                    new AlertDialog.Builder(AdminCategoryActivity.this)
                            .setMessage("Please select a JPG or PNG image")
                            .setPositiveButton("Close", null)
                            .show();
                    uri = null;
                }
            }
        } else {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                uri = data.getData();
                if (isAcceptedFileType(uri)) {
                    Glide.with(this)
                            .load(uri)
                            .apply(new RequestOptions().placeholder(R.drawable.upload_img))
                            .into(newCategoryImg);
                } else {
                    new AlertDialog.Builder(AdminCategoryActivity.this)
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
    private void uploadImage(String categoryName, Uri imageUri, boolean updateImage) {
        progressBar.setVisibility(View.VISIBLE);
            try {
                if(update) {
                    if (updateImage) {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        int quality = calculateQuality(bitmap);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

                        StorageReference newImageRef = FirebaseStorage.getInstance().getReference().child("categories").child(categoryName);
                        newImageRef.putFile(imageUri)
                                .addOnSuccessListener(taskSnapshot -> {
                                    newImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                        String newImageUrl = uri.toString();
                                        saveCategory(categoryName, newImageUrl, true);
                                        progressBar.setVisibility(View.GONE);
                                    });
                                })
                                .addOnFailureListener(e -> {
                                    progressBar.setVisibility(View.GONE);
                                    new AlertDialog.Builder(AdminCategoryActivity.this)
                                            .setMessage("Failed to upload image")
                                            .setPositiveButton("Close", null)
                                            .show();
                                });
                    } else {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("categoryName", categoryName);

                        String key = categoryAdapter.getRef(position).getKey();
                        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("categories");
                        categoryRef.child(key).updateChildren(map)
                                .addOnSuccessListener(aVoid -> {
                                    new AlertDialog.Builder(AdminCategoryActivity.this)
                                            .setMessage("Successfully updated category name!")
                                            .setPositiveButton("Close", null)
                                            .show();
                                    progressBar.setVisibility(View.GONE);
                                })
                                .addOnFailureListener(e -> {
                                    progressBar.setVisibility(View.GONE);
                                    new AlertDialog.Builder(AdminCategoryActivity.this)
                                            .setMessage("Failed to update category name!")
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

                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("categories/" + categoryName);
                    storageRef.putFile(imageUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();
                                saveCategory(categoryName, imageUrl,true);
                                progressBar.setVisibility(View.GONE);
                            });
                        })
                        .addOnFailureListener(e -> {
                            progressBar.setVisibility(View.GONE);
                            new AlertDialog.Builder(AdminCategoryActivity.this)
                                    .setMessage("Failed to upload image!")
                                    .setPositiveButton("Close", null)
                                    .show();
                        });
                }
            } catch (IOException e) {
                e.printStackTrace();
                progressBar.setVisibility(View.GONE);
                new AlertDialog.Builder(AdminCategoryActivity.this)
                        .setMessage("Failed to upload image!")
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
    private void saveCategory(String categoryName, String imageUrl, boolean updateImage) {
        if(update) {
            if (updateImage) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("categoryImage", imageUrl);
                map.put("categoryName", categoryName);

                String key = categoryAdapter.getRef(position).getKey();
                DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("categories");
                CategoryHelperClass category = new CategoryHelperClass(categoryName, imageUrl);
                categoryRef.child(key).updateChildren(map)
                        .addOnSuccessListener(aVoid -> {
                            new AlertDialog.Builder(AdminCategoryActivity.this)
                                    .setMessage("Successfully updated category!")
                                    .setPositiveButton("Close", null)
                                    .show();
                            progressBar.setVisibility(View.GONE);
                        })
                        .addOnFailureListener(e -> {
                            new AlertDialog.Builder(AdminCategoryActivity.this)
                                    .setMessage("Failed to update category!")
                                    .setPositiveButton("Close", null)
                                    .show();
                            progressBar.setVisibility(View.GONE);
                        });
            } else {
                DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("categories");
                String categoryId = categoryRef.push().getKey();

                CategoryHelperClass category = new CategoryHelperClass(categoryName, imageUrl);
                if (categoryId != null) {
                    categoryRef.child(categoryId).setValue(category)
                            .addOnSuccessListener(aVoid -> {
                                new AlertDialog.Builder(AdminCategoryActivity.this)
                                        .setMessage("Category saved successfully!")
                                        .setPositiveButton("Close", null)
                                        .show();
                                progressBar.setVisibility(View.GONE);
                            })
                            .addOnFailureListener(e -> {
                                new AlertDialog.Builder(AdminCategoryActivity.this)
                                        .setMessage("Failed to save category!")
                                        .setPositiveButton("Close", null)
                                        .show();
                                progressBar.setVisibility(View.GONE);
                            });
                }
            }
        }else {
                    DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("categories");
                    String categoryId = categoryRef.push().getKey();

                    CategoryHelperClass category = new CategoryHelperClass(categoryName, imageUrl);
                    if (categoryId != null) {
                        categoryRef.child(categoryId).setValue(category)
                                .addOnSuccessListener(aVoid -> {
                                    new AlertDialog.Builder(AdminCategoryActivity.this)
                                            .setMessage("Successfully saved category!")
                                            .setPositiveButton("Close", null)
                                            .show();
                                    newCategoryNameTxt.setText("");
                                    Glide.with(this).load(R.drawable.upload_img).into(newCategoryImg);
                                    uri = null;
                                })
                                .addOnFailureListener(e -> {
                                    new AlertDialog.Builder(AdminCategoryActivity.this)
                                            .setMessage("Failed to save category!")
                                            .setPositiveButton("Close", null)
                                            .show();
                                });
                    }
                }

        }
    private void showCategory() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<CategoryHelperClass> options = new FirebaseRecyclerOptions.Builder<CategoryHelperClass>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("categories"),CategoryHelperClass.class)
                .build();

        categoryAdapter = new CategoryAdapter(options);
        recyclerView.setAdapter(categoryAdapter);

        categoryAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        categoryAdapter.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        categoryAdapter.stopListening();
    }
}
