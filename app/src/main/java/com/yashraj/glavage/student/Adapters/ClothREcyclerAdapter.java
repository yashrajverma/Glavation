package com.yashraj.glavage.student.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.yashraj.glavage.R;
import com.yashraj.glavage.student.Model;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import static java.lang.Long.parseLong;

public class ClothREcyclerAdapter extends RecyclerView.Adapter<ClothREcyclerAdapter.ViewHolder> {
    Context context;
    List<Model> modelList;

    public ClothREcyclerAdapter(Context context, List<Model> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_image,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Model model=modelList.get(position);
        holder.cloth_type.setText(model.getCloth_type());
        DateFormat dateFormat=DateFormat.getDateInstance();
        String formattedate=dateFormat.format(new Date(parseLong(model.getDateadded())).getTime());
        holder.dataAdded.setText(formattedate);
        String image_url=model.getImage();
        Picasso.get().load(image_url).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dataAdded,cloth_type;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataAdded=itemView.findViewById(R.id.date_added_textView);
            cloth_type=itemView.findViewById(R.id.cloth_type_textView);
            image=itemView.findViewById(R.id.card_cloth_image);
        }
    }
}
