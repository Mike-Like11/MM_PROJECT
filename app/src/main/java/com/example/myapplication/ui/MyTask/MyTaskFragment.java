package com.example.myapplication.ui.MyTask;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MaiwActivity;
import com.example.myapplication.MapActivity;
import com.example.myapplication.Models.Request;
import com.example.myapplication.R;
import com.example.myapplication.ui.FreeTask.FreeTaskAdapter;
import com.example.myapplication.ui.FreeTask.FreeTaskModel;
import com.example.myapplication.ui.MyTask.MyTaskModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

public class MyTaskFragment extends Fragment {
    private MyTaskModel myTaskModel;
    private RecyclerView rv;
    private Context mContext;
    FloatingActionButton fb;
    private List<Request> listData;
    FirebaseDatabase db;
    DatabaseReference services;
    private MyTaskAdapter adapter;
    DatePickerDialog picker;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myTaskModel = ViewModelProviders.of(this).get(MyTaskModel.class);
        View root = inflater.inflate(R.layout.fragment_my_task, container, false);
        Intent intent=new Intent(getContext(),MapActivity.class);
        mContext=getActivity();
        fb=(FloatingActionButton) root.findViewById(R.id.fab_my_task);
        db=FirebaseDatabase.getInstance();
        services=db.getReference();
        rv=(RecyclerView)root.findViewById(R.id.recycler_view);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        listData=new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String name = user.getDisplayName();

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRequestWindow();
            }
        });

        services.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    listData.clear();
                    int i=0;
                    for (DataSnapshot npsnapshot : dataSnapshot.child("Requests").getChildren()) {
                        Request l = npsnapshot.child("Task").getValue(Request.class);
                        assert l != null;
                        if (name.equals(l.getName_1())){
                            if (!l.getName_2().equals("") && !l.getTask().equals("") && !l.getTask().equals("В данный момент у вас нет выполняемых заданий") && !l.getTask().equals("У вас пока что нет активных просьб")) {
                                i++;
                                listData.add(l);
                            }
                        }
                    }
                    if (i==0){
                        Request r=new Request();
                        r.setName_1(name);
                        r.setName_2("No");
                        r.setEmail_2("No");
                        r.setStatus("Исполнитель не найден");
                        r.setTask("У вас пока что нет активных просьб");
                        r.setDescription("Исполнитель не найден");
                        r.setAddress(" Исполнитель не найден");
                        r.setData("Исполнитель не найден");
                        services.child("Requests").child(r.getTask()).child("Task").setValue(r);
                        listData.add(r);
                    }
                    adapter=new MyTaskAdapter(listData,mContext);
                    rv.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                data.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }

        });
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
                r.setEmail_2("No");
                r.setStatus(" Исполнитель не найден");
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