package com.example.efinder;

import android.app.Application;
import android.os.StrictMode;

import java.util.ArrayList;

public class fetchChargingStationsOnStartup extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }

        ArrayList<ChargingStation> chargingStations = DownloadService.getStations();
        StationManager.setStation_list(chargingStations);

        //Initalization of favorites list
        ArrayList<ChargingStation> favorites = new ArrayList<>();
        FavoriteManager.setStation_list(favorites);


    }
}
