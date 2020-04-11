package com.example.myapplication.ui.MyWork;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;

public class MyWorkFragment extends Fragment {

    private MyWorkModel myWorkModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myWorkModel =
                ViewModelProviders.of(this).get(MyWorkModel.class);
        View root = inflater.inflate(R.layout.fragment_my_work, container, false);
        final TextView textView = root.findViewById(R.id.text_my_work);
        myWorkModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}