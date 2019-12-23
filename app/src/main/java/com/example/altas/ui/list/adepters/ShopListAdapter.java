package com.example.altas.ui.list.adepters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
    private IRecyclerViewButtonClickListener basketButtonClickListener;
    private boolean isBasket;


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
         * @param listener listener for button in ViewHolder
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
         * @param product  Product
         * @param context  Context
         * @param isBasket boolean, checks if we call adapter from basket fragment
         */
        void bindData(final Product product, Context context, boolean isBasket) {

            // Check if picture is set
            if (product.pictureUrl != null && !product.pictureUrl.equals("")) {
                Glide.with(context)
                        .load(Uri.parse(product.pictureUrl))
                        .into(cardImageView);
            }

            titleTextView.setText(product.name);
            priceTextView.setText(product.price);
            brandTextView.setText(product.brand);

            // If it is basket, we want to change image for basket button
            if (isBasket) {
                buttonAddToCart.setImageResource(R.drawable.ic_shopping_cart_remove_black_24dp);
            }
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
     * @param listener for button clicked in ViewHolder
     * @param isBasket Validate if adapter is needed for basket
     */
    public ShopListAdapter(ArrayList<Product> modelList, Context context, IRecyclerViewButtonClickListener listener, boolean isBasket) {
        dataModelList = modelList;
        mContext = context;
        this.basketButtonClickListener = listener;
        this.isBasket = isBasket;
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
        holder.bindData(dataModelList.get(position), mContext, isBasket);
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
