package com.example.altas.ui.shop;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.altas.MainActivity;
import com.example.altas.Models.Product;
import com.example.altas.R;
import com.example.altas.ui.list.adepters.ShopListAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {

    private ShopViewModel mViewModel;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // Not sure why we need this, keep it here until we find out
    public static ShopFragment newInstance() {
        return new ShopFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate shop fragment's view
        View shopFragmentRoot = inflater.inflate(R.layout.fragment_shop, container, false);

        mRecyclerView = shopFragmentRoot.findViewById(R.id.shop_products_recycler_view);

        List<Product> products = new ArrayList<>();
        products.add( new Product() );
        products.add( new Product() );
        products.add( new Product() );
        products.add( new Product() );
        products.add( new Product() );
        products.add( new Product() );
        products.add( new Product() );
        products.add( new Product() );
        products.add( new Product() );
        products.add( new Product() );

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager

        mLayoutManager = new LinearLayoutManager( getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter and pass in our data model list

        mAdapter = new ShopListAdapter(products, getContext());
        mRecyclerView.setAdapter(mAdapter);

        return shopFragmentRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ShopViewModel.class);
        // TODO: Use the ViewModel
    }

}
