package com.example.techolutionassignment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class Tab1 extends Fragment {

    RecyclerView recyclerView;
    EditText editText_addITem;
    Button button_add_item;
    ViewGroup rootview;
    List<ToDoItem> todoList=new ArrayList<>();
    ToDoListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview= (ViewGroup)inflater.inflate(R.layout.fragment_tab1, container, false);

        initViews();

        button_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s=editText_addITem.getText().toString().trim();
                if(s!=null && s.length()!=0 && !s.isEmpty())
                {
                    todoList.add(new ToDoItem(false,s));
                    adapter.notifyDataSetChanged();
                }

                editText_addITem.setText("");
                closekeyboard();
            }
        });

        return rootview;
    }

    @Override
    public void onStop() {
        Log.e(TAG, "onStop: ");
        saveData();
        super.onStop();
    }


    private void saveData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(todoList);
        editor.putString("task list", json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<List<ToDoItem>>() {}.getType();
        todoList = gson.fromJson(json, type);
        if (todoList == null) {
            todoList = new ArrayList<>();
        }
    }

    private void initViews(){
        recyclerView=rootview.findViewById(R.id.todoRecyclerView);
        adapter=new ToDoListAdapter(todoList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        editText_addITem=rootview.findViewById(R.id.addItem_et);
        button_add_item=rootview.findViewById(R.id.addItem_button);
    }

    void closekeyboard()
    {
        View view=getActivity().getCurrentFocus();
        if(view!=null){
            InputMethodManager imm= (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

}
