package com.example.RAXS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListViewAdapter_order extends ArrayAdapter<JSONObject> {


    int listLayout1;
    ArrayList<JSONObject> ordersList1;
    Context context1;


    public ListViewAdapter_order(Context  context1, int listLayout1, int field, ArrayList<JSONObject> ordersList1) {
        super(context1, listLayout1, field, ordersList1);
        this.context1 = context1;
        this.listLayout1 = listLayout1;
        this.ordersList1 = ordersList1;
    }


    @Override
    @SuppressLint("ViewHolder")
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listViewItem = inflater.inflate(listLayout1, null, false);
        TextView product = listViewItem.findViewById(R.id.product);
        TextView count = listViewItem.findViewById(R.id.count);

        try{
            product.setText(ordersList1.get(position).getString("o"));
            count.setText(ordersList1.get(position).getString("c"));
        }catch (JSONException je) {
            je.printStackTrace();
        }
        return listViewItem;



    }



}
