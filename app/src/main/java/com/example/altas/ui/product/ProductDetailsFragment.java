package com.example.altas.ui.product;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.altas.MainActivity;
import com.example.altas.Models.Product;
import com.example.altas.Models.User;
import com.example.altas.R;
import com.example.altas.repositories.BasketRepository;
import com.example.altas.ui.shop.ShopFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import static android.content.Context.MODE_PRIVATE;
import static com.example.altas.MainActivity.ALTAS_PREF_NAME;
import static com.example.altas.MainActivity.BASKET_UUID;

/**
 * Public class ProductDetailFragment that extends Fragments, handles single Product's details
 */
public class ProductDetailsFragment extends Fragment {

    private Product product;
    private TextView textViewProductDescription;
    private TextView textViewProductTitle;
    private TextView textViewProductPrice;
    private TextView textViewProductBrand;
    private TextView textViewProductAmount;
    private ImageView imageViewProductImage;

    private FloatingActionButton buttonAddToCart;
    private BasketRepository basketRepository;


    private RequestQueue queue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Initialize fragment's layout
        View root = inflater.inflate(R.layout.fragment_product_details, container, false);

        // Initialize class variables
        product = (Product) getArguments().getSerializable(ShopFragment.SELECTED_PRODUCT_KEY);
        basketRepository = new BasketRepository();
        textViewProductDescription = root.findViewById(R.id.text_view_product_details_description);
        textViewProductDescription.setMovementMethod(new ScrollingMovementMethod());
        textViewProductTitle = root.findViewById(R.id.text_view_product_details_title);
        textViewProductPrice = root.findViewById(R.id.text_view_product_details_price);
        textViewProductBrand = root.findViewById(R.id.text_view_product_details_brand);
        textViewProductAmount = root.findViewById(R.id.text_view_product_details_amount);
        imageViewProductImage = root.findViewById(R.id.image_view_details_image);
        buttonAddToCart = root.findViewById(R.id.button_details_add_to_cart);
        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });


        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(getContext());

        // Set up Action bar
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            // Disable back button in toolbar and change it's title if it exists
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle(product.name);
        }

        putProductsValuesToLayout();

        return root;
    }

    /**
     * Handles adding product to cart
     */
    private void addToCart() {
        // Get basket's id
        SharedPreferences prefs = getActivity().getSharedPreferences(ALTAS_PREF_NAME, MODE_PRIVATE);
        String basketUUID = prefs.getString(BASKET_UUID, null);

        User user = User.getInstance();
        if (user.email != null && !user.email.equals("")) {
            basketUUID = user.email;
        }
        basketRepository.addProductToBasket(basketUUID, product.id, queue);

        // Inform user that product was added
        Snackbar.make(getParentFragment().getView(), product.name + " " + getString(R.string.product_was_added), Snackbar.LENGTH_SHORT)
                .show();
    }

    /**
     * Places values from product to Layout for user to see
     */
    private void putProductsValuesToLayout() {
        textViewProductDescription.setText(product.description);
        textViewProductTitle.setText(product.name);
        textViewProductPrice.setText(product.price);
        textViewProductBrand.setText(product.brand);
        textViewProductAmount.setText(product.amount);

        // Check if picture url is set
        if (product.pictureUrl != null && !product.pictureUrl.equals("")) {
            // Parse url to image view
            Glide.with(getActivity())
                    .load(Uri.parse(product.pictureUrl))
                    .into(imageViewProductImage);
        }
    }
}
