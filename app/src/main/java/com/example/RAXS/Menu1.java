package com.example.RAXS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Menu1 extends AppCompatActivity {

    ListView listView;
    public String bc;
    public String device_id;
    public String JSON_URL;
    public String order_number;
    public Button exit;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu1);
        exit = findViewById(R.id.exit);
        Bundle arguments = getIntent().getExtras();
        bc = arguments.get("bc").toString();
        device_id = arguments.get("device_id").toString();
        JSON_URL = "https://shop.polaroid.su/api/pdct/supply_list.php?meid=" + device_id + "&bc=" + bc;
        listView = (ListView) findViewById(R.id.list);
        loadJSONFromURL(JSON_URL);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu1.this, Menu.class);
                intent.putExtra("bc", bc);
                intent.putExtra("device_id", device_id);
                intent.putExtra("order_number", order_number);
                startActivity(intent);
            }

        });

    }





    public void loadJSONFromURL(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
        new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);    //new JSONObject(EncodingToUTF8(response0));
                    JSONArray jsonArray = object.getJSONArray("supply_list");
                    ArrayList<JSONObject> listItems = getArrayListFromJSONArray(jsonArray);
                    ListAdapter adapter = new ListViewAdapter(getApplicationContext(),R.layout.row,R.id.order,listItems);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
                         public void onItemClick (AdapterView< ? > adapter, View view, int position, long arg){
                       // TODO Auto-generated method stub
                TextView v = (TextView) view.findViewById(R.id.order);
                          order_number = (String) v.getText();

                new Menu1.GetUrlData().execute(JSON_URL);

                Intent intent = new Intent(Menu1.this, Order.class);
                intent.putExtra("bc", bc);
                intent.putExtra("device_id", device_id);
                intent.putExtra("order_number", order_number);
                startActivity(intent);


   }


   }
                    );
            }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private ArrayList<JSONObject> getArrayListFromJSONArray(JSONArray jsonArray){
        ArrayList<JSONObject> aList = new ArrayList<JSONObject>();
        try {
            if (jsonArray!=null){
                for (int i=0; i<jsonArray.length();i++){
                    aList.add(jsonArray.getJSONObject(i));

                }
            }
        } catch (JSONException js) {
            js.printStackTrace();
        }
        return aList;
    }


    public class GetUrlData extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                line = reader.readLine();
                buffer.append(line).append("\n");
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
}}