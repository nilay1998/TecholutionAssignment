package com.example.techolutionassignment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.MyViewHolder> {

    List<ToDoItem> todiList;
    ToDoListAdapter(List<ToDoItem> t){
        todiList=t;
    }

    @NonNull
    @Override
    public ToDoListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_todo,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final  ToDoListAdapter.MyViewHolder holder, final int position) {
        holder.editText.setText(todiList.get(position).getTodop());
        holder.checkBox.setChecked(todiList.get(position).isChecked());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                todiList.get(position).setChecked(b);
            }
        });

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todiList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return todiList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        CheckBox checkBox;
        EditText editText;
        Button button;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox=itemView.findViewById(R.id.checkbox);
            editText=itemView.findViewById(R.id.todo_et);
            button=itemView.findViewById(R.id.del_button);
        }
    }
}
