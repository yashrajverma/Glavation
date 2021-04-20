package com.yashraj.glavage.vendor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.yashraj.glavage.R;
import com.yashraj.glavage.vendor.HostelActivity;
import com.yashraj.glavage.vendor.Models.HostelModel;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static Context context;
    static List<HostelModel> itemList;

    public RecyclerAdapter(Context context, List<HostelModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.hostel_card,parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HostelModel model=itemList.get(position);
        holder.hostel_name.setText(model.getHostel_name());
        for(int i=0;i<=itemList.size();i=i+2){
            if(position%2==0){
                if((i%2)==0){
                    holder.cardView.setVisibility(View.VISIBLE);
                    holder.noti_badge.setText(String.valueOf(position+(i/2)));
                    Log.i("Hostel",String.valueOf(i));
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView hostel_name;
        TextView noti_badge;
        CardView cardView;
        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context=ctx;
            hostel_name=itemView.findViewById(R.id.hostel_name);
            noti_badge=itemView.findViewById(R.id.noti_badge);
            cardView=itemView.findViewById(R.id.hostel_cardView);
                itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position=getAdapterPosition();
                    HostelModel model=itemList.get(position);

                    Intent intent=new Intent(context, HostelActivity.class);
                    intent.putExtra("hostel_name",model.getHostel_name());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            });
        }




    }
}
