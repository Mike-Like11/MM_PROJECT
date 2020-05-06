package com.example.myapplication.ui.MyProfile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.MainActivity;
import com.example.myapplication.MaiwActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

import static android.content.ContentValues.TAG;

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
        //mAuth = FirebaseAuth.getInstance();
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
                exit();
            }
        });
        return root;
    }

    private void exit(){
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
                getActivity().finish();
            }
        });
    }
    private void changePass() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Смена пароля");
        dialog.setMessage("Введите текущий и новый пароли в поля ниже");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View pass_change_window = inflater.inflate(R.layout.pass_change_window, null);
        dialog.setView(pass_change_window);
        final MaterialEditText pass_old = pass_change_window.findViewById(R.id.old_pass);
        final MaterialEditText pass_new = pass_change_window.findViewById(R.id.new_pass);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Сменить пароль", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

            }
        });
        final AlertDialog dialog1 = dialog.create();
        dialog1.show();

        dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(pass_old.getText().toString())) {
                    pass_old.setError("Введите ваш текущий пароль");
                    return;
                }

                final String pass_old1=pass_old.getText().toString();

                if (pass_new.getText().toString().length() < 5) {
                    pass_new.setError("Пароль должен содержать пять и более символов");
                    return;
                }

                final String pass_new1=pass_new.getText().toString();

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String email = user.getEmail();
                AuthCredential credential = EmailAuthProvider.getCredential(email, pass_old1);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(pass_new1);
                            dialog1.dismiss();
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Пароль успешно изменён!", Snackbar.LENGTH_SHORT).show();
                        }
                        else {
                            pass_old.setError("Текущий пароль введён неверно");
                            return;
                        }
                    }
                });
            }
        });
    }

    private void changeAvatar(){

    }


}
