package com.example.android.quakereport;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Earthquake> extractEarthquakes(String jsonResponse) {

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Earthquake> earthquakes = new ArrayList<Earthquake>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray earthquakeArray = root.optJSONArray("features");
            int earthquake_count = earthquakeArray.length();
            for(int i = 0; i < earthquake_count; i++){
                JSONObject item = earthquakeArray.getJSONObject(i);
                JSONObject properties = item.optJSONObject("properties");
                double mag = properties.optDouble("mag");
                String location = properties.optString("place");
                long timeInMilliseconds = properties.optLong("time");
                String url = properties.getString("url");
                earthquakes.add(new Earthquake(mag, location, timeInMilliseconds, url));
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }
    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }
    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // if the url is null, then return early
        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            // if the response was successful (response code 200), then read the input stream and parse the response.
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

            } else{
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            // TODO: Handle the exception
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            // inputStream里存放的不是string是0101，因为我们知道里面应该是string，所以把inputStream里的0101，转换成string
            // 为了开始从inputStream中读取数据，我们将inputStream作为构造函数的一个参数传递给InputStreamReader
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            // inputStreamReader一次只能读一个字符，很慢，所以将inputStreamReader包装到BufferedReader中
            // BufferedReader在接收到对某个字符的请求后，会读取并保存该字符前后的一大块数据
            // 当继续请求另一个字符时，BufferedReader就能够利用提前读取的数据，来满足请求，而无需再回到inputStreamReader
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    public static List<Earthquake> fetchEarthquakeData(String... urls){
        // Perform HTTP request to the URL and receive a JSON response back
        List<Earthquake> earthquakes = new ArrayList<>();
        if(urls.length < 1 || urls[0] == null){
            return null;
        }

        for(int i = 0; i < urls.length; i++){
            // Create URL object
            URL url = QueryUtils.createUrl(urls[i]);
            try {
                String jsonResponse = QueryUtils.makeHttpRequest(url);
                earthquakes.addAll(QueryUtils.extractEarthquakes(jsonResponse));
            } catch (IOException e) {
                // TODO Handle the IOException
                Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
            }
        }
        return earthquakes;
    }
}