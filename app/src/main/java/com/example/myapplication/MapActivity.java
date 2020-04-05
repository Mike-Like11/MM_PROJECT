package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

public class MapActivity extends AppCompatActivity {
    Button request,data;
    Calendar dateAndTime=Calendar.getInstance();
    DatePickerDialog.OnDateSetListener d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        request=findViewById(R.id.button3);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRequestWindow();
            }
        });
    }
    private void showRequestWindow(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("Попросить о помощи");
        dialog.setMessage("Введите все  данные о вашем задании ");
        LayoutInflater inflater=LayoutInflater.from(this);
        View register_window=inflater.inflate(R.layout.request_window,null);
        dialog.setView(register_window);


        dialog.show();
    }

}
