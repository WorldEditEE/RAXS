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

public class ListViewAdapter extends ArrayAdapter<JSONObject> {


    int listLayout;
    ArrayList<JSONObject> ordersList;
    Context context;





    public ListViewAdapter(Context  context, int listLayout, int field, ArrayList<JSONObject> ordersList) {
        super(context, listLayout, field, ordersList);
        this.context = context;
        this.listLayout = listLayout;
        this.ordersList = ordersList;
    }


    @Override
    @SuppressLint("ViewHolder")
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View listViewItem = inflater.inflate(listLayout, null, false);
        TextView order = listViewItem.findViewById(R.id.order);
        TextView name = listViewItem.findViewById(R.id.name);
        TextView count = listViewItem.findViewById(R.id.count);

        try{
            order.setText(ordersList.get(position).getString("o"));
            name.setText(ordersList.get(position).getString("n"));
            count.setText(ordersList.get(position).getString("c"));
        }catch (JSONException je) {
            je.printStackTrace();
        }
        return listViewItem;



    }



}
