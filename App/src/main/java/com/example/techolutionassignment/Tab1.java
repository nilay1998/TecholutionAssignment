package com.example.techolutionassignment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class Tab1 extends Fragment {

    RecyclerView recyclerView;
    EditText editText_addITem;
    Button button_add_item;
    ViewGroup rootview;
    List<ToDoItem> todoList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview= (ViewGroup)inflater.inflate(R.layout.fragment_tab1, container, false);

        recyclerView=rootview.findViewById(R.id.todoRecyclerView);
        final ToDoListAdapter adapter=new ToDoListAdapter(todoList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        editText_addITem=rootview.findViewById(R.id.addItem_et);
        button_add_item=rootview.findViewById(R.id.addItem_button);

        button_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s=editText_addITem.getText().toString().trim();
                if(s!=null||s.length()!=0)
                {
                    todoList.add(new ToDoItem(false,s));
                    adapter.notifyDataSetChanged();
                }
            }
        });

        return rootview;
    }

}
