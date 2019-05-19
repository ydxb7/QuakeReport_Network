package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthquakeAdapter extends ArrayAdapter {
    private int mResource;

    public EarthquakeAdapter(Context context, int resource, ArrayList<Earthquake> objects) {
        super(context, resource, objects);
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(mResource, parent, false);
        }

        Earthquake currentEatrhquakeItem = (Earthquake) getItem(position);

        // show magnitude
        Double mag = currentEatrhquakeItem.getMag();
        String magToDisplay = formatMagnitude(mag);

        TextView mag_text_view = listItemView.findViewById(R.id.mag_text_view);
        mag_text_view.setText(magToDisplay);

        // 为震级圆圈设置正确的背景颜色。
        // 从 TextView 获取背景，该背景是一个 GradientDrawable。
        GradientDrawable magnitudeCircle = (GradientDrawable) mag_text_view.getBackground();

        // 根据当前的地震震级获取相应的背景颜色
        int magnitudeColor = getMagnitudeColor(mag);

        // 设置震级圆圈的颜色
        magnitudeCircle.setColor(magnitudeColor);


        // show location and location deviation
        String original_location = currentEatrhquakeItem.getLocation();
        int idx = original_location.indexOf("of");
        String location;
        String location_deviation;
        if (idx != -1) {
            location = original_location.substring(idx + 3);
            location_deviation = original_location.substring(0, idx + 3);
        } else {
            location = original_location;
            location_deviation = "Near the";
        }

        TextView location_text_view = listItemView.findViewById(R.id.location_text_view);
        location_text_view.setText(location);

        TextView location_deviation_text_view = listItemView.findViewById(R.id.location_deviation_text_view);
        location_deviation_text_view.setText(location_deviation);

        // show date
        TextView date_text_view = listItemView.findViewById(R.id.date_text_view);
        Date dateObject = new Date(currentEatrhquakeItem.getTime());
        String date = formatDate(dateObject);
        date_text_view.setText(date);

        // show time
        TextView time_text_view = listItemView.findViewById(R.id.time_text_view);
        String time = formatTime(dateObject);
        time_text_view.setText(time);

        return listItemView;
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy");
        String dateToDisplay = dateFormatter.format(dateObject);
        return dateToDisplay;
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm a");
        String timeToDisplay = dateFormatter.format(dateObject);
        return timeToDisplay;
    }

    private String formatMagnitude(Double mag) {
        DecimalFormat formatter = new DecimalFormat("0.0");
        return formatter.format(mag);
    }

    private int getMagnitudeColor(double mag) {
        int magnitude = (int) Math.floor(mag);
        int ans;
        switch (magnitude) {
            case 9:
                ans = ContextCompat.getColor(getContext(), R.color.magnitude9);
                break;
            case 8:
                ans = ContextCompat.getColor(getContext(), R.color.magnitude8);
                break;
            case 7:
                ans = ContextCompat.getColor(getContext(), R.color.magnitude7);
                break;
            case 6:
                ans = ContextCompat.getColor(getContext(), R.color.magnitude6);
                break;
            case 5:
                ans = ContextCompat.getColor(getContext(), R.color.magnitude5);
                break;
            case 4:
                ans = ContextCompat.getColor(getContext(), R.color.magnitude4);
                break;
            case 3:
                ans = ContextCompat.getColor(getContext(), R.color.magnitude3);
                break;
            case 2:
                ans = ContextCompat.getColor(getContext(), R.color.magnitude2);
                break;
            case 1:
            case 0:
                ans = ContextCompat.getColor(getContext(), R.color.magnitude1);
                break;
            default:
                ans = ContextCompat.getColor(getContext(), R.color.magnitude10plus);
                break;
        }
        Log.v("EarthquakeAdapter", "mag = " + mag + "     int mag = " + magnitude);
        return ans;
    }
}
