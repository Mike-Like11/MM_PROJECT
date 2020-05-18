package com.example.myapplication.ui.MyTask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Review;
import com.example.myapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>{
    private List<Review> listData;
    private Context context;
    FirebaseDatabase db;
    DatabaseReference services;

    public ProfileAdapter(List<Review> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_profile,parent,false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull final ProfileAdapter.ViewHolder holder, final int position) {
        final Review ld = listData.get(position);
        if(ld!=null &&  ld.getDescription()!=null) {
            holder.d.setText(ld.getDescription());
            holder.rbp.setRating(ld.getRating() / 5);
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView d;
        RatingBar rbp;
        FirebaseDatabase db;
        DatabaseReference services;

        public ViewHolder(View itemView) {
            super(itemView);
            d= (TextView) itemView.findViewById(R.id.desk_vip);
            rbp= (RatingBar) itemView.findViewById(R.id.stars_number_profile);

        }
    }
}


