package com.example.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.model.CupcakeHelperClass;
import com.example.thecupcakefactory.CupcakeDetailActivity;
import com.example.thecupcakefactory.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class MemberCupcakeAdapter extends FirebaseRecyclerAdapter<CupcakeHelperClass, MemberCupcakeAdapter.ViewHolder>  {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */


    public MemberCupcakeAdapter(@NonNull FirebaseRecyclerOptions<CupcakeHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MemberCupcakeAdapter.ViewHolder holder, int position, @NonNull CupcakeHelperClass model) {
        holder.cupcakeName.setText(model.getCupcakeName());
        holder.cupcakePrice.setText("Rs. " +model.getCupcakePrice() + "/-");

        Glide.with(holder.cupcakeImg.getContext())
                .load(model.getCupcakeImage())
                .placeholder(R.drawable.loading_image_foreground)
                .into(holder.cupcakeImg);

        holder.memberCupcakeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String image = model.getCupcakeImage();
                String name = model.getCupcakeName();
                String price = model.getCupcakePrice();
                String ingredients = model.getCupcakeIngredients();

                Intent intent = new Intent(view.getContext(), CupcakeDetailActivity.class);
                intent.putExtra("Image", image);
                intent.putExtra("Name", name);
                intent.putExtra("Price", price);
                intent.putExtra("Ingredients", ingredients);

                view.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public MemberCupcakeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_cupcake_card, parent, false);
        return new MemberCupcakeAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView cupcakeName, cupcakePrice;
        ImageView cupcakeImg,wishlist;

        CardView memberCupcakeView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cupcakeImg=itemView.findViewById(R.id.cartCupcakeImg);
            cupcakeName=itemView.findViewById(R.id.cartCupcakeName);
            cupcakePrice=itemView.findViewById(R.id.cartCupcakePrice);
            memberCupcakeView=itemView.findViewById(R.id.memberCupcakeView);

            wishlist=itemView.findViewById(R.id.wishlist);

        }
    }
}
