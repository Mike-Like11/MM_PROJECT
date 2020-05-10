package com.example.myapplication.ui.MyTask;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
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

import com.daasuu.bl.ArrowDirection;
import com.example.myapplication.Models.Article;
import com.example.myapplication.Models.Like;
import com.example.myapplication.Models.Message;
import com.example.myapplication.R;
import com.github.library.bubbleview.BubbleTextView;
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

import android.text.format.DateFormat;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MyTaskCommentAdapter extends RecyclerView.Adapter<MyTaskCommentAdapter.ViewHolder>{
    private List<Message> MyTaskCommentData;

    private  boolean buto=false;
    private Context context;
    public MyTaskCommentAdapter(List<Message> listData, Context context) {
        this.MyTaskCommentData = listData;
        this.context =context;

    }


    @Override
    public MyTaskCommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment_my_task,parent,false);
        return new MyTaskCommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Message ld =MyTaskCommentData.get(position);
         if(!ld.getName().equals("")) {
            holder.comment_name.setText(ld.getText());
             final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
             // Name, email address, and profile photo Url
             final String name = user.getDisplayName();
             final String email = user.getEmail();
            if(!ld.getName().equals(name)) {
                holder.comment_name.setTextColor(Color.RED);
            }
            else{
                holder.comment_name.setTextColor(Color.WHITE);
            }
            holder.message_name.setText(ld.getName());
            holder.message_time.setText(DateFormat.format("dd-mm-yy HH:mm:ss",ld.getTime()));
        }

    }
    @Override
    public int getItemCount() {
        return MyTaskCommentData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private BubbleTextView comment_name;
        private TextView message_name,message_time;

        FirebaseDatabase db;
        DatabaseReference services;
        public ViewHolder(View itemView) {
            super(itemView);
            comment_name=(BubbleTextView) itemView.findViewById(R.id.comments_my_task);
            message_name=(TextView)itemView.findViewById(R.id.message_name);
            message_time=(TextView)itemView.findViewById(R.id.message_time);


        }
    }
}
