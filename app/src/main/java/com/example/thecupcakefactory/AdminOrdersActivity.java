package com.example.thecupcakefactory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapter.AdminOrdersAdapter;
import com.example.adapter.OrderAdapter;
import com.example.model.OrderHelperClass;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class AdminOrdersActivity extends AppCompatActivity {

    RecyclerView adminOrders;
    AdminOrdersAdapter adminOrdersAdapter;
    ProgressBar progressBar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);
        orders();
    }

    private void orders() {
        adminOrders = findViewById(R.id.adminOrders);
        adminOrders.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar);

        FirebaseRecyclerOptions<OrderHelperClass> options = new FirebaseRecyclerOptions.Builder<OrderHelperClass>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("orders"), OrderHelperClass.class)
                .build();

        adminOrdersAdapter = new AdminOrdersAdapter(options);
        adminOrders.setAdapter(adminOrdersAdapter);

        adminOrdersAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                progressBar.setVisibility(View.GONE);
            }

            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    protected void onStart() {
        adminOrdersAdapter.startListening();
        super.onStart();
    }

    }
