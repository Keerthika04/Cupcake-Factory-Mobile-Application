package com.example.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.model.CartHelperClass;
import com.example.thecupcakefactory.MemberHomeActivity;
import com.example.thecupcakefactory.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class CartAdapter extends FirebaseRecyclerAdapter<CartHelperClass, CartAdapter.ViewHolder> {

    private final OnQuantityChangeListener onQuantityChangeListener;

    public CartAdapter(@NonNull FirebaseRecyclerOptions<CartHelperClass> options, OnQuantityChangeListener listener) {
        super(options);
        this.onQuantityChangeListener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull CartHelperClass model) {
        holder.cupcakeName.setText(model.getName());
        holder.displayCount.setText(model.getQuantity());
        holder.cupcakePrice.setText("Rs. " + (Integer.parseInt(model.getPrice()) * Integer.parseInt(model.getQuantity())) + "/-");

        Glide.with(holder.cupcakeImg.getContext())
                .load(model.getImg())
                .transform(new CenterCrop(), new RoundedCorners(22))
                .placeholder(R.drawable.loading_image_foreground)
                .into(holder.cupcakeImg);

        holder.incrementBtn.setOnClickListener(v -> {
            int count = Integer.parseInt(model.getQuantity());
            count++;
            updateQuantity(holder, model, position, count);
        });

        holder.decrementBtn.setOnClickListener(v -> {
            int count = Integer.parseInt(model.getQuantity());
            if (count > 1) {
                count--;
                updateQuantity(holder, model, position, count);
            }
        });

        holder.delete.setOnClickListener(v -> {
            String user = MemberHomeActivity.user;
            FirebaseDatabase.getInstance().getReference().child("cart").child(user).child(getRef(position).getKey()).removeValue();
        });
    }

    private void updateQuantity(@NonNull ViewHolder holder, @NonNull CartHelperClass model, int position, int count) {
        String user = MemberHomeActivity.user;
        FirebaseDatabase.getInstance().getReference().child("cart").child(user).child(getRef(position).getKey()).child("quantity").setValue(String.valueOf(count));
        model.setQuantity(String.valueOf(count));
        holder.displayCount.setText(String.valueOf(count));
        holder.cupcakePrice.setText("Rs. " + (Integer.parseInt(model.getPrice()) * count) + "/-");
        onQuantityChangeListener.onQuantityChange();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_cart, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cupcakeName, cupcakePrice, displayCount;
        ImageView cupcakeImg, decrementBtn, incrementBtn, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cupcakeImg = itemView.findViewById(R.id.cartCupcakeImg);
            cupcakeName = itemView.findViewById(R.id.cartCupcakeName);
            cupcakePrice = itemView.findViewById(R.id.cartCupcakePrice);
            displayCount = itemView.findViewById(R.id.displayCount);
            decrementBtn = itemView.findViewById(R.id.decrementBtn);
            incrementBtn = itemView.findViewById(R.id.incrementBtn);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    public interface OnQuantityChangeListener {
        void onQuantityChange();
    }
}
