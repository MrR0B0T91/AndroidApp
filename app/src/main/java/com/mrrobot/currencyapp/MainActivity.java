package com.mrrobot.currencyapp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText userField;
    private EditText userFieldRub;
    private Button mainButton;
    private TextView resultInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userField = findViewById(R.id.user_field);
        userFieldRub = findViewById(R.id.user_field_rub);
        mainButton = findViewById(R.id.main_button);
        resultInfo = findViewById(R.id.result_info);

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userField.getText().toString().trim().equals(""))
                    Toast.makeText(MainActivity.this, R.string.no_user_input, Toast.LENGTH_LONG).show();
                else {
                    String url = "https://www.cbr-xml-daily.ru/daily_json.js";

                    new GetUrlData().execute(url);
                }
            }
        });

    }

    private class GetUrlData extends AsyncTask<String,String,String>{

        protected void onPreExecute() {
            super.onPreExecute();
            resultInfo.setText("Ожидайте...");
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

                while ((line = reader.readLine()) != null)
                    buffer.append(line).append("\n");
                return buffer.toString();
            } catch (Exception e) {
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

                String currency = userField.getText().toString();
                Double rub = Double.parseDouble(userFieldRub.getText().toString());

                Double value = jsonObject.getJSONObject("Valute").getJSONObject(currency).getDouble("Value");
                Double resultDouble = rub * value;

                resultInfo.setText("Сумма в руб: " + resultDouble + " руб.");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}