package com.example.RAXS;

import static android.view.inputmethod.EditorInfo.IME_ACTION_UNSPECIFIED;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.RAXS.adapter.OrderAdapter;

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

public class Order extends AppCompatActivity {

    public Button apply;
    public Button exit_order;
    public EditText count_text;
    public EditText product_code;
    RecyclerView listView_order;
    public String device_id;
    public String bc;
   // public String answer;
    public String order_number;
    public String productstring;
    public String countstring;

    ArrayList<OrderEntity> listItemsOrder;
 //   ArrayAdapter<String> adapter1;
    private OrderAdapter orderAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        count_text = findViewById(R.id.count_text);
        product_code = findViewById(R.id.product_code);
        exit_order = findViewById(R.id.exit_order);
        apply = findViewById(R.id.apply);
       // listView_order = (ListView) findViewById(R.id.listView_order);
        Bundle arguments = getIntent().getExtras();
        bc = arguments.get("bc").toString();
        device_id = arguments.get("device_id").toString();
        order_number = arguments.get("order_number").toString();
        listView_order = findViewById(R.id.listView_order);


        listItemsOrder = new ArrayList<>();
      //  adapter1 = new ArrayAdapter<String>(getApplicationContext(),R.layout.row_order,R.id.count, listItemsOrder);
       // listView_order.setAdapter(adapter1);

        initRecyclerView();
        product_code.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @SuppressLint({"HardwareIds", "SetTextI18n"})
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == IME_ACTION_UNSPECIFIED) { //actionId 4 for actionDone And 6 for actionSend
                     productstring = product_code.getText().toString();
                     countstring = count_text.getText().toString();
                    String url = "https://shop.polaroid.su/api/pdct/supply_product_add.php?meid=" + device_id + "&user=" + bc + "&operation=" + order_number + "&bc=" + productstring + "&count" + countstring;

                    new Order.GetUrlData().execute(url);
                    product_code.setText("");


                    return true;
                } else
                    return false;

            }
        });


    }

    private void initRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listView_order.setLayoutManager(linearLayoutManager);
        orderAdapter = new OrderAdapter(listItemsOrder);
        listView_order.setAdapter(orderAdapter);
    }

    private class GetUrlData extends AsyncTask<String, String, String> {

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


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String answer = jsonObject.getString("n");
                int count = 0;
                for (int i = 0; i < answer.length(); i++) {
                    count++;
                }
                if (count >= 15)
                    product_code.setTextSize(9);
                else {
                    product_code.setTextSize(12);
                }
                OrderEntity entity = new OrderEntity(answer, Integer.parseInt(countstring));
                listItemsOrder.add(entity);
                orderAdapter.notifyDataSetChanged();

//                product_code.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                    @SuppressLint({"HardwareIds", "SetTextI18n"})
//                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                        if (actionId == IME_ACTION_UNSPECIFIED) { //actionId 4 for actionDone And 6 for actionSend
//                            //product_code.setText(answer + "ssss");
//                            int count = 0;
//                            for (int i = 0; i < answer.length(); i++) {
//                                count++;
//                            }
//                            if (count >= 15)
//                                product_code.setTextSize(9);
//                            else {
//                                product_code.setTextSize(12);
//                            }
//                            return true;
//                        } else
//                            return false;
//
//                    }
//                });



                exit_order.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Order.this, Menu1.class);
                        intent.putExtra("bc", bc);
                        intent.putExtra("device_id", device_id);
                        intent.putExtra("order_number", order_number);
                        startActivity(intent);
                    }

                });
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }



    }





        }





