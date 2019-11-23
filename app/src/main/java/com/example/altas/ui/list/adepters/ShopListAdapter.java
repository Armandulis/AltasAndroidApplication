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
import com.example.altas.ui.list.adepters.IRecyclerViewSupport.IRecyclerViewButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * Public class ShopListAdapter Used to customize the way items are showed in the list
 */
public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.MyViewHolder> {

    private ArrayList<Product> dataModelList;
    private Context mContext;
    IRecyclerViewButtonClickListener basketButtonClickListener;


    /**
     * MyViewHolder holds view items and sets values to them
     */
    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final IRecyclerViewButtonClickListener mListener;
        ImageView cardImageView;
        TextView titleTextView;
        TextView priceTextView;
        TextView brandTextView;
        FloatingActionButton buttonAddToCart;

        /**
         * Initializes local variables to items from layout
         *
         * @param itemView View
         */
        MyViewHolder(@NonNull View itemView, IRecyclerViewButtonClickListener listener) {
            super(itemView);
            cardImageView = itemView.findViewById(R.id.shop_list_item_image_view);
            titleTextView = itemView.findViewById(R.id.shop_list_item_title);
            priceTextView = itemView.findViewById(R.id.shop_list_item_price);
            brandTextView = itemView.findViewById(R.id.shop_list_item_brand);
            buttonAddToCart = itemView.findViewById(R.id.shop_list_item_add_to_cart_button);

            mListener = listener;
            buttonAddToCart.setOnClickListener(this);
        }

        /**
         * Sets values to layouts items
         *
         * @param product Product
         * @param context Context
         */
        void bindData(final Product product, Context context) {
            cardImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_lt));
            titleTextView.setText(product.name);
            priceTextView.setText(product.price);
            brandTextView.setText(product.brand);


        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }

    /**
     * ShopListAdapter's constructor
     *
     * @param modelList Array<Product>
     * @param context   Context
     */
    public ShopListAdapter(ArrayList<Product> modelList, Context context, IRecyclerViewButtonClickListener listener) {
        dataModelList = modelList;
        mContext = context;
        this.basketButtonClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate out card list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_list_item_layout, parent, false);

        // Return a new view holder
        return new MyViewHolder(view, basketButtonClickListener);
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

    /**
     * Returns an item from the list in given position
     *
     * @param productAtPosition int
     * @return Product at given position
     */
    public Product getItemFromList(int productAtPosition) {
        return dataModelList.get(productAtPosition);
    }

}
