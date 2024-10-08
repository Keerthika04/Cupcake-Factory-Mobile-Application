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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.model.OrderHelperClass;
import com.example.thecupcakefactory.MemberHomeActivity;
import com.example.thecupcakefactory.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class OrderAdapter extends FirebaseRecyclerAdapter<OrderHelperClass, OrderAdapter.ViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public OrderAdapter(@NonNull FirebaseRecyclerOptions<OrderHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position, @NonNull OrderHelperClass model) {
        int cupcakeId = position;
        if(model.isCancelled() == false) {
            String id = String.valueOf(position + 1);
            holder.cupcakeName.setText(model.getName());
            holder.orderId.setText("C0" + id);
            holder.orderProcessStatus.setText("Status : " + model.getStatus());

            Glide.with(holder.cupcakeImg.getContext())
                    .load(model.getImg())
                    .transform(new CenterCrop(), new RoundedCorners(22))
                    .placeholder(R.drawable.loading_image_foreground)
                    .into(holder.cupcakeImg);

            if(!(model.getStatus().equals("Out for Delivery") || model.getStatus().equals("Completed"))) {
                holder.delete.setOnClickListener(v -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Are you Sure?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String user = MemberHomeActivity.user;
                            FirebaseDatabase.getInstance().getReference().child("orders").child(getRef(cupcakeId).getKey()).child("cancelled").setValue(true);
                            FirebaseDatabase.getInstance().getReference().child("orders").child(getRef(cupcakeId).getKey()).child("status").setValue("Cancelled");
                        }
                    });
                    builder.setNegativeButton("Cancel", null);

                    builder.show();

                });
            }else{
                holder.delete.setVisibility(View.GONE);
            }
        }else {
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_order_cart, parent, false);
        return new OrderAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cupcakeName, orderId, orderProcessStatus;
        ImageView cupcakeImg, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cupcakeImg = itemView.findViewById(R.id.orderCupcakeImg);
            cupcakeName = itemView.findViewById(R.id.orderCupcakeName);
            orderProcessStatus = itemView.findViewById(R.id.orderProcessStatus);
            orderId = itemView.findViewById(R.id.orderId);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
