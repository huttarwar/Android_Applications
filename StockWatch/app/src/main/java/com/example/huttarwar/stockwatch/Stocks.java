package com.example.huttarwar.stockwatch;

import java.io.Serializable;



public class Stocks implements Serializable
{
    private String symbol;
    private String name;
    public Stocks()
    {    }

    public Stocks(String symbol, String name)
    {
        this.symbol = symbol;
        this.name= name;
    }

    public String getSymbol()
    {return symbol;}

    public String getName()
    {return name;}


}
