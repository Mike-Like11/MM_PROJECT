package com.example.myapplication.ui.Articles;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import com.example.myapplication.Models.Article;
import com.example.myapplication.R;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>{
private List<Article>listData;

public ArticleAdapter(List<Article> listData) {
        this.listData = listData;
        }


@Override
public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_article,parent,false);
        return new ViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article ld=listData.get(position);
        holder.txtname.setText(ld.getName());
        holder.txtavtor.setText("Автор: "+ld.getName_narrator());
        holder.txtmovie.setText(ld.getDescription());

        }

@Override
public int getItemCount() {
        return listData.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder{
    private TextView txtavtor,txtname,txtmovie;
    public ViewHolder(View itemView) {
        super(itemView);
        txtname=(TextView)itemView.findViewById(R.id.nametxt);
        txtavtor=(TextView)itemView.findViewById(R.id.avtortxt);
        txtmovie=(TextView)itemView.findViewById(R.id.suttxt);
    }
}
}