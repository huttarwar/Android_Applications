package com.example.huttarwar.stockwatch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;




public class StocksAdapter extends RecyclerView.Adapter<StocksViewHolder>
{
    private List<Stocks> stocksList;
    private MainActivity mainActivity;

    public StocksAdapter(List<Stocks> empList, MainActivity ma)
    {
        this.stocksList = empList;
        mainActivity = ma;
    }

    @Override
    public StocksViewHolder onCreateViewHolder(final ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_list, parent, false);
        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);
        return new StocksViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StocksViewHolder holder, int position)
    {
        Stocks stocks = stocksList.get(position);
        holder.symbol.setText(stocks.getSymbol());
        holder.name.setText(stocks.getName());
    }

    @Override
    public int getItemCount()
    {    return stocksList.size();    }
}
