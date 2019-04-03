package com.example.huttarwar.stockwatch;

import java.io.Serializable;



public class TemporaryStocks implements Serializable {

    private String symbol;
    private String name;
    private String value;
    private String updown;
    private String change;
    private String changepercent;


    public TemporaryStocks(String symBOL, String nAME, String vALUE, String aRROW, String cHANGE, String cHANGEPERCENT)
    {
        symbol=symBOL;
        name=nAME;
        value=vALUE;
        updown=aRROW;
        change=cHANGE;
        changepercent=cHANGEPERCENT;
    }

    public String getSymbol()
    {return symbol;}

    public String getName()
    {return name;}

    public String getValue()
    {return value;}

    public String getUpdown()
    {return updown;}

    public String getChange()
    {return change;}

    public String getChangep()
    {return changepercent;}
}
