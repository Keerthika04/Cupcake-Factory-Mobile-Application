package com.example.thecupcakefactory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapter.CartAdapter;
import com.example.adapter.OrderAdapter;
import com.example.model.OrderHelperClass;
import com.example.model.OrderHelperClass;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MemberOrderActivity extends AppCompatActivity {
    TextView bug;
    RecyclerView orderItems;
    OrderAdapter orderAdapter;
    ProgressBar progressBar;
    String user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_order);

        Bundle extras = getIntent().getExtras();
        user = extras.getString("username");

        bug = findViewById(R.id.bug);

        showOrders(user);
    }

    private void showOrders(String user) {
        bug.setText(user);

        orderItems = findViewById(R.id.orderItems);
        orderItems.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar);

        FirebaseRecyclerOptions<OrderHelperClass> options = new FirebaseRecyclerOptions.Builder<OrderHelperClass>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("orders").orderByChild("user").equalTo(user), OrderHelperClass.class)
                .build();

        orderAdapter = new OrderAdapter(options);
        orderItems.setAdapter(orderAdapter);

        orderAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
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

    @Override
    protected void onStart() {
        super.onStart();
        if (orderAdapter != null) {
            orderAdapter.startListening();
        }
    }

    @Override
    public void onBackPressed() {
        orderAdapter.stopListening();
        super.onBackPressed();
    }
}
