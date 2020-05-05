package com.example.myapplication.ui.MyProfile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.MainActivity;
import com.example.myapplication.MaiwActivity;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyProfileFragment extends Fragment{

    Button pass,photo,exit;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private MyProfileModel myProfileModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        myProfileModel = ViewModelProviders.of(this).get(MyProfileModel.class);
        View root = inflater.inflate(R.layout.fragment_my_profile, container, false);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String name = user.getDisplayName();
        TextView name_user=(TextView)root.findViewById(R.id.name_user);
        name_user.setText(name);
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final TextView textView = root.findViewById(R.id.name_user);
        final ImageView imageView = root.findViewById(R.id.imageView);
        pass=root.findViewById(R.id.pass);
        photo=root.findViewById(R.id.photo);
        exit=root.findViewById(R.id.exit);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePass();
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAvatar();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit(name);
            }
        });
        return root;
    }

    private void exit(String user){
        final AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
        dialog.setTitle("Выход из учётной записи");
        dialog.setMessage("Вы уверены, что хотите выйти из аккаунта?");

        dialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface , int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface , int which) {

            }
        });
        final AlertDialog dialog1=dialog.create();
        dialog1.show();

        dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),MainActivity.class);
                startActivity(intent);
                FirebaseAuth.getInstance().signOut();
            }
        });
    }
    private void changePass(){

    }
    private void changeAvatar(){

    }

    /*private void signOut() {
        mAuth.signOut();
    }*/
}




