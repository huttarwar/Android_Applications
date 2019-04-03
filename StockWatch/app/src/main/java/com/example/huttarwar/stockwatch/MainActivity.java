package com.example.huttarwar.stockwatch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "";
    private ArrayList<Stocks> stocksList = new ArrayList<>();
    private  ArrayList<TemporaryStocks> temporaryStocksList = new ArrayList<>();
    private RecyclerView recyclerView; // Layout's recyclerview
    private SwipeRefreshLayout swipeRefreshLayout; // The SwipeRefreshLayout
    MenuItem addStock;
    private String searchingsymbol;
    private String symbolSelection;
    private DatabaseHandler databaseHandler;
    private static String marketwatch = "http://www.marketwatch.com/investing/stock/";
    private StocksAdapter stocksAdapter;
    private TemporaryStocksAdapter temporaryStocksAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addStock = (MenuItem) findViewById(R.id.add);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        stocksAdapter = new StocksAdapter(stocksList, this);
        temporaryStocksAdapter = new TemporaryStocksAdapter(temporaryStocksList, this);
        recyclerView.setAdapter(stocksAdapter);
        recyclerView.setAdapter(temporaryStocksAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiper);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                doRefresh();
            }
        });

        databaseHandler = new DatabaseHandler(this);
        ArrayList<Stocks> list = databaseHandler.loadStocks();
        stocksList.addAll(list);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting())
        {
            for (int i = 0; i < stocksList.size(); i++)
            {
                Stocks temporary_stocks = stocksList.get(i);
                System.out.println("MainActivity" + temporary_stocks.getSymbol());
                data_to_download(temporary_stocks);
            }
        }
        else
        {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("No Network Connection");
            alertDialog.setMessage("Stocks Cannot Be Updated Without A Network Connection");
            alertDialog.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.add:
                ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = manager.getActiveNetworkInfo();
                if (info != null && info.isConnectedOrConnecting())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    final EditText editText = new EditText(this);
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                    editText.setGravity(Gravity.CENTER_HORIZONTAL);
                    builder.setView(editText);
                    editText.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            String symbol = editText.getText().toString().trim().replaceAll(", ", ",");
                            searchingsymbol = symbol;
                            if(symbol.equals(""))
                            {
                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                alertDialog.setTitle("Please Enter a symbol");
                                alertDialog.setMessage("Cannot be left blank");
                                alertDialog.show();
                            }
                            else
                            {
                                search_Symbol_Company(symbol);
                            }
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                        }
                    });

                    builder.setMessage("Please enter a Stock Symbol:");
                    builder.setTitle("Stock Selection");
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
                else
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("No Network Connection");
                    alertDialog.setMessage("Stocks Cannot Be Added Without A Network Connection");
                    alertDialog.show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void search_Symbol_Company(String symbol)
    {
        AsyncLoaderTask alt = new AsyncLoaderTask(this);
        alt.execute(symbol);
    }

    public void no_Symbol_Found()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Symbol not Found: " + searchingsymbol);
        alertDialog.setMessage("Data for stock symbol");
        alertDialog.show();
    }

    public void no_Data_Found()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("No Data Found: " + symbolSelection);
        alertDialog.setMessage("There is no data for "+ symbolSelection +" symbol");
        alertDialog.show();
    }


    private void doRefresh()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting())
        {
            ArrayList<TemporaryStocks> copy = (ArrayList<TemporaryStocks>) temporaryStocksList.clone();
            temporaryStocksList.clear();
            for(int i=0;i<copy.size();i++) {
                TemporaryStocks temp = copy.get(i);
                data_Download_2(temp);
            }
        }
        else
        {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("No Network Connection");
            alertDialog.setMessage("Stocks Cannot Be Updated Without A Network Connection");
            alertDialog.show();
        }
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onClick(View v)
    {
        int pos = recyclerView.getChildLayoutPosition(v);
        TemporaryStocks c = temporaryStocksList.get(pos);
        String symbol = c.getSymbol();
        String url = marketwatch + symbol;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v)
    {
        final int pos = recyclerView.getChildLayoutPosition(v);
        TemporaryStocks stocks = temporaryStocksList.get(pos);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                databaseHandler.deleteStock(temporaryStocksList.get(pos).getSymbol());
                temporaryStocksList.remove(pos);
                temporaryStocksAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
            }
        });
        builder.setIcon(R.drawable.ic_delete_black_48dp);

        builder.setMessage("Delete Stock Symbol " + temporaryStocksList.get(pos).getSymbol() + "?");
        builder.setTitle("Delete Stock");
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    //public void choose_Symbol(final ArrayList<Stocks> sList)
    public void choose_Symbol(final HashMap<String,String> sList)
    {
        int j = sList.size();
        Map<String, String> hello = sList;
        Stocks stocks = null;
        Log.d(TAG, String.valueOf(j));
        if (j == 1) {
            for(Map.Entry<String, String> entry:sList.entrySet()){

                stocks = new Stocks(entry.getKey(),entry.getValue());
            }
            data_download(stocks);
        }

        else if(j == 0){
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Symbol Not Found:" + searchingsymbol);
            alertDialog.setMessage("Data for stock symbol");
            alertDialog.show();
        }
        else
            {
            final CharSequence[] stocksArray = new CharSequence[j];
         /*   for (int i = 0; i < j; i++)
            {
                Stocks temp = sList.get(i);
                stocksArray[i] = temp.getSymbol()+"-"+temp.getName();
            }*/
         int i =0;
            for(Map.Entry<String, String> entry:sList.entrySet()){
                    if(i <= j) {
                        stocks = new Stocks(entry.getKey(), entry.getValue());
                        stocksArray[i] = stocks.getSymbol()+"-"+stocks.getName();
                        i++;
                    }

                }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Make a selection");

            builder.setItems(stocksArray, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    String dataToDownload = (String) stocksArray[which];
                    String[] data = dataToDownload.split("-");
                    String symbol = data[0];
                    String name = data[1];
                    int i;
                    for (i = 0; i < sList.size(); i++)
                    {
                        if(stocksArray[i] == dataToDownload)
                            break;
                    }
                    Stocks temp = new Stocks(symbol,name);
                    data_download(temp);
                }
            });

            builder.setNegativeButton("Nevermind", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {   }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }



    public void temporary_choose_Symbol(TemporaryStocks stockList)
    {
        boolean symbolExistOrNot;
        Stocks stocks = new Stocks(stockList.getSymbol(),stockList.getName());
        symbolExistOrNot = databaseHandler.CheckIsDataAlreadyInDBorNot(stocks.getSymbol());
        if(symbolExistOrNot == false) {
            databaseHandler.addStock(stocks);
            temporaryStocksList.add(stockList);
            Collections.sort(temporaryStocksList, new Comparator<TemporaryStocks>()
            {
                @Override
                public int compare(TemporaryStocks left, TemporaryStocks right)
                {
                    return left.getSymbol().compareTo(right.getSymbol());
                }
            });
            temporaryStocksAdapter.notifyDataSetChanged();
        }
        else
        {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setIcon(R.drawable.ic_warning_black_48dp);
            alertDialog.setTitle("Duplicate Stock" );
            alertDialog.setMessage("Stock Symbol "+ stocks.getSymbol() +" is already displayed");
            alertDialog.show();
        }

    }


    private void data_download(Stocks sym){
        String symbol = sym.getSymbol();
        String name = sym.getName();
        symbolSelection = sym.getSymbol();
        AsyncDataLoader alt = new AsyncDataLoader(this);
        alt.execute(symbol ,name);
    }

    private void data_to_download(Stocks sym)
    {
        AsyncDataLoaderStart asyncDataLoaderStart = new AsyncDataLoaderStart(this,sym);
        asyncDataLoaderStart.execute();
    }

    private void data_Download_2(TemporaryStocks symBOL)
    {
        String symbol = symBOL.getSymbol();
        String name = symBOL.getName();
        AsyncDataLoaderSwipe asyncDataLoaderSwipe = new AsyncDataLoaderSwipe(this,new Stocks(symbol,name));
        asyncDataLoaderSwipe.execute();
    }

    public void temporary_choose_Symbol_1(TemporaryStocks sList) {

        temporaryStocksList.add(sList);
        Collections.sort(temporaryStocksList, new Comparator<TemporaryStocks>() {
            @Override
            public int compare(TemporaryStocks left, TemporaryStocks right) {
                return left.getSymbol().compareTo(right.getSymbol());
            }
        });
        temporaryStocksAdapter.notifyDataSetChanged();

    }
    public void temporary_choose_Symbol_2(TemporaryStocks sList) {

        temporaryStocksList.add(sList);
        Collections.sort(temporaryStocksList, new Comparator<TemporaryStocks>()
        {
            @Override
            public int compare(TemporaryStocks left, TemporaryStocks right)
            {
                return left.getSymbol().compareTo(right.getSymbol());
            }
        });
        temporaryStocksAdapter.notifyDataSetChanged();
    }
}

