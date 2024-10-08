package com.example.adapter;

import android.app.AlertDialog;
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
import com.example.model.CategoryHelperClass;
import com.example.model.CupcakeHelperClass;
import com.example.thecupcakefactory.CategoryDetailActivity;
import com.example.thecupcakefactory.CupcakeDetailActivity;
import com.example.thecupcakefactory.LoginActivity;
import com.example.thecupcakefactory.MemberHomeActivity;
import com.example.thecupcakefactory.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class MemberCategoryAdapter extends FirebaseRecyclerAdapter<CategoryHelperClass, MemberCategoryAdapter.ViewHolder>  {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */


    public MemberCategoryAdapter(@NonNull FirebaseRecyclerOptions<CategoryHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MemberCategoryAdapter.ViewHolder holder, int position, @NonNull CategoryHelperClass model) {
        Glide.with(holder.categoryImg.getContext())
                .load(model.getCategoryImage())
                .placeholder(R.drawable.loading_image_foreground)
                .into(holder.categoryImg);

        holder.categoryImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = model.getCategoryName();
                String user = MemberHomeActivity.user;

                Intent intent = new Intent(view.getContext(), CategoryDetailActivity.class);
                intent.putExtra("Name", name);
                intent.putExtra("username",user);

                view.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public MemberCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_btn, parent, false);
        return new MemberCategoryAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView categoryImg;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryImg=itemView.findViewById(R.id.categoryImg);

        }
    }
}
