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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mrrobot.currencyapp.adapter.CurrencyAdapter;
import com.mrrobot.currencyapp.model.Currency;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText userField;
    private EditText userFieldRub;
    private Button mainButton;
    private TextView resultInfo;
    private Button refreshButton;
    private final String URL = "https://www.cbr-xml-daily.ru/daily_json.js";
    private final List<Currency> currencies = new ArrayList<>();

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
        refreshButton = findViewById(R.id.refresh_button);

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userField.getText().toString().trim().equals(""))
                    Toast.makeText(MainActivity.this, R.string.no_user_input, Toast.LENGTH_LONG).show();
                else {

                    new GetUrlData().execute(URL);
                }
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new GetUrlDataCurrency().execute(URL);
            }
        });
    }

    private void setCurrencyRecycler(List<Currency> currencies) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);

        recyclerView = findViewById(R.id.currency_recycler);
        recyclerView.setLayoutManager(layoutManager);

        currencyAdapter = new CurrencyAdapter(getApplicationContext(), currencies);
        recyclerView.setAdapter(currencyAdapter);
    }

    private class GetUrlData extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            resultInfo.setText("Ожидайте...");
        }

        @Override
        protected String doInBackground(String... strings) {

            return connectAndParse(strings[0]);
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
                String name = jsonObject.getJSONObject("Valute").getJSONObject(currency).getString("Name");
                Double resultDouble = (rub / value) * nominal;

                resultInfo.setText("Итог:  " + resultDouble + " " + name);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class GetUrlDataCurrency extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            resultInfo.setText("Ждем");
        }

        @Override
        protected String doInBackground(String... strings) {

            return connectAndParse(strings[0]);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            List<String> charCodeList = getCharCodes();

            try {
                JSONObject jsonObject = new JSONObject(result);

                for (int i = 0; i < charCodeList.size(); i++) {
                    JSONObject jsonValute = jsonObject.getJSONObject("Valute").getJSONObject(charCodeList.get(i));

                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();

                    Currency currency = gson.fromJson(jsonValute.toString(), Currency.class);

                    currencies.add(currency);
                }

                setCurrencyRecycler(currencies);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private String connectAndParse(String string) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(string);
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

    private List<String> getCharCodes() {

        List<String> charCodeList = new ArrayList<>();
        charCodeList.add("AUD");
        charCodeList.add("AZN");
        charCodeList.add("GBP");
        charCodeList.add("AMD");
        charCodeList.add("BYN");
        charCodeList.add("BGN");
        charCodeList.add("BRL");
        charCodeList.add("HUF");
        charCodeList.add("HKD");
        charCodeList.add("DKK");
        charCodeList.add("USD");
        charCodeList.add("EUR");
        charCodeList.add("INR");
        charCodeList.add("KZT");
        charCodeList.add("CAD");
        charCodeList.add("KGS");
        charCodeList.add("CNY");
        charCodeList.add("MDL");
        charCodeList.add("NOK");
        charCodeList.add("PLN");
        charCodeList.add("RON");
        charCodeList.add("XDR");
        charCodeList.add("SGD");
        charCodeList.add("TJS");
        charCodeList.add("TRY");
        charCodeList.add("TMT");
        charCodeList.add("UZS");
        charCodeList.add("UAH");
        charCodeList.add("CZK");
        charCodeList.add("SEK");
        charCodeList.add("CHF");
        charCodeList.add("ZAR");
        charCodeList.add("KRW");
        charCodeList.add("JPY");

        return charCodeList;
    }
}