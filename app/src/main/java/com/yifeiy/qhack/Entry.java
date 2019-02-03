package com.yifeiy.qhack;

import android.widget.LinearLayout;

public class Entry {

    public String name;
    public String purchase_date;
    public String img_addr;
    public String urc;
    public String price;

    public Entry(String [] args){
        name = args[0];
        purchase_date = args[1];
        img_addr = args[2];
        urc = args[3];
        price = args[4];
    }


}
