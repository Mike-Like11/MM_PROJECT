package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.Models.Request;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

import static java.lang.Math.random;

public class MapActivity extends AppCompatActivity {
    Button request,data;
    Calendar dateAndTime=Calendar.getInstance();
    DatePickerDialog.OnDateSetListener d;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference services;
    RelativeLayout root;
    TextView tw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        request=findViewById(R.id.button3);
        auth=FirebaseAuth.getInstance();
        db=FirebaseDatabase.getInstance();
        services=db.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Name, email address, and profile photo Url
        String name = user.getDisplayName();
        String email = user.getEmail();
        tw=findViewById(R.id.text_up);
        tw.setText("Ваши просьбы");
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRequestWindow();
            }
        });
    }
    private void showRequestWindow(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);

       dialog.setTitle("Ваша Просьба");
        dialog.setMessage("Введите все данные о вашем задании ");
        LayoutInflater inflater=LayoutInflater.from(this);
        View request_window=inflater.inflate(R.layout.request_window,null);
        dialog.setView(request_window);
        final MaterialEditText task=request_window.findViewById(R.id.taskField);
        final MaterialEditText description=request_window.findViewById(R.id.descriptionField);
        final  MaterialEditText address=request_window.findViewById(R.id.addressField);
        final EditText data=request_window.findViewById(R.id.dataField);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface , int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        if (TextUtils.isEmpty(task.getText().toString())) {
                            Snackbar.make(root, "Введите ваше задание", Snackbar.LENGTH_SHORT).show();
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
                        services.child("Requests").setValue(r).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                //Snackbar.make(root,"Просьба добавлена!",Snackbar.LENGTH_SHORT).show();
                                startActivity(new Intent(MapActivity.this, MaiwActivity.class));
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(root, "Ошибка авторизации" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }

                            ;
                        });


                    }

              });







        dialog.show();
    }

}
