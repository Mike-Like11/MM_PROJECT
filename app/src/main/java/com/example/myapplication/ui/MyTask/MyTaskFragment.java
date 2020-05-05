package com.example.myapplication.ui.MyTask;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.MaiwActivity;
import com.example.myapplication.MapActivity;
import com.example.myapplication.Models.Request;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MyTaskFragment extends Fragment {
    private Context mContext;
    FloatingActionButton fb;
    FirebaseDatabase db;
    DatabaseReference services;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_my_task, container, false);
        Intent intent=new Intent(getContext(),MapActivity.class);
        mContext=getActivity();
        fb=(FloatingActionButton) root.findViewById(R.id.fab_my_task);
        db=FirebaseDatabase.getInstance();
        services=db.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Name, email address, and profile photo Url
        String name = user.getDisplayName();
        String email = user.getEmail();
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRequestWindow();
            }
        });


        return root;
    }
    private void showRequestWindow(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);

        dialog.setTitle("Ваша Просьба");
        dialog.setMessage("Введите все данные о вашем задании ");
        LayoutInflater inflater=LayoutInflater.from(mContext);
        View request_window=inflater.inflate(R.layout.request_window,null);
        dialog.setView(request_window);
        final MaterialEditText task=request_window.findViewById(R.id.taskField);
        final MaterialEditText description=request_window.findViewById(R.id.descriptionField);
        final  MaterialEditText address=request_window.findViewById(R.id.addressField);
        final EditText data=request_window.findViewById(R.id.dataField);
        dialog.setNegativeButton("Отменить", null);
        dialog.setPositiveButton("Добавить", null);
        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface , int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


            }

        });
        final AlertDialog dialog1=dialog.create();
        dialog1.show();
        dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(task.getText().toString())) {
                    task.setError("Введите ваше задание") ;
                    return;
                }
                if (TextUtils.isEmpty(description.getText().toString())) {
                    description.setError("Введите ваше описание") ;
                    return;
                }
                if (TextUtils.isEmpty(address.getText().toString())) {
                    address.setError("Введите адрес задания") ;
                    return;
                }
                if (TextUtils.isEmpty(data.getText().toString())) {
                    data.setError("Введите дату задания") ;
                    return;
                }
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Name, email address, and profile photo Url
                String name = user.getDisplayName();
                String email = user.getEmail();
                Request r=new Request();
                r.setName_1(name);
                r.setName_2("No");
                r.setTask(task.getText().toString());
                r.setDescription(description.getText().toString());
                r.setAddress(address.getText().toString());
                r.setData(data.getText().toString());
                services.child("Requests").child(r.getTask()).child("Task").setValue(r).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        //Snackbar.make(root,"Просьба добавлена!",Snackbar.LENGTH_SHORT).show();
                        dialog1.dismiss();
                        dialog1.cancel();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Ошибка произошла " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }


                });


            }
        });
    }
}