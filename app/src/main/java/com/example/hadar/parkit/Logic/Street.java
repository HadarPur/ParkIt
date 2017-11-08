package com.example.hadar.parkit.Logic;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/** contains info about single street **/
public class Street implements Serializable {
    private static final String TAG ="STREET";
    private String cars, rate, sensors, street;
    private int numOfCars, numOfSensors;
    private double occupacy, utilityValue;
    private float[] walking_distance;
    private UserLocation streetLocation;

    //c'tor
    public Street(){
    }

    //c'tor
    public Street(String cars, String rate, String sensors, String street, Activity activity){
        this.cars = cars;
        this.rate = rate;
        this.sensors = sensors;
        this.street = street;
    }

    //convert numbers
    public void convertAll(){
        this.occupacy = Double.parseDouble(rate);
        this.numOfCars = Integer.parseInt(cars);
        this.numOfSensors = Integer.parseInt(sensors);
    }

    /** calculates the location of a street by name **/
    public void findStreetLocation(Activity activity,String name){
        streetLocation = getLocation(name,activity);
    }

    //get location by name
    public UserLocation getLocation(String name, Activity activity){
        Geocoder coder = new Geocoder(activity.getApplicationContext());
        List<Address> address;
        List<Address> streets;
        UserLocation location=null;

        try {
            Log.d(TAG,"name: "+name);
            address = coder.getFromLocationName(name, 5);
            if (address == null) {
                return null;
            }
            if (address.size() == 0) {
                return null;
            }

            Address add = address.get(0);
            location = new UserLocation(activity,add.getLatitude(),add.getLongitude());
            String neighName= add.getSubLocality();
            streets = coder.getFromLocation(location.getLatitude(),location.getLongitude(),10);
            Log.d(TAG,"number of dtreets: : "+streets.size());

            for(int i=0; i<streets.size(); i++){
                String stname = streets.get(i).getThoroughfare();
                Log.d(TAG,"street: "+stname);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return location;
    }

    //calculate radius
    public void calcWalkingDistance(String destination,Activity activity){
        walking_distance=new float[1];
        UserLocation destinationloc=getLocation(destination,activity);
        Location.distanceBetween(streetLocation.getLatitude(),streetLocation.getLongitude(),
                destinationloc.getLatitude(), destinationloc.getLongitude(),
                this.walking_distance);
    }

    //getter
    public double getWalking_distance() {
        return this.walking_distance[0];
    }

    /** calculates the utility value of each street **/
    public void utilityFunction(double Max_Walking){
        double walking_distance_norm,cars_rate,accupacy_Rate;
        convertAll();
        accupacy_Rate=norm(occupacy,100);
        walking_distance_norm=norm(walking_distance[0] ,Max_Walking);
        cars_rate=norm(numOfCars, numOfSensors);
        utilityValue=10*accupacy_Rate+5*cars_rate+2*walking_distance_norm;
    }

    //getter
    public double getUtilityValue() {
        return utilityValue;
    }

    /** calculate norm **/
    public double norm(double real_Value, double Max_value){
        double norm;
        norm= real_Value/ Max_value;
        return norm;
    }

    public String getStreet() {
        return street;
    }

    public String getCars() {
        return cars;
    }

    public String getRate() {
        return rate;
    }

    public String getSensors() {
        return sensors;
    }

    public UserLocation getStreetLocation() {
        return streetLocation;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setSensors(String sensors) {
        this.sensors = sensors;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCars(String cars) {
        this.cars = cars;
    }

    public double getOccupacy() {
        return this.occupacy;
    }

}
