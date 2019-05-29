package com.hunglq.map1.model;

public class PokeStop {
    int PokeStopId;
    String PokeStopName;
    double Latitude;
    double Longitude;

    public PokeStop(int pokeStopId, String pokeStopName, double latitude, double longitude) {
        PokeStopId = pokeStopId;
        PokeStopName = pokeStopName;
        Latitude = latitude;
        Longitude = longitude;
    }

    public int getPokeStopId() {
        return PokeStopId;
    }

    public void setPokeStopId(int pokeStopId) {
        PokeStopId = pokeStopId;
    }

    public String getPokeStopName() {
        return PokeStopName;
    }

    public void setPokeStopName(String pokeStopName) {
        PokeStopName = pokeStopName;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
