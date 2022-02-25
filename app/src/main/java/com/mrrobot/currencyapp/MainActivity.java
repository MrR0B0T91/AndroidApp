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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mrrobot.currencyapp.adapter.CurrencyAdapter;
import com.mrrobot.currencyapp.model.Currency;

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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText userField;
    private EditText userFieldRub;
    private Button mainButton;
    private TextView resultInfo;

    private RecyclerView recyclerView;
    private CurrencyAdapter currencyAdapter;

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

        List<Currency> currencies = new ArrayList<>();

        currencies.add(new Currency("R01010", "036", "AUD", 1, "Австралийский доллар", 62.4844, 57.9663));
        currencies.add(new Currency("R01010", "036", "AUD", 1, "Канадский доллар", 32.4844, 27.9663));
        currencies.add(new Currency("R01010", "036", "AUD", 1, "Африканский доллар", 14.48, 15.96));
        currencies.add(new Currency("R01010", "036", "AUD", 1, "Австралийский доллар", 62.4844, 57.9663));
        currencies.add(new Currency("R01010", "036", "AUD", 1, "Австралийский доллар", 62.4844, 57.9663));

        setCurrencyRecycler(currencies);

    }

    private void setCurrencyRecycler(List<Currency> currencies) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        recyclerView = findViewById(R.id.currency_recycler);
        recyclerView.setLayoutManager(layoutManager);

        currencyAdapter = new CurrencyAdapter(this, currencies);
        recyclerView.setAdapter(currencyAdapter);
    }

    private class GetUrlData extends AsyncTask<String, String, String> {

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
                Double nominal = jsonObject.getJSONObject("Valute").getJSONObject(currency).getDouble("Nominal");
                Double resultDouble = (rub / value) * nominal;

                resultInfo.setText("Сумма в руб: " + resultDouble + " руб.");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}