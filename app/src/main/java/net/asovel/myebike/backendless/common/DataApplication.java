package net.asovel.myebike.backendless.common;

import android.app.Application;

public class DataApplication extends Application
{
    private String chosenTable;

    public String getChosenTable()
    {
        return chosenTable;
    }

    public void setChosenTable(String chosenTable)
    {
        this.chosenTable = chosenTable;
    }
}