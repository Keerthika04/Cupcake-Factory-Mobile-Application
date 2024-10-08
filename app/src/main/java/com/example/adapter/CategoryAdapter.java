package com.example.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.model.CategoryHelperClass;
import com.example.thecupcakefactory.AdminCategoryActivity;
import com.example.thecupcakefactory.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;


public class CategoryAdapter extends FirebaseRecyclerAdapter<CategoryHelperClass, CategoryAdapter.ViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public CategoryAdapter(@NonNull FirebaseRecyclerOptions<CategoryHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position, @NonNull CategoryHelperClass model) {
        int categoryPosition = position;
        holder.categoryTxt.setText(model.getCategoryName());
        Glide.with(holder.categoryImg.getContext())
                .load(model.getCategoryImage())
                .placeholder(R.drawable.loading_image_foreground)
                .into(holder.categoryImg);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = holder.getAdapterPosition();
                ((AdminCategoryActivity) view.getContext()).getPosition(position);

                ((AdminCategoryActivity) view.getContext()).onUpdateCategory();
                ((AdminCategoryActivity) view.getContext()).updateCategoryNameTxt.setText(model.getCategoryName());
                Glide.with(view.getContext())
                        .load(model.getCategoryImage())
                        .placeholder(R.drawable.loading_image_foreground)
                        .into(((AdminCategoryActivity) view.getContext()).updateCategoryImg);

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Are you Sure?");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("categories").child(getRef(categoryPosition).getKey()).removeValue();
                        FirebaseStorage.getInstance().getReference().child("categories/" + model.getCategoryName()).delete();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView categoryTxt;
        ImageView categoryImg,edit,delete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImg=itemView.findViewById(R.id.categoryImg);
            categoryTxt=itemView.findViewById(R.id.categoryTxt);
            edit=itemView.findViewById(R.id.edit);
            delete=itemView.findViewById(R.id.delete);


        }
    }

}
