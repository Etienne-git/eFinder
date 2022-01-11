package com.example.efinder;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DownloadService {

    /**
     * Docker for station arrayList.
     * @return array of station objects
     */
    public static ArrayList<ChargingStation> getStations() {
        Gson gson = new Gson();
        final Type return_type = new TypeToken<ArrayList<ChargingStation>>() {}.getType();
        final String json = getJson();

        return gson.fromJson(json, return_type);
    }

    /**
     * Download json from api.
     * @return json data string
     * @link https://api.aurora-theogenia.de/chargingstations/charging_stations.json
     */
    private static String getJson() {
        BufferedReader reader = null;
        try {
            final URL url = new URL("https://api.aurora-theogenia.de/chargingstations/charging_stations.json");
            reader = new BufferedReader(new InputStreamReader(read(url)));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create InputStream with connection timeout (for big files).
     * @param url api url
     * @return Configured InputStream object
     * @throws IOException if url is bad or doesn't exist
     */
    private static InputStream read(URL url) throws IOException {
        final HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
        httpCon.setConnectTimeout(10000);
        httpCon.setReadTimeout(10000);
        return httpCon.getInputStream();
    }


}
