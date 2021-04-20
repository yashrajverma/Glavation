package com.yashraj.glavage.vendor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yashraj.glavage.R;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    List<String> studentModels;
    Context context;

    public StudentAdapter(List<String> studentModels, Context context) {
        this.studentModels = studentModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String model=studentModels.get(position);
        holder.textView.setText(model);

    }

    @Override
    public int getItemCount() {
        return studentModels.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        TextView textView;
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.room_number);
            checkBox=itemView.findViewById(R.id.checkBox);

        }
    }
}
