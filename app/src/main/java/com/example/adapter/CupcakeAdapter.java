package com.example.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.model.CupcakeHelperClass;
import com.example.thecupcakefactory.AdminCupcakeActivity;
import com.example.thecupcakefactory.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;


public class CupcakeAdapter extends FirebaseRecyclerAdapter<CupcakeHelperClass, CupcakeAdapter.ViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private List<CupcakeHelperClass> cupcakeList;

    public CupcakeAdapter(@NonNull FirebaseRecyclerOptions<CupcakeHelperClass> options) {
        super(options);
        cupcakeList = new ArrayList<>();
    }

    @Override
    protected void onBindViewHolder(@NonNull CupcakeAdapter.ViewHolder holder, int position, @NonNull CupcakeHelperClass model) {
        int cupcakePosition = position;
        if (cupcakeList != null && !cupcakeList.isEmpty()) {
            CupcakeHelperClass cupcake = cupcakeList.get(position);
            holder.cupcakeNameTxt.setText(cupcake.getCupcakeName());
            holder.cupcakeCategoryTxt.setText(cupcake.getCupcakeCategory() + " Category");
            holder.cupcakePriceTxt.setText("Rs. " + cupcake.getCupcakePrice() + "/-");
            holder.cupcakeIngredientsList.setText(cupcake.getCupcakeIngredients());
            Glide.with(holder.cupcakeImg.getContext())
                    .load(cupcake.getCupcakeImage())
                    .placeholder(R.drawable.loading_image_foreground)
                    .into(holder.cupcakeImg);
        }else {
            holder.cupcakeNameTxt.setText(model.getCupcakeName());
            holder.cupcakeCategoryTxt.setText(model.getCupcakeCategory() + " Category");
            holder.cupcakePriceTxt.setText("Rs. " + model.getCupcakePrice() + "/-");
            holder.cupcakeIngredientsList.setText(model.getCupcakeIngredients());
            Glide.with(holder.cupcakeImg.getContext())
                    .load(model.getCupcakeImage())
                    .placeholder(R.drawable.loading_image_foreground)
                    .into(holder.cupcakeImg);
        }
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                ((AdminCupcakeActivity) view.getContext()).getPosition(position);

                ((AdminCupcakeActivity) view.getContext()).onUpdateCupcake();
                ((AdminCupcakeActivity) view.getContext()).updateNewCupcakeNameTxt.setText(model.getCupcakeName());
                ((AdminCupcakeActivity) view.getContext()).updateAutoCompleteCategoryTxt.setText(model.getCupcakeCategory());
                ((AdminCupcakeActivity) view.getContext()).updateNewCupcakePriceTxt.setText(model.getCupcakePrice());
                ((AdminCupcakeActivity) view.getContext()).updateNewCupcakeIngredientsTxt.setText(model.getCupcakeIngredients());
                Glide.with(view.getContext())
                        .load(model.getCupcakeImage())
                        .placeholder(R.drawable.loading_image_foreground)
                        .into(((AdminCupcakeActivity) view.getContext()).updateNewCupcakeImg);
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
                        FirebaseDatabase.getInstance().getReference().child("cupcakes").child(getRef(cupcakePosition).getKey()).removeValue();
                        FirebaseStorage.getInstance().getReference().child("cupcakes/" + model.getCupcakeName()).delete();
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

    public void searchCupcake(ArrayList<CupcakeHelperClass> cupcakes) {
        cupcakeList = cupcakes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CupcakeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cupcake_card, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView cupcakeNameTxt, cupcakeCategoryTxt, cupcakePriceTxt, cupcakeIngredientsList;
        ImageView cupcakeImg,edit,delete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cupcakeImg=itemView.findViewById(R.id.cartCupcakeImg);
            cupcakeNameTxt=itemView.findViewById(R.id.cupcakeNameTxt);
            cupcakeCategoryTxt=itemView.findViewById(R.id.cupcakeCategoryTxt);
            cupcakePriceTxt=itemView.findViewById(R.id.cupcakePriceTxt);
            cupcakeIngredientsList=itemView.findViewById(R.id.cupcakeIngredientsList);
            edit=itemView.findViewById(R.id.edit);
            delete=itemView.findViewById(R.id.delete);

        }
    }
}
