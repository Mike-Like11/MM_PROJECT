package com.example.myapplication.ui.Articles;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Article;
import com.example.myapplication.Models.Like;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    private List<String> CommentData;
    private boolean liki;
    private boolean likos;
    private  boolean buto;
    private  String s;
    private Context context;
    FirebaseDatabase db;
    DatabaseReference services;
    public CommentAdapter(List<String> listData,Context context) {
        this.CommentData = listData;
        this.context =context;
    }


    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment,parent,false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String ld =CommentData.get(position);
        if(!ld.equals("")) {
            holder.comment_name.setText(ld);

        }

    }







    @Override
    public int getItemCount() {
        return CommentData.size();
    }

public class ViewHolder extends RecyclerView.ViewHolder{
    private TextView comment_name;
    private EditText myTV;
    private AppCompatButton btn_d,btn_r;
    private AppCompatButton btn_c;
    private ViewSwitcher switcher;
    private LikeButton lb;
    private String s;

    FirebaseDatabase db;
    DatabaseReference services;
    public ViewHolder(View itemView) {
        super(itemView);
        comment_name=(TextView)itemView.findViewById(R.id.comments2);


    }
}
}
