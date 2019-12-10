package com.example.altas.ui.about;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.altas.repositories.AboutRepository;

/**
 * public class AboutViewModel that extends ViewModel
 */
public class AboutViewModel extends ViewModel {

    private MutableLiveData<String> aboutEmailMutableLiveData;
    private MutableLiveData<String> aboutPhoneMutableLiveData;
    private MutableLiveData<String> aboutUsMutableLiveData;

    private AboutRepository aboutRepository;

    /**
     * AboutViewModel constructor
     */
    public AboutViewModel() {
        this.aboutRepository = new AboutRepository();

        aboutEmailMutableLiveData = new MutableLiveData<>();
        aboutEmailMutableLiveData.setValue(aboutRepository.getAboutEmail());

        aboutPhoneMutableLiveData = new MutableLiveData<>();
        aboutPhoneMutableLiveData.setValue(aboutRepository.getAboutPhone());

        aboutUsMutableLiveData = new MutableLiveData<>();
        aboutUsMutableLiveData.setValue(aboutRepository.getAboutUs());
    }

    /**
     * Returns aboutEmailMutableLiveData
     * @return LiveData<String>
     */
    LiveData<String> getAboutEmail() {
        return aboutEmailMutableLiveData;
    }

    /**
     * Returns aboutEmailMutableLiveData
     * @return LiveData<String>
     */
    LiveData<String> getAboutPhone() {
        return aboutPhoneMutableLiveData;
    }

    /**
     * Returns aboutUsMutableLiveData
     * @return LiveData<String>
     */
    LiveData<String> getAboutUs() {
        return aboutUsMutableLiveData;
    }
}
