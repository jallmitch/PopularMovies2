package com.example.jessemitchell.popularmovies.app.sync;

import android.app.Application;

/**
 * Created by jesse.mitchell on 1/18/2017.
 */

public class RxApplication extends Application
{
    private NetworkService netService;

    @Override
    public void onCreate()
    {
        super.onCreate();
        netService = new NetworkService();
    }

    public NetworkService getNetorkService()
    {
        return netService;
    }
}
