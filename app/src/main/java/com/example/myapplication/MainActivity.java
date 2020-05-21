package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.myapplication.Models.User;
import com.example.myapplication.Models.User_name;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
Button sign,register;
FirebaseAuth auth;
FirebaseDatabase db;
DatabaseReference users;
RelativeLayout root;
DatabaseReference services;
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
    final AlertDialog.Builder dialog=new AlertDialog.Builder(this);
    dialog.setTitle("Войти");
    dialog.setMessage("Введите данные для входа ");
    dialog.setPositiveButton("Войти",null);
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

    }
});
    final AlertDialog dialog1=dialog.create();
    dialog1.show();
    dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(TextUtils.isEmpty(email.getText().toString())){
                email.setError("Введите вашу почту");

                return;

            }

            if(pass.getText().toString().length()<5){
                pass.setError("Неправильный пароль");
                return;
            }
            auth.signInWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            dialog1.dismiss();
                            startActivity(new Intent(MainActivity.this, MaiwActivity.class));
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    email.setError("Ошибка авторизации"+"\n"+"Возможно, вы неверно ввели данные");
                }
            });

        }
    });
}
private void showRegisterWindow(){
    final AlertDialog.Builder dialog=new AlertDialog.Builder(this);
    dialog.setTitle("Зарегистрироваться");
    dialog.setPositiveButton("Добавить",null);
    dialog.setNegativeButton("отменить",null);
    dialog.setMessage("Введите все ваши данные для регистрации");
    LayoutInflater inflater=LayoutInflater.from(this);
    View register_window=inflater.inflate(R.layout.register_window,null);
    dialog.setView(register_window);
dialog.setCancelable(false);
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
        public void onClick(final DialogInterface dialogInterface , int which) {


            }
    });

final AlertDialog dialog1=dialog.create();
dialog1.show();
dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        if(TextUtils.isEmpty(email.getText().toString())){
            email.setError("Введите вашу почту");



        }
        else {
            if (TextUtils.isEmpty(name.getText().toString())) {
                name.setError("Введите ваше имя");

                return;

            } else {
                if (TextUtils.isEmpty(phone.getText().toString())) {
                    phone.setError("Введите ваш телефон");

                    return;

                } else {
                    if (pass.getText().toString().length() < 5) {
                        pass.setError("Введите ваш пароль(более 5 символов)");

                        return;
                    } else {

                        users.addValueEventListener(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                boolean f=false;
                                String s="";
                                for (DataSnapshot childr : dataSnapshot.child("Users").child("User_name").getChildren()) {
                                    User_name un=childr.getValue(User_name.class);
                                    if (un != null && name.getText().toString().equals(un.getName())) {
                                        f = true;
                                    }
                                }

                                if(f==false) {

                                    auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                @Override
                                                public void onSuccess(AuthResult authResult) {
                                                    User user1 = new User();
                                                    user1.setEmail(email.getText().toString());
                                                    user1.setName(name.getText().toString());
                                                    user1.setPhone(phone.getText().toString());
                                                    user1.setPass(pass.getText().toString());
                                                    User_name un = new User_name();
                                                    un.setName(user1.getName());
                                                    users.child("Users").child("User_name").child(user1.getName()).setValue(un);
                                                    users.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user1)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name.getText().toString()).build();
                                                                    user.updateProfile(profileUpdates);
                                                                    Snackbar.make(root, "Пользователь " + profileUpdates.getDisplayName() + " добавлен!", Snackbar.LENGTH_SHORT).show();
                                                                    dialog1.dismiss();
                                                                }
                                                            });

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            email.setError("Ошибка! Возможно данная почта уже используется");
                                        }
                                    });
                                }
                                else {
                                    name.setError("Данное имя уже занято");
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });

                    }
                }
            }
        }
    }
});
}
}
