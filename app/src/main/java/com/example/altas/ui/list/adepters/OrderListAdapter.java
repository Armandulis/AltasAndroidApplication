package com.example.altas.ui.list.adepters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.altas.Models.ProductStatus;
import com.example.altas.R;
import com.example.altas.ui.list.adepters.IRecyclerViewSupport.IRecyclerViewButtonClickListener;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder> {

    private ArrayList<ProductStatus> dataModelList;
    private Context mContext;

    private IRecyclerViewButtonClickListener confirmButtonListener;

    /**
     * MyViewHolder holds view items and sets values to them
     */
    static class OrderListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final IRecyclerViewButtonClickListener mListener;
        ImageView cardImageView;
        TextView titleTextView;
        TextView dateTextView;
        Button buttonStatus;
        Button buttonConfirmDelivered;

        /**
         * Initializes local variables to items from layout
         *
         * @param itemView View
         */
        OrderListViewHolder(@NonNull View itemView, IRecyclerViewButtonClickListener listener) {
            super(itemView);
            cardImageView = itemView.findViewById(R.id.order_status_item_image_view);
            titleTextView = itemView.findViewById(R.id.order_status_item_title);
            dateTextView = itemView.findViewById(R.id.order_status_text_date);
            buttonStatus = itemView.findViewById(R.id.order_status_button_status);
            buttonConfirmDelivered = itemView.findViewById(R.id.order_button_confirm);

            mListener = listener;
            buttonConfirmDelivered.setOnClickListener(this);
        }

        /**
         * Sets values to layouts items
         *
         * @param productStatus ProductStatus
         * @param context Context
         */
        void bindData(final ProductStatus productStatus, Context context) {
            if (productStatus.product.pictureUrl != null && !productStatus.product.pictureUrl.equals("")){
                Glide.with(context)
                        .load(Uri.parse(productStatus.product.pictureUrl))
                        .into(cardImageView);
            }
            titleTextView.setText(productStatus.product.name);
            dateTextView.setText(productStatus.purchaseDate);
            buttonStatus.setText(productStatus.status);

            // If status is already confirmed delivery by user, we want a button that removes ProductStatus
            if(productStatus.status.equals(context.getString(R.string.confirmed_delivery))){
                buttonConfirmDelivered.setText(R.string.remove_product);
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
     */
    public OrderListAdapter(ArrayList<ProductStatus> modelList, Context context, IRecyclerViewButtonClickListener listener) {
        dataModelList = modelList;
        mContext = context;
        this.confirmButtonListener = listener;
    }

    @NonNull
    @Override
    public OrderListAdapter.OrderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate out card list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_status_list_item_layout, parent, false);

        // Return a new view holder
        return new OrderListAdapter.OrderListViewHolder(view, confirmButtonListener);
    }


    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.OrderListViewHolder holder, int position) {
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
     * @param productStatusAtPosition int
     * @return ProductStatus at given position
     */
    public ProductStatus getItemFromList(int productStatusAtPosition) {
        return dataModelList.get(productStatusAtPosition);
    }

}