package com.example.thecupcakefactory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.adapter.MemberCategoryAdapter;
import com.example.adapter.MemberCupcakeAdapter;
import com.example.model.CategoryHelperClass;
import com.example.model.CupcakeHelperClass;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class CategoryDetailActivity extends AppCompatActivity {

    RecyclerView cupcakeBasedOnCategory;
    TextView categoryNameTxt;
    String categoryName, user;
    ProgressBar categorySelectionProgressBar;
    MemberCupcakeAdapter cupcakeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_cupcake_card);

        categoryNameTxt = findViewById(R.id.categoryNameTxt);

        Bundle extras = getIntent().getExtras();
        user = extras.getString("username");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            categoryNameTxt.setText(bundle.getString("Name"));
            categoryName = bundle.getString("Name");
        }
        showCategories(categoryName);

    }

    private void showCategories(String categoryName) {
        cupcakeBasedOnCategory = findViewById(R.id.cupcakeBasedOnCategory);
        cupcakeBasedOnCategory.setLayoutManager(new GridLayoutManager(this,2));
        categorySelectionProgressBar = findViewById(R.id.categorySelectionProgressBar);


        Query query = FirebaseDatabase.getInstance().getReference().child("cupcakes")
                .orderByChild("cupcakeCategory")
                .equalTo(categoryName);

        FirebaseRecyclerOptions<CupcakeHelperClass> options = new FirebaseRecyclerOptions.Builder<CupcakeHelperClass>()
                .setQuery(query, CupcakeHelperClass.class)
                .build();

        cupcakeAdapter = new MemberCupcakeAdapter(options);
        cupcakeBasedOnCategory.setAdapter(cupcakeAdapter);

        cupcakeAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                categorySelectionProgressBar.setVisibility(View.GONE);
            }
        });
    }

    protected void onStart() {
        cupcakeAdapter.startListening();
        super.onStart();
    }

    public void onBackPressed() {
        super.onBackPressed();
        cupcakeAdapter.stopListening();
    }
    public void toOrders(View view){
        Intent i = new Intent(this,MemberOrderActivity.class);
        i.putExtra("username", user);
        startActivity(i);
    }
    public void toHome(View view){
        Intent i = new Intent(this,MemberHomeActivity.class);
        i.putExtra("username", user);
        startActivity(i);
    }

    public void toCart(View view){
        Intent i = new Intent(this,CartActivity.class);
        i.putExtra("username", user);
        startActivity(i);
    }

}
