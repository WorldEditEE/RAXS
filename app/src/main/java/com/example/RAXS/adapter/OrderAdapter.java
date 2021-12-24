package com.example.RAXS.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.RAXS.OrderEntity;
import com.example.RAXS.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{

    private List<OrderEntity> orderEntityList;

    public OrderAdapter(List<OrderEntity> orderEntityList) {
        this.orderEntityList = orderEntityList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.row_order,parent, false);

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        OrderEntity currentOrder = orderEntityList.get(position);
        holder.textViewProduct.setText(currentOrder.getAnswer());
        int count = currentOrder.getCount();
        holder.textViewCount.setText(String.valueOf(count));

    }

    @Override
    public int getItemCount() {
        return orderEntityList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewProduct;

        private TextView textViewCount;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewProduct = itemView.findViewById(R.id.product);
            textViewCount = itemView.findViewById(R.id.count);
        }
    }
}
