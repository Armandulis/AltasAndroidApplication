package com.example.altas.ui.list.adepters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.altas.Models.Product;
import com.example.altas.R;

import java.util.ArrayList;

/**
 * Public class ShopListAdapter Used to customize the way items are showed in the list
 */
public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.MyViewHolder> {

    private ArrayList<Product> dataModelList;
    private Context mContext;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImageView;
        TextView titleTextView;
        TextView subTitleTextView;
        TextView priceTextView;
        TextView brandTextView;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImageView = itemView.findViewById(R.id.shop_list_item_image_view);
            titleTextView = itemView.findViewById(R.id.shop_list_item_title);
            subTitleTextView = itemView.findViewById(R.id.shop_list_item_subtitle);
            priceTextView = itemView.findViewById(R.id.shop_list_item_price);
            brandTextView = itemView.findViewById(R.id.shop_list_item_brand);
        }

        void bindData(Product product, Context context) {
            cardImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_lt));
            titleTextView.setText(product.name);
            subTitleTextView.setText(product.description);
            priceTextView.setText(product.price);
            brandTextView.setText(product.brand);
        }
    }

    public ShopListAdapter(ArrayList<Product> modelList, Context context) {
        dataModelList = modelList;
        mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate out card list item

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_list_item_layout, parent, false);
        // Return a new view holder

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Bind data for the item at position
        holder.bindData(dataModelList.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        // Return the total number of items
        return dataModelList.size();
    }
}
