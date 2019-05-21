package com.example.android.quakereport;

import android.content.Context;
import java.util.List;
import android.content.AsyncTaskLoader;


public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private String mUrl;
    private static String LOG_TAG = EarthquakeLoader.class.getSimpleName();


    public EarthquakeLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    /**
     * 这位于后台线程上。
     */
    @Override
    public List<Earthquake> loadInBackground() {
        if(mUrl == null){
            return null;
        }

        // 执行网络请求、解析响应和提取地震列表。
        List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        return earthquakes;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }
}
