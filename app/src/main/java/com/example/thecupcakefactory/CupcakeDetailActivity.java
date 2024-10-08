package com.example.thecupcakefactory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.model.CartHelperClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CupcakeDetailActivity extends AppCompatActivity {
    TextView cupcakeNameInCard, cupcakePriceInCard, cupcakeIngredientsInCard, totalPriceTxt, displayCount;
    ImageView cupcakeCardImg, incrementBtn, decrementBtn;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    String imgTxt, nameTxt, priceTxt, quantityTxt;
    int count = 1;
    int price;

    Button addToCartBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cupcake_info_card);

        quantityTxt = String.valueOf(count);

        cupcakeNameInCard = findViewById(R.id.cupcakeNameInCard);
        cupcakePriceInCard = findViewById(R.id.cupcakePriceInCard);
        cupcakeCardImg = findViewById(R.id.cupcakeCardImg);
        cupcakeIngredientsInCard = findViewById(R.id.cupcakeIngredientsInCard);
        totalPriceTxt = findViewById(R.id.totalPriceTxt);
        addToCartBtn = findViewById(R.id.addToCartBtn);
        displayCount = findViewById(R.id.displayCount);
        incrementBtn = findViewById(R.id.incrementBtn);
        decrementBtn = findViewById(R.id.decrementBtn);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Glide.with(this)
                    .load(bundle.getString("Image"))
                    .apply(new RequestOptions().placeholder(R.drawable.upload_img))
                    .into(cupcakeCardImg);
            cupcakeNameInCard.setText(bundle.getString("Name"));
            cupcakePriceInCard.setText("Rs. " + bundle.getString("Price") + "/-");
            cupcakeIngredientsInCard.setText(bundle.getString("Ingredients"));
            price = Integer.parseInt(bundle.getString("Price"));
            totalPriceTxt.setText(String.valueOf("Rs. " + price + "/-"));
            imgTxt = bundle.getString("Image");
            nameTxt = bundle.getString("Name");
            priceTxt = bundle.getString("Price");
        }

        displayCount.setText(String.valueOf(count));

        incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                displayCount.setText(String.valueOf(count));
                totalPriceTxt.setText("Rs. " + String.valueOf(price * count) + "/-");
                quantityTxt = String.valueOf(count);
            }
        });

        decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 1) {
                    count--;
                    displayCount.setText(String.valueOf(count));
                    totalPriceTxt.setText("Rs. " + String.valueOf(price * count) + "/-");
                    quantityTxt = String.valueOf(count);
                }
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart();
            }
        });

    }

    public void addToCart(){
        String user = MemberHomeActivity.user;
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("cart").child(user);
        CartHelperClass cart = new CartHelperClass(imgTxt, nameTxt, priceTxt, quantityTxt);
        reference.push().setValue(cart)
        .addOnSuccessListener(aVoid -> {
            Toast.makeText(CupcakeDetailActivity.this, "Saved to cart", Toast.LENGTH_SHORT).show();
            count = 1;
        })
                .addOnFailureListener(e -> {
                    Toast.makeText(CupcakeDetailActivity.this, "Failed to saved to cart", Toast.LENGTH_SHORT).show();
                });
    }

}
