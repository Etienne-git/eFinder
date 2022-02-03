package com.example.efinder;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;




public class ChargingStation {


    /* General information */
    private int id;
    private String operator;
    private String street;
    private String number;
    private String additional;
    private int postal_code;
    private String location;
    private String state;
    private String area;
    private double lat;
    private double lon;
    private String installation_date;
    private double conn_power;
    private ModuleType module_type;
    private int number_of_connections;

    /* Charging Point 1 */
    private ArrayList<PlugType> plug_types_1;
    private double power_1;
    private String public_key_1;

    /* Charging Point 2 */
    private ArrayList<PlugType> plug_types_2;
    private double power_2;
    private String public_key_2;

    /* Charging Point 3 */
    private ArrayList<PlugType> plug_types_3;
    private double power_3;
    private String public_key_3;

    /* Charging Point 4 */
    private ArrayList<PlugType> plug_types_4;
    private double power_4;
    private String public_key_4;

    /* Usage | maintenance  */
    private boolean is_used;

    private boolean is_favorite = false;



    public boolean isIs_favorite() { return is_favorite; };

    public void set_favorite(boolean is_favorite) { this.is_favorite = is_favorite; };


    /* Getters */
    /**
     * Get the stations id.
     * @return id as int
     * To set ids use: {@link #setId(int)}
     */
    public int getId() {
        return id;
    }

    /**
     * Get the stations operator.
     * @return operator as string
     */
    public final String getOperator() {
        return operator;
    }

    /**
     * Get the stations street.
     * @return street as string
     */
    public final String getStreet() {
        return street;
    }

    /**
     * Get the stations number of street.
     * @return number as string
     */
    public final String getNumber() {
        return number;
    }

    /**
     * Get the stations additional.
     * @return additional as string
     */
    public final String getAdditional() {
        return additional;
    }

    /**
     * Get the stations postal code.
     * @return postal code as int
     */
    public final int getPostal_code() {
        return postal_code;
    }

    /**
     * Get the stations location.
     * @return location as string
     */
    public final String getLocation() {
        return location;
    }

    /**
     * Get the stations state.
     * @return state as string
     */
    public final String getState() {
        return state;
    }

    /**
     * Get the stations area.
     * @return area as string
     */
    public final String getArea() {
        return area;
    }

    /**
     * Get the stations lat position.
     * @return lat as double
     */
    public final double getLat() {
        return lat;
    }

    /**
     * Get the stations lon position.
     * @return lon as double
     */
    public final double getLon() {
        return lon;
    }

    /**
     * Get the stations installation date.
     * @return installation date as date format
     */
    public final String getInstallation_date() {
        return installation_date;
    }

    /**
     * Get the stations connection power.
     * @return connection power as double
     */
    public final double getConn_power() {
        return conn_power;
    }

    /**
     * Get the stations module type.
     * @return module type as enum
     */
    public final ModuleType getModule_type() {
        return module_type;
    }

    /**
     * Get the stations amount of connections.
     * @return amount of connection as int
     */
    public final int getNumber_of_connections() {
        return number_of_connections;
    }

    /**
     * Get the stations plug types.
     * @return ArrayList of enums
     */
    public final ArrayList<PlugType> getPlug_types_1() {
        return plug_types_1;
    }

    /**
     * Get the stations first plug power.
     * @return power as double
     */
    public final double getPower_1() {
        return power_1;
    }

    /**
     * Get the stations public key for first connection.
     * @return key as string
     */
    public final String getPublic_key_1() {
        return public_key_1;
    }

    /**
     * Get the stations plug types.
     * @return ArrayList of enums
     */
    public final ArrayList<PlugType> getPlug_types_2() {
        return plug_types_2;
    }

    /**
     * Get the stations second plug power.
     * @return power as double
     */
    public final double getPower_2() {
        return power_2;
    }

    /**
     * Get the stations public key for second connection.
     * @return key as string
     */
    public final String getPublic_key_2() {
        return public_key_2;
    }

    /**
     * Get the stations plug types.
     * @return ArrayList of enums
     */
    public final ArrayList<PlugType> getPlug_types_3() {
        return plug_types_3;
    }

    /**
     * Get the stations third plug power.
     * @return power as double
     */
    public final double getPower_3() {
        return power_3;
    }

    /**
     * Get the stations public key for third connection.
     * @return key as string
     */
    public final String getPublic_key_3() {
        return public_key_3;
    }

    /**
     * Get the stations plug types.
     * @return ArrayList of enums
     */
    public final ArrayList<PlugType> getPlug_types_4() {
        return plug_types_4;
    }

    /**
     * Get the stations fourth plug power.
     * @return power as double
     */
    public final double getPower_4() {
        return power_4;
    }

    /**
     * Get the stations public key for fourth connection.
     * @return key as string
     */
    public final String getPublic_key_4() {
        return public_key_4;
    }

    /**
     * Get the stations usability.
     * @return boolean for usage
     */
    public boolean isIs_used() {
        return is_used;
    }

    /**
     * Give the station an id.
     * @param id as int
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter for usage.
     * @param is_used as boolean
     */
    public void setIs_used(boolean is_used) {
        this.is_used = is_used;
    }




}
