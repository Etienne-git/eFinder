package com.example.efinder;

import com.google.gson.annotations.SerializedName;

public enum ModuleType {
    @SerializedName("Normalladeeinrichtung") STANDARD,
    @SerializedName("Schnellladeeinrichtung") FAST_CHARGING
}