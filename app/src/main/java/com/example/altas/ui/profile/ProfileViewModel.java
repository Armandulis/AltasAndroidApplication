package com.example.altas.ui.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.altas.Models.ProductStatus;
import com.example.altas.repositories.AuthenticationRepository;
import com.example.altas.repositories.ProductStatusRepository;

import java.util.ArrayList;

/**
 * public class ProfileViewModel that extends ViewModel
 */
public class ProfileViewModel extends ViewModel {

    public MutableLiveData<ArrayList<ProductStatus>> productStatusListMutableLiveData;
    private AuthenticationRepository authRepo;
    private ProductStatusRepository statusRepo;
    private ArrayList<ProductStatus> productStatusList;

    /**
     * ProfileViewModel constructor
     */
    public ProfileViewModel() {

        // Initialize variables
        authRepo = new AuthenticationRepository();
        statusRepo = new ProductStatusRepository();
        productStatusListMutableLiveData = new MutableLiveData<>();

    }

    /**
     * Calls repo to get statuses
     *
     * @param userId user who's productStatuses we want to get
     */
    public void getAllUserProductStatus(String userId) {
        productStatusList = statusRepo.getAllProducts(userId);
        productStatusListMutableLiveData.setValue(productStatusList);
    }

    /**
     * Removes productStatus from local list and calls repo to remove it from database
     *
     * @param productStatus productStatus that needs to be removed
     */
    public void removeProductStatus(ProductStatus productStatus) {

        // Remove productStatus locally
        productStatusList.remove(productStatus);
        productStatusListMutableLiveData.setValue(productStatusList);

        // Call repo to remove it from database
        statusRepo.removeProductStatus(productStatus.id);
    }


    /**
     * Updates product status locally and calls repo to do it in the database as well
     *
     * @param productStatus productStatus that will be updated
     * @param position      of productStatus that needs to be updated
     */
    public void confirmDelivered(ProductStatus productStatus, int position) {

        // Update productStatus locally
        productStatusList.set(position, productStatus);
        productStatusListMutableLiveData.setValue(productStatusList);

        // Call repo to update it from database
        statusRepo.updateProductStatus(productStatus);
    }
}
