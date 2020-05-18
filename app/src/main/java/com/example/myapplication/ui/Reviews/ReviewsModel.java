package com.example.myapplication.ui.Reviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReviewsModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ReviewsModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is ReviewsModel fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}