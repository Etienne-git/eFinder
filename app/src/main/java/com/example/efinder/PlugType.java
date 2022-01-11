package com.example.efinder;

import com.google.gson.annotations.SerializedName;

public enum PlugType {
    @SerializedName("AC Steckdose Typ 2") AC_PLUG_TYPE_2,
    @SerializedName("AC Kupplung Typ 2") AC_CLUTCH_TYPE_2,
    @SerializedName("AC Schuko") AC_SCHUKO,
    @SerializedName("DC Kupplung Combo") DC_CLUTCH_COMBO,
    @SerializedName("DC CHAdeMO") DC_CHADEMO
}