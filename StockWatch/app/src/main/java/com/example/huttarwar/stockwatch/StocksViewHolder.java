package com.example.huttarwar.stockwatch;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



public class StocksViewHolder extends RecyclerView.ViewHolder
{
    public TextView symbol;
    public TextView name;
    public TextView value;
    public ImageView upDown;
    public TextView change;
    public TextView changepercent;

    public StocksViewHolder(View view)
    {
        super(view);
        symbol = (TextView) view.findViewById(R.id.sSymbol);
        name = (TextView) view.findViewById(R.id.sName);
        value = (TextView) view.findViewById(R.id.sValue);
        upDown = (ImageView) view.findViewById(R.id.sUpDown);
        change = (TextView) view.findViewById(R.id.sChange);
        changepercent = (TextView) view.findViewById(R.id.sChangeP);
    }
}
