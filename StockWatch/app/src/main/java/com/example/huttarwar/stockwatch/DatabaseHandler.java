package com.example.huttarwar.stockwatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;



public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHandler";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "StockAppDB";
    private static final String TABLE_NAME = "StockWatchTable";
    private static final String SYMBOL = "StockSymbol";
    private static final String COMPANY = "CompanyName";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    SYMBOL + " TEXT not null unique," +
                    COMPANY + " TEXT not null)";

    private SQLiteDatabase database;

    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {    }




    public boolean CheckIsDataAlreadyInDBorNot(String fieldValue)
    {
        String Query = "Select * FROM " + TABLE_NAME + " WHERE " + SYMBOL + " =?";
        Cursor cursor = database.rawQuery(Query, new String[] {fieldValue});
        if(cursor.getCount() <= 0)
        {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void addStock(Stocks stocks)
    {
        Log.d(TAG, "addStock: Adding " + stocks.getSymbol());
        ContentValues values = new ContentValues();
        values.put(SYMBOL, stocks.getSymbol());
        values.put(COMPANY, stocks.getName());
        database.insert(TABLE_NAME, null, values);
        Log.d(TAG, "Add Complete");
    }

    public void deleteStock(String symbol)
    {
        database.delete(TABLE_NAME, SYMBOL + " = ?", new String[]{symbol});
    }

    public ArrayList<Stocks> loadStocks()
    {
        ArrayList<Stocks> stocks = new ArrayList<>();
        Cursor cursor = database.query(
                TABLE_NAME,  // The table to query
                new String[]{SYMBOL, COMPANY}, // The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null); // The sort order
        if (cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String symbol = cursor.getString(0);
                String company = cursor.getString(1);
                stocks.add(new Stocks(symbol, company));
                cursor.moveToNext();
            }
            cursor.close();
        }

        return stocks;
    }
}
