package com.example.jessemitchell.popularmovies.app;

import android.app.Application;

import com.example.jessemitchell.popularmovies.app.data.NetworkService;

/**
 * Created by jesse.mitchell on 1/18/2017.
 */

public class RxApplication extends Application
{
    private NetworkService netService;

    @Override
    public void onCreate() {
        super.onCreate();
        netService = new NetworkService();
        netService.clearCache();
    }

    public NetworkService getNetorkService()
    {
        return netService;
    }
}
