package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.myapplication.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
Button sign,register;
FirebaseAuth auth;
FirebaseDatabase db;
DatabaseReference users;
RelativeLayout root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sign=findViewById(R.id.button2);
        register=findViewById(R.id.button);
        auth=FirebaseAuth.getInstance();
        db=FirebaseDatabase.getInstance();
        users=db.getReference();
        root=findViewById(R.id.root_element);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterWindow();
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignWindow();
            }
        });
}
private void showSignWindow(){
    AlertDialog.Builder dialog=new AlertDialog.Builder(this);
    dialog.setTitle("Войти");
    dialog.setMessage("Введите данные для входа ");
    LayoutInflater inflater=LayoutInflater.from(this);
    View sign_window=inflater.inflate(R.layout.sign_window,null);
    dialog.setView(sign_window);
    final MaterialEditText email=sign_window.findViewById(R.id.emailField);

    final MaterialEditText pass=sign_window.findViewById(R.id.parolField);
    dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface , int which) {
            dialogInterface.dismiss();
        }
    });
    dialog.setPositiveButton("Войти", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface , int which) {
            if(TextUtils.isEmpty(email.getText().toString())){
                Snackbar.make(root,"Введите вашу почту",Snackbar.LENGTH_SHORT).show();
                return;

            }

            if(pass.getText().toString().length()<5){
                Snackbar.make(root,"Введите ваш пароль(более 5 символов)",Snackbar.LENGTH_SHORT).show();
                return;
            }
            auth.signInWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            startActivity(new Intent(MainActivity.this, MapActivity.class));
                            finish();

                            }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(root,"Ошибка авторизации"+e.getMessage(),Snackbar.LENGTH_SHORT).show();
                }
            });

        }
    });
    dialog.show();
}
private void showRegisterWindow(){
    AlertDialog.Builder dialog=new AlertDialog.Builder(this);
    dialog.setTitle("Зарегистрироваться");
    dialog.setMessage("Введите все ваши данные для регистрации");
    LayoutInflater inflater=LayoutInflater.from(this);
    View register_window=inflater.inflate(R.layout.register_window,null);
    dialog.setView(register_window);
   final MaterialEditText email=register_window.findViewById(R.id.emailField);
   final MaterialEditText name=register_window.findViewById(R.id.nameField);
   final MaterialEditText phone=register_window.findViewById(R.id.telephoneField);
   final MaterialEditText pass=register_window.findViewById(R.id.parolField);
  dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface , int which) {
            dialogInterface.dismiss();
        }
    });
    dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface , int which) {
            if(TextUtils.isEmpty(email.getText().toString())){
                Snackbar.make(root,"Введите вашу почту",Snackbar.LENGTH_SHORT).show();
                return;

            }
            if(TextUtils.isEmpty(name.getText().toString())){
                Snackbar.make(root,"Введите ваше имя",Snackbar.LENGTH_SHORT).show();
                return;

            }
            if(TextUtils.isEmpty(phone.getText().toString())){
                Snackbar.make(root,"Введите ваш телефон",Snackbar.LENGTH_SHORT).show();
                return;

            }
            if(pass.getText().toString().length()<5){
                Snackbar.make(root,"Введите ваш пароль(более 5 символов)",Snackbar.LENGTH_SHORT).show();
                return;
            }
            auth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            User user=new User();
                            user.setEmail(name.getText().toString());
                            user.setName(name.getText().toString());
                            user.setPhone(phone.getText().toString());
                            user.setPass(pass.getText().toString());
                            users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Snackbar.make(root,"Пользователь добавлен!",Snackbar.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    });



        }
    });
dialog.show();
}
}
