package com.example.RAXS;

import static android.view.inputmethod.EditorInfo.IME_ACTION_UNSPECIFIED;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    public EditText code;
    private Button button;
    private TextView logo;
    private TextView result_info;
    private Context context;
    public String device_id;
    public String bc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        code = findViewById(R.id.code);
        //button = findViewById(R.id.button);
        logo = findViewById(R.id.logo);
        result_info = findViewById(R.id.result_info);






        code.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @SuppressLint("HardwareIds")
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == IME_ACTION_UNSPECIFIED) { //actionId 4 for actionDone And 6 for actionSend


                            if (code.getText().toString().trim().equals(""))
                                result_info.setText(R.string.error_input);
                            else {
                                 bc = code.getText().toString();
                                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                                 device_id = "";
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    device_id = getDeviceId(getApplicationContext());
                                }
                                else{
                                    device_id = tm.getDeviceId();
                                }

                                String url = "https://shop.polaroid.su/api/pdct/auth.php?meid=" + device_id + "&bc=" + bc;

                                new GetUrlData().execute(url);


                            }



                    return true;
                } else
                    return false;
            }
        });





    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {

        String deviceId;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }

        return deviceId;
    }

    private class GetUrlData extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        result_info.setText("Проверка данных..");
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





        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
           try {
           JSONObject jsonObject = new JSONObject(result);
               String answer;
               answer = jsonObject.getString("answer");
           result_info.setText("Успешно");
               if (answer.equalsIgnoreCase("okay")) {
                   Intent intent = new Intent(MainActivity.this, Menu.class);
                   intent.putExtra("bc", bc);
                   intent.putExtra("device_id", device_id);
                   startActivity(intent);
               }
               else {
                   code.setText("");
          result_info.setText("Ошибка, отсканируйте верный код");
               }
         } catch (JSONException e) {
               e.printStackTrace();
         }

        }
    }
}