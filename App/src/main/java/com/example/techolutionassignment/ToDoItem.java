package com.example.techolutionassignment;

public class ToDoItem {
    boolean isChecked;
    String todop;

    ToDoItem(boolean i,String s)
    {
        isChecked=i;
        todop=s;
    }
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getTodop() {
        return todop;
    }

    public void setTodop(String todop) {
        this.todop = todop;
    }
}
