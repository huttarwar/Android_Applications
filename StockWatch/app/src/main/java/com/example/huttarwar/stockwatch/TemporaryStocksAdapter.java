package com.example.huttarwar.stockwatch;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class TemporaryStocksAdapter extends RecyclerView.Adapter<StocksViewHolder>
{
    private List<TemporaryStocks> stockList;
    private MainActivity mainActivity;


    public TemporaryStocksAdapter(List<TemporaryStocks> temporaryStocksList, MainActivity ma)
    {
        this.stockList = temporaryStocksList;
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
    public void onBindViewHolder(StocksViewHolder stocksViewHolder, int position)
    {
        TemporaryStocks stock = stockList.get(position);
        Double check = Double.parseDouble(stock.getChange());
        if(check > 0)
        {
            stocksViewHolder.symbol.setText(stock.getSymbol());
            stocksViewHolder.symbol.setTextColor(Color.parseColor("#3CE522"));
            stocksViewHolder.name.setText(stock.getName());
            stocksViewHolder.name.setTextColor(Color.parseColor("#3CE522"));
            stocksViewHolder.value.setText(stock.getValue());
            stocksViewHolder.value.setTextColor(Color.parseColor("#3CE522"));
            stocksViewHolder.upDown.setImageResource(R.drawable.ic_arrow_drop_up);
            stocksViewHolder.change.setText(stock.getChange());
            stocksViewHolder.change.setTextColor(Color.parseColor("#3CE522"));
            stocksViewHolder.changepercent.setText("(" + stock.getChangep() + "%" + ")");
            stocksViewHolder.changepercent.setTextColor(Color.parseColor("#3CE522"));
        }
        else
        {
            stocksViewHolder.symbol.setText(stock.getSymbol());
            stocksViewHolder.symbol.setTextColor(Color.parseColor("#FD0504"));
            stocksViewHolder.name.setText(stock.getName());
            stocksViewHolder.name.setTextColor(Color.parseColor("#FD0504"));
            stocksViewHolder.value.setText(stock.getValue());
            stocksViewHolder.value.setTextColor(Color.parseColor("#FD0504"));
            stocksViewHolder.upDown.setImageResource(R.drawable.ic_arrow_drop_down);
            stocksViewHolder.change.setText(stock.getChange());
            stocksViewHolder.change.setTextColor(Color.parseColor("#FD0504"));
            stocksViewHolder.changepercent.setText("(" + stock.getChangep() + "%" + ")");
            stocksViewHolder.changepercent.setTextColor(Color.parseColor("#FD0504"));
        }
    }

    @Override
    public int getItemCount()
    {
        return stockList.size();
    }
}
