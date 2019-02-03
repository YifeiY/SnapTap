package com.yifeiy.qhack;

import android.widget.LinearLayout;

public class Entry {

    public String name;
    public String purchase_date;
    public String expr_date;
    public boolean is_dead;
    public String img_addr;

    public Entry(String [] args){

        name = args[0];
        purchase_date = args[1];
        expr_date = args[2];
        if (Integer.valueOf(args[3]) == 1){
            is_dead = true;
        }else{
            is_dead = false;
        }
        img_addr = args[4];
    }

    @Override
    public String toString() {
        return "Entry{" +
                "name='" + name + '\'' +
                ", purchase_date='" + purchase_date + '\'' +
                ", expr_date='" + expr_date + '\'' +
                ", is_dead=" + is_dead +
                '}';
    }
}
