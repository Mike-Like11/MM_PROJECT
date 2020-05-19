package com.example.myapplication.ui.send;

import android.content.Context;
import android.content.DialogInterface;
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

import com.example.myapplication.Models.Reviews;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SendFragment extends Fragment {

    Button infor;

    private Context mContext;
    DatabaseReference services;
    FirebaseDatabase db;

    private SendViewModel sendViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db=FirebaseDatabase.getInstance();
        services=db.getReference();
        sendViewModel = ViewModelProviders.of(this).get(SendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_send, container, false);
        final TextView textView = root.findViewById(R.id.bugsss);
        final ImageView imageView = root.findViewById(R.id.image);
        infor=root.findViewById(R.id.infor);
        infor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infor();
            }
        });
        return root;
    }

    private void infor() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Отзыв о работоспособности");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View answer = inflater.inflate(R.layout.list_bug, null);
        dialog.setView(answer);
        final MaterialEditText ans = answer.findViewById(R.id.answering);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface , int which) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("Отправить сообщение", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface , int which) {

            }
        });
        final AlertDialog dialog1=dialog.create();
        dialog1.show();

        dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(ans.getText().toString())) {
                    ans.setError("Поле сообщения пусто");
                    return;
                }

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String name = user.getDisplayName();
                Reviews r=new Reviews();
                r.setName(name);
                r.setDescription(ans.getText().toString());
                services.child("Bug report").child(r.getDescription()).setValue(r).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Cпасибо, что помогаете нам стать лучше!", Snackbar.LENGTH_SHORT).show();
                        dialog1.dismiss();
                        dialog1.cancel();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Ошибка произошла" , Snackbar.LENGTH_SHORT).show();
                    }


                });
            }
        });
    }
}