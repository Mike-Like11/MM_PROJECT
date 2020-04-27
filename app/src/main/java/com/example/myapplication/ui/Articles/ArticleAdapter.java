package com.example.myapplication.ui.Articles;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.example.myapplication.Models.Article;
import com.example.myapplication.Models.Like;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>{
private List<Article>listData;
private Context context;
    FirebaseDatabase db;
    DatabaseReference services;
public ArticleAdapter(List<Article> listData,Context context) {
        this.listData = listData;
        this.context =context;
        }


@Override
public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_article,parent,false);
        return new ViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Article ld=listData.get(position);
        if(!ld.getName_narrator().equals("") &&!ld.getName().equals("") &&!ld.getDescription().equals("")) {
            holder.txtname.setText(ld.getName());
            holder.txtmovie.setText("Автор: " + ld.getName_narrator());
            holder.myTV.setText(ld.getDescription());
        }
            holder.nl.setText(String.valueOf(ld.getLike()));

    holder.myTV.setEnabled(false);
    db= FirebaseDatabase.getInstance();
    services=db.getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    // Name, email address, and profile photo Url
    final String name = user.getDisplayName();
    final String email = user.getEmail();
    if(ld.isLike_or_not()) {
    holder.lb.setLiked(true);
    }
    else{
        holder.lb.setLiked(false);
    }

    holder.lb.setOnLikeListener(new OnLikeListener() {
        @Override
        public void liked(LikeButton likeButton) {
           ld.setLike(ld.getLike()+1);
            holder.nl.setText(String.valueOf(ld.getLike()));
            services.child("Articles").child(ld.getName().toString()).child("Likes").push().setValue(email);
            holder.lb.setLiked(true);
            ld.setLike_or_not(true);

            services.child("Articles").child(ld.getName()).child("Article").child("like").setValue(ld.getLike());
            services.child("Articles").child(ld.getName()).child("Article").child("like_or_not").setValue(ld.isLike_or_not());
        }

        @Override
        public void unLiked(LikeButton likeButton) {
            ld.setLike(ld.getLike()-1);
            holder.nl.setText(String.valueOf(ld.getLike()));
            ld.setLike_or_not(false);
            services.child("Articles").child(ld.getName()).child("Article").child("like").setValue(ld.getLike());
            services.child("Articles").child(ld.getName()).child("Article").child("like_or_not").setValue(ld.isLike_or_not());
            holder.lb.setLiked(false);


            services.child("Articles").child(ld.getName().toString()).child("Likes").removeEventListener(new ValueEventListener(){

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds:dataSnapshot.getChildren()){

                        for(DataSnapshot ds1:ds.child("Likes").getChildren()) {
                            String like = (String) ds1.getValue();
                            if (Objects.equals(like, email)) {
                               ds.getRef().removeValue();
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            holder.lb.setLiked(false);
        }
    });


        if(ld.user_s(ld.getName_narrator(),name)){
            holder.btn_d.setVisibility(View.VISIBLE);
            holder.btn_r.setVisibility(View.VISIBLE);
            holder.btn_r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View View) {
                    //showArticle_Rewrite_Window(ld);
                     //or switcher.showPrevious();
                    holder.myTV.setEnabled(true);
                    holder.btn_d.setText("Отменить изменения");
                    holder.btn_r.setText("Сохранить изменения");
                    holder.btn_d.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(android.view.View v) {
                            holder.myTV.setEnabled(false);
                            holder.myTV.setText(ld.getDescription());
                            holder.btn_d.setText("Удалить");
                            holder.btn_r.setText("Поредачить");

                        }
                    });
                    holder.btn_r.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(android.view.View v) {
                            /*

                            Map map = new HashMap<>();
                            map.put("name",ld.getName());
                            map.put("name_narrator", ld.getName_narrator());
                            map.put("description",holder.myTV.getText().toString());
                            ld.setDescription(holder.myTV.getText().toString());



                            services.child("Articles").child(ld.getName()).updateChildren(map);
                            holder.myTV.setText(ld.getDescription());
                            */

                            services.child("Articles").child(ld.getName()).removeValue();
                            int position=holder.getAdapterPosition();
                            removeItem(position);
                            ld.setDescription(holder.myTV.getText().toString());
                            services.child("Articles").child(ld.getName()).child("Article").setValue(ld);
                            final Like l=new Like();
                            l.setAmail("aaaa");
                            services.child("Articles").child(ld.getName()).child("Likes").setValue(l);
                            holder.myTV.setEnabled(false);
                            holder.btn_d.setText("Удалить");
                            holder.btn_r.setText("Поредачить");

                        }
                    });


                }
            });
            holder.btn_d.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    services.child("Articles").child(ld.getName()).removeValue();
                    ld.setName("");
                    ld.setDescription("");
                    ld.setName_narrator("");
                    holder.txtname.setText(ld.getName());
                    int position=holder.getAdapterPosition();
                    removeItem(position);

                }
            });

        }
        else{
            holder.btn_d.setVisibility(View.INVISIBLE);
            holder.btn_r.setVisibility(View.INVISIBLE);
        }


        }

    public void removeItem(int position) {
        listData.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }


        @Override
public int getItemCount() {
        return listData.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder{
    private TextView txtavtor,txtname,txtmovie,nl;
    private EditText myTV;
    private AppCompatButton btn_d,btn_r;
    private   ViewSwitcher switcher;
    private LikeButton lb;
    public ViewHolder(View itemView) {
        super(itemView);
        txtname=(TextView)itemView.findViewById(R.id.nametxt);
        txtmovie=(TextView)itemView.findViewById(R.id.avtortxt);
        btn_d=(AppCompatButton)itemView.findViewById(R.id.btn_a_d);
        btn_r=(AppCompatButton) itemView.findViewById(R.id.btn_a_r);
        myTV = (EditText) itemView.findViewById(R.id.avtor_2txt);
        lb=(LikeButton)itemView.findViewById(R.id.star_button);
        nl=(TextView)itemView.findViewById(R.id.number_liketxt);
    }
}
}