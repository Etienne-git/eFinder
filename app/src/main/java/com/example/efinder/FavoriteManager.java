package com.example.efinder;

import java.util.ArrayList;

public class FavoriteManager {

    /**
     * Main container for stations (static to get data from all activities / fragments).
     */
    private static ArrayList<ChargingStation> station_storage = new ArrayList<>(
    );

    /**
     * Setter for container.
     * @param station_list of station objects
     */
    public static void setStation_list(ArrayList<ChargingStation> station_list) {
        station_storage = station_list;
    }

    /**
     * Getter for station container.
     * @return arrayList with station objects
     */
    public static ArrayList<ChargingStation> getStation_list() {
        return station_storage;
    }

}
