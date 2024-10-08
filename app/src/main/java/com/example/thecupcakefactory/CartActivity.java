package com.example.thecupcakefactory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapter.CartAdapter;
import com.example.model.CartHelperClass;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnQuantityChangeListener {

    RecyclerView cartItems;
    CartAdapter cartAdapter;
    ProgressBar progressBar;
    TextView bug, subTotalTxt, totalTaxTxt, totalTxt, deliveryTxt;
    Button checkoutBtn;

    private DatabaseReference ordersReference, cartReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_cart);

        Bundle extras = getIntent().getExtras();
        String user = extras.getString("username");

        bug = findViewById(R.id.bug);
        subTotalTxt = findViewById(R.id.subTotalTxt);
        totalTaxTxt = findViewById(R.id.totalTaxTxt);
        totalTxt = findViewById(R.id.totalTxt);
        deliveryTxt = findViewById(R.id.deliveryTxt);
        checkoutBtn = findViewById(R.id.checkoutBtn);

        cartReference = FirebaseDatabase.getInstance().getReference().child("cart").child(user);
        ordersReference = FirebaseDatabase.getInstance().getReference().child("orders");

        showCart(user);

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orders(user);
            }
        });
    }

    private void showCart(String user) {
        bug.setText(user);

        cartItems = findViewById(R.id.cartItems);
        cartItems.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar);

        FirebaseRecyclerOptions<CartHelperClass> options = new FirebaseRecyclerOptions.Builder<CartHelperClass>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("cart").child(user), CartHelperClass.class)
                .build();

        cartAdapter = new CartAdapter(options, this);
        cartItems.setAdapter(cartAdapter);

        cartAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                updateTotal();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                updateTotal();
                progressBar.setVisibility(View.GONE);
            }
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                progressBar.setVisibility(View.GONE);
                updateTotal();
            }
        });

    }
    public void orders(String user){

        cartReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
                    String cartItemId = cartSnapshot.getKey();
                    CartHelperClass cartItem = cartSnapshot.getValue(CartHelperClass.class);

                    String orderId = ordersReference.push().getKey();
                    ordersReference.child(orderId).setValue(cartItem);
                    cartReference.child(cartItemId).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("orders").child(orderId).child("user").setValue(user);
                    FirebaseDatabase.getInstance().getReference().child("orders").child(orderId).child("cancelled").setValue(false);
                    FirebaseDatabase.getInstance().getReference().child("orders").child(orderId).child("status").setValue("Being Reviewed");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CartActivity.this, "Failed to place order", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onQuantityChange() {
        updateTotal();
    }

    private void updateTotal() {
        double subTotal = 0;
        for (int i = 0; i < cartAdapter.getItemCount(); i++) {
            CartHelperClass item = cartAdapter.getItem(i);
            subTotal += Integer.parseInt(item.getPrice()) * Integer.parseInt(item.getQuantity());
        }

        double tax = Math.round(subTotal * 0.02);
        double delivery = 125;
        double total = subTotal + tax + delivery;

        subTotalTxt.setText("Rs." + subTotal + "/-");
        totalTaxTxt.setText("Rs." + tax + "/-");
        totalTxt.setText("Rs." + total + "/-");
        deliveryTxt.setText("Rs." + delivery + "/-");
    }

    private double calculateTotal() {
        double subTotal = 0.0;
        for (int i = 0; i < cartAdapter.getItemCount(); i++) {
            CartHelperClass item = cartAdapter.getItem(i);
            subTotal += Integer.parseInt(item.getPrice()) * Integer.parseInt(item.getQuantity());
        }

        double tax = subTotal * 0.05;
        double delivery = 125.0;
        return subTotal + tax + delivery;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (cartAdapter != null) {
            cartAdapter.startListening();
        }
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("total", calculateTotal());
        setResult(RESULT_OK, resultIntent);
        cartAdapter.stopListening();
        super.onBackPressed();
    }
}
