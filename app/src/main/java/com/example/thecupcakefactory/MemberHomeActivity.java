package com.example.thecupcakefactory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.adapter.MemberCategoryAdapter;
import com.example.adapter.MemberCupcakeAdapter;
import com.example.model.CategoryHelperClass;
import com.example.model.CupcakeHelperClass;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class MemberHomeActivity extends AppCompatActivity {

    private RecyclerView classicCupcakeView, anniversaryCupcakeView, birthdayCupcakeView, babyShowerCupcakeView, valentineCupcakeView, mothersDayCupcakeView, fathersDayCupcakeView, graduationDayCupcakeView, christmasCupcakeView, categorySelection;
    private MemberCategoryAdapter memberCategoryAdapter;
    private MemberCupcakeAdapter classicCupcakeAdapter, anniversaryCupcakeAdapter, birthdayCupcakeAdapter, babyShowerCupcakeAdapter, valentineCupcakeAdapter, mothersDayCupcakeAdapter, fathersDayCupcakeAdapter, graduationDayCupcakeAdapter, christmasCupcakeAdapter;
    private ProgressBar classicCupcakeProgressBar, anniversaryCupcakeProgressBar, birthdayCupcakeProgressBar, babyShowerCupcakeProgressBar, valentineCupcakeProgressBar, mothersDayCupcakeProgressBar, fathersDayCupcakeProgressBar, graduationDayCupcakeProgressBar, christmasCupcakeProgressBar, categorySelectionProgressBar;
    String data;
    public static String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_home);

        Bundle extras = getIntent().getExtras();
        user = extras.getString("username");

        imageSlider();
        showCategories();
        showCasualCupcake();
        showAnniversaryCupcake();
        showBirthdayCupcake();
        showBabyShowerCupcake();
        showValentineCupcake();
        showMothersDayCupcake();
        showFathersDayCupcake();
        showGraduationDayCupcake();
        showChristmasCupcake();
    }

    private void imageSlider(){
        ImageSlider imageSlider = findViewById(R.id.imageSlider);
        ArrayList<SlideModel> sliderModel = new ArrayList<>();
        sliderModel.add(new SlideModel(R.drawable.promotion_card2, ScaleTypes.FIT));
        sliderModel.add(new SlideModel(R.drawable.promotion_card3, ScaleTypes.FIT));
        sliderModel.add(new SlideModel(R.drawable.promotion_card1, ScaleTypes.FIT));

        imageSlider.setImageList(sliderModel,ScaleTypes.FIT);
    }
    private void showCategories() {
        categorySelection = findViewById(R.id.categorySelectionView);
        categorySelection.setNestedScrollingEnabled(false);
        categorySelection.setLayoutManager(new GridLayoutManager(this,5));
        categorySelectionProgressBar = findViewById(R.id.categorySelectionProgressBar);

        FirebaseRecyclerOptions<CategoryHelperClass> options = new FirebaseRecyclerOptions.Builder<CategoryHelperClass>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("categories"), CategoryHelperClass.class)
                .build();

        memberCategoryAdapter = new MemberCategoryAdapter(options);
        categorySelection.setAdapter(memberCategoryAdapter);

        memberCategoryAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                categorySelectionProgressBar.setVisibility(View.GONE);
            }
        });

    }
    private void showCasualCupcake() {
        classicCupcakeView = findViewById(R.id.classicCupcakeView);
        classicCupcakeView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        classicCupcakeProgressBar = findViewById(R.id.classicCupcakeProgressBar);

        Query query = FirebaseDatabase.getInstance().getReference().child("cupcakes")
                .orderByChild("cupcakeCategory")
                .equalTo("Classic");

        FirebaseRecyclerOptions<CupcakeHelperClass> options = new FirebaseRecyclerOptions.Builder<CupcakeHelperClass>()
                .setQuery(query, CupcakeHelperClass.class)
                .build();

        classicCupcakeAdapter = new MemberCupcakeAdapter(options);
        classicCupcakeView.setAdapter(classicCupcakeAdapter);

        classicCupcakeAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                classicCupcakeProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showAnniversaryCupcake() {
        anniversaryCupcakeView = findViewById(R.id.anniversaryCupcakeView);
        anniversaryCupcakeView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        anniversaryCupcakeProgressBar = findViewById(R.id.anniversaryCupcakeProgressBar);

        Query query = FirebaseDatabase.getInstance().getReference().child("cupcakes")
                .orderByChild("cupcakeCategory")
                .equalTo("Anniversary");

        FirebaseRecyclerOptions<CupcakeHelperClass> options = new FirebaseRecyclerOptions.Builder<CupcakeHelperClass>()
                .setQuery(query, CupcakeHelperClass.class)
                .build();

        anniversaryCupcakeAdapter = new MemberCupcakeAdapter(options);
        anniversaryCupcakeView.setAdapter(anniversaryCupcakeAdapter);

        anniversaryCupcakeAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                anniversaryCupcakeProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showBirthdayCupcake() {
        birthdayCupcakeView = findViewById(R.id.birthdayCupcakeView);
        birthdayCupcakeView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        birthdayCupcakeProgressBar = findViewById(R.id.birthdayCupcakeProgressBar);

        Query query = FirebaseDatabase.getInstance().getReference().child("cupcakes")
                .orderByChild("cupcakeCategory")
                .equalTo("Birthday");

        FirebaseRecyclerOptions<CupcakeHelperClass> options = new FirebaseRecyclerOptions.Builder<CupcakeHelperClass>()
                .setQuery(query, CupcakeHelperClass.class)
                .build();

        birthdayCupcakeAdapter = new MemberCupcakeAdapter(options);
        birthdayCupcakeView.setAdapter(birthdayCupcakeAdapter);

        birthdayCupcakeAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                birthdayCupcakeProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showBabyShowerCupcake() {
        babyShowerCupcakeView = findViewById(R.id.babyShowerCupcakeView);
        babyShowerCupcakeView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        babyShowerCupcakeProgressBar = findViewById(R.id.babyShowerCupcakeProgressBar);

        Query query = FirebaseDatabase.getInstance().getReference().child("cupcakes")
                .orderByChild("cupcakeCategory")
                .equalTo("Baby Shower");

        FirebaseRecyclerOptions<CupcakeHelperClass> options = new FirebaseRecyclerOptions.Builder<CupcakeHelperClass>()
                .setQuery(query, CupcakeHelperClass.class)
                .build();

        babyShowerCupcakeAdapter = new MemberCupcakeAdapter(options);
        babyShowerCupcakeView.setAdapter(babyShowerCupcakeAdapter);

        babyShowerCupcakeAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                babyShowerCupcakeProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showValentineCupcake() {
        valentineCupcakeView = findViewById(R.id.valentineCupcakeView);
        valentineCupcakeView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        valentineCupcakeProgressBar = findViewById(R.id.valentineCupcakeProgressBar);

        Query query = FirebaseDatabase.getInstance().getReference().child("cupcakes")
                .orderByChild("cupcakeCategory")
                .equalTo("Valentine");

        FirebaseRecyclerOptions<CupcakeHelperClass> options = new FirebaseRecyclerOptions.Builder<CupcakeHelperClass>()
                .setQuery(query, CupcakeHelperClass.class)
                .build();

        valentineCupcakeAdapter = new MemberCupcakeAdapter(options);
        valentineCupcakeView.setAdapter(valentineCupcakeAdapter);

        valentineCupcakeAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                valentineCupcakeProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showMothersDayCupcake() {
        mothersDayCupcakeView = findViewById(R.id.mothersDayCupcakeView);
        mothersDayCupcakeView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mothersDayCupcakeProgressBar = findViewById(R.id.mothersDayCupcakeProgressBar);

        Query query = FirebaseDatabase.getInstance().getReference().child("cupcakes")
                .orderByChild("cupcakeCategory")
                .equalTo("Mother's Day");

        FirebaseRecyclerOptions<CupcakeHelperClass> options = new FirebaseRecyclerOptions.Builder<CupcakeHelperClass>()
                .setQuery(query, CupcakeHelperClass.class)
                .build();

        mothersDayCupcakeAdapter = new MemberCupcakeAdapter(options);
        mothersDayCupcakeView.setAdapter(mothersDayCupcakeAdapter);

        mothersDayCupcakeAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                mothersDayCupcakeProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showFathersDayCupcake() {
        fathersDayCupcakeView = findViewById(R.id.fathersDayCupcakeView);
        fathersDayCupcakeView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        fathersDayCupcakeProgressBar = findViewById(R.id.fathersDayCupcakeProgressBar);

        Query query = FirebaseDatabase.getInstance().getReference().child("cupcakes")
                .orderByChild("cupcakeCategory")
                .equalTo("Father's Day");

        FirebaseRecyclerOptions<CupcakeHelperClass> options = new FirebaseRecyclerOptions.Builder<CupcakeHelperClass>()
                .setQuery(query, CupcakeHelperClass.class)
                .build();

        fathersDayCupcakeAdapter = new MemberCupcakeAdapter(options);
        fathersDayCupcakeView.setAdapter(fathersDayCupcakeAdapter);

        fathersDayCupcakeAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                fathersDayCupcakeProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showGraduationDayCupcake() {
        graduationDayCupcakeView = findViewById(R.id.graduationDayCupcakeView);
        graduationDayCupcakeView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        graduationDayCupcakeProgressBar = findViewById(R.id.graduationDayCupcakeProgressBar);

        Query query = FirebaseDatabase.getInstance().getReference().child("cupcakes")
                .orderByChild("cupcakeCategory")
                .equalTo("Graduation");

        FirebaseRecyclerOptions<CupcakeHelperClass> options = new FirebaseRecyclerOptions.Builder<CupcakeHelperClass>()
                .setQuery(query, CupcakeHelperClass.class)
                .build();

        graduationDayCupcakeAdapter = new MemberCupcakeAdapter(options);
        graduationDayCupcakeView.setAdapter(graduationDayCupcakeAdapter);

        graduationDayCupcakeAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                graduationDayCupcakeProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showChristmasCupcake() {
        christmasCupcakeView = findViewById(R.id.christmasCupcakeView);
        christmasCupcakeView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        christmasCupcakeProgressBar = findViewById(R.id.christmasCupcakeProgressBar);

        Query query = FirebaseDatabase.getInstance().getReference().child("cupcakes")
                .orderByChild("cupcakeCategory")
                .equalTo("Christmas");

        FirebaseRecyclerOptions<CupcakeHelperClass> options = new FirebaseRecyclerOptions.Builder<CupcakeHelperClass>()
                .setQuery(query, CupcakeHelperClass.class)
                .build();

        christmasCupcakeAdapter = new MemberCupcakeAdapter(options);
        christmasCupcakeView.setAdapter(christmasCupcakeAdapter);

        christmasCupcakeAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                christmasCupcakeProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onStart() {
        memberCategoryAdapter.startListening();
        classicCupcakeAdapter.startListening();
        birthdayCupcakeAdapter.startListening();
        anniversaryCupcakeAdapter.startListening();
        babyShowerCupcakeAdapter.startListening();
        valentineCupcakeAdapter.startListening();
        mothersDayCupcakeAdapter.startListening();
        fathersDayCupcakeAdapter.startListening();
        graduationDayCupcakeAdapter.startListening();
        christmasCupcakeAdapter.startListening();
        super.onStart();
    }

    public void toCart(View view){
        Intent i = new Intent(MemberHomeActivity.this,CartActivity.class);
        i.putExtra("username", user);
        startActivity(i);
    }

    public void toOrders(View view){
        Intent i = new Intent(MemberHomeActivity.this,MemberOrderActivity.class);
        i.putExtra("username", user);
        startActivity(i);
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MemberHomeActivity.super.onBackPressed();
                        memberCategoryAdapter.stopListening();
                        classicCupcakeAdapter.stopListening();
                        birthdayCupcakeAdapter.stopListening();
                        anniversaryCupcakeAdapter.stopListening();
                        babyShowerCupcakeAdapter.stopListening();
                        valentineCupcakeAdapter.stopListening();
                        mothersDayCupcakeAdapter.stopListening();
                        fathersDayCupcakeAdapter.stopListening();
                        graduationDayCupcakeAdapter.stopListening();
                        christmasCupcakeAdapter.stopListening();
                        startActivity(new Intent(MemberHomeActivity.this, MainActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
