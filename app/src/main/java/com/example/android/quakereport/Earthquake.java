package com.example.android.quakereport;

public class Earthquake {
    private double mMag;
    private String mLocation;
    private long mTimeInMilliseconds;
    private String mUrl;

    public Earthquake(double mag, String location, long time, String url){
        mMag = mag;
        mLocation = location;
        mTimeInMilliseconds = time;
        mUrl = url;
    }

    public double getMag(){
        return mMag;
    }

    public String getLocation(){
        return mLocation;
    }

    public long getTime(){
        return mTimeInMilliseconds;
    }

    public String getUrl(){
        return mUrl;
    }
}
