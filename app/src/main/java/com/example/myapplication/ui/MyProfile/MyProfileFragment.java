package com.example.myapplication.ui.MyProfile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;

public class MyProfileFragment extends Fragment{

    private MyProfileModel myProfileModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myProfileModel = ViewModelProviders.of(this).get(MyProfileModel.class);
        View root = inflater.inflate(R.layout.fragment_my_profile, container, false);
        final TextView textView = root.findViewById(R.id.textView_name_user);
        final ImageView imageView = root.findViewById(R.id.imageView);
        return root;
    }
}
