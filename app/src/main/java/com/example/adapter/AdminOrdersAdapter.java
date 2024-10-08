package com.example.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.model.OrderHelperClass;
import com.example.thecupcakefactory.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class AdminOrdersAdapter extends FirebaseRecyclerAdapter<OrderHelperClass, AdminOrdersAdapter.ViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdminOrdersAdapter(@NonNull FirebaseRecyclerOptions<OrderHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AdminOrdersAdapter.ViewHolder holder, int position, @NonNull OrderHelperClass model) {
        int cupcakeId = position;
        holder.adminOrderUsername.setText("Username: " + model.getUser());
        holder.cupcakeName.setText("Cupcake: " + model.getName());
        holder.adminOrderPriceTotal.setText("Total:" + (Integer.parseInt(model.getQuantity()) * Integer.parseInt(model.getPrice())));
        holder.quantity.setText("Quantity: " + model.getQuantity());
        holder.orderProcessStatus.setText("Status : " + model.getStatus());

        Glide.with(holder.img.getContext())
                .load(model.getImg())
                .placeholder(R.drawable.loading_image_foreground)
                .into(holder.img);

        if(model.isCancelled() == false) {
            holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference().child("orders").child(getRef(cupcakeId).getKey()).child("status").setValue("Rejected");
                }
            });

            holder.processingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference().child("orders").child(getRef(cupcakeId).getKey()).child("status").setValue("Processing");
                }
            });

            holder.outForDeliveryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference().child("orders").child(getRef(cupcakeId).getKey()).child("status").setValue("Out for Delivery");
                }
            });

            holder.CompletedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference().child("orders").child(getRef(cupcakeId).getKey()).child("status").setValue("Completed");
                }
            });
        }else{
            holder.rejectBtn.setVisibility(View.GONE);
            holder.processingBtn.setVisibility(View.GONE);
            holder.outForDeliveryBtn.setVisibility(View.GONE);
            holder.CompletedBtn.setVisibility(View.GONE);
            holder.orderProcessStatus.setTextColor(Color.parseColor("#B31A19"));
        }

    }

    @NonNull
    @Override
    public AdminOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_card, parent, false);
        return new AdminOrdersAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView adminOrderNo, cupcakeName, adminOrderUsername, orderProcessStatus, adminOrderPriceTotal, quantity;
        ImageView img, rejectBtn, processingBtn, outForDeliveryBtn, CompletedBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            cupcakeName = itemView.findViewById(R.id.cupcakeName);
            adminOrderPriceTotal= itemView.findViewById(R.id.adminOrderPriceTotal);
            adminOrderNo = itemView.findViewById(R.id.cupcakeName);
            quantity = itemView.findViewById(R.id.cupcakeQuantity);
            adminOrderUsername = itemView.findViewById(R.id.adminOrderUsername);
            orderProcessStatus = itemView.findViewById(R.id.orderProcessStatus);
            rejectBtn = itemView.findViewById(R.id.rejectBtn);
            outForDeliveryBtn = itemView.findViewById(R.id.outForDeliveryBtn);
            processingBtn = itemView.findViewById(R.id.processingBtn);
            CompletedBtn = itemView.findViewById(R.id.CompletedBtn);
        }
    }
}
