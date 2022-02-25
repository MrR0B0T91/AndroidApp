package com.mrrobot.currencyapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrrobot.currencyapp.R;
import com.mrrobot.currencyapp.model.Currency;

import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {

    Context context;
    List<Currency> currencies;

    public CurrencyAdapter(Context context, List<Currency> currencies) {
        this.context = context;
        this.currencies = currencies;
    }

    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View currencyItems = LayoutInflater.from(context).inflate(R.layout.currency_item, parent, false);
        return new CurrencyAdapter.CurrencyViewHolder(currencyItems);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {

        holder.currencyTitle.setText(currencies.get(position).getName());
        holder.valueNow.setText(currencies.get(position).getValue().toString());
        holder.valuePrevious.setText(currencies.get(position).getPrevious().toString());

    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public static final class CurrencyViewHolder extends RecyclerView.ViewHolder {


        TextView currencyTitle, valueNow, valuePrevious;

        public CurrencyViewHolder(@NonNull View itemView) {
            super(itemView);

            currencyTitle = itemView.findViewById(R.id.currency_name);
            valueNow = itemView.findViewById(R.id.value_now);
            valuePrevious = itemView.findViewById(R.id.value_previous);

        }
    }
}
