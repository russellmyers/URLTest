package com.example.russellm.myfirstapp;

import android.os.AsyncTask;
import android.util.Log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * Created by RussellM on 26/05/2016.
 */

interface HTMLReaderResponse {
    void htmlReadFinish(String output,String err);

}
public class HTMLReader  extends AsyncTask<URL, Void, String[]>   {

    private URL url;
    public HTMLReaderResponse delegate;

    public HTMLReader(String urlString,HTMLReaderResponse inDelegate) {
        try {
            url = new URL(urlString);
        }
        catch (java.net.MalformedURLException e) {
            url = null;
        }
        delegate = inDelegate;


    }

    public URL getURL() {
        return url;
    }

    public String[] checkConnection(android.app.Activity act) {
        android.net.ConnectivityManager connMgr = (android.net.ConnectivityManager)
                act.getSystemService(android.content.Context.CONNECTIVITY_SERVICE);

        android.net.NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            return new String[] {"0",networkInfo.getExtraInfo()};


        } else {
            // display error
            return new String[] {"-1",networkInfo.getReason()};

        }
    }

    public int  readHTML(android.app.Activity act) {

        if (checkConnection(act).equals("-1")) {
            return -1;
        }

        execute(url);
        return 0;

    }


    @Override
    protected String[] doInBackground(URL... urls) {

        String[] ret = {null,"err"};

       // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String htmlString = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                ret[1] = "null input stream";
                return ret;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                ret[1] = "Empty Stream";
                return ret;
            }
            htmlString = buffer.toString();
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            ret[1] = e.toString();
            return ret;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                    ret[1] = "Closing error";
                    return ret;
                }
            }
        }

        ret[0] = htmlString;
        ret[1] = "";
        return ret;
    }

    @Override
    protected void onPostExecute(String[] result) {
        //textView.setText(result);
        // TextView tv = (TextView)findViewById(R.id.textView);
        // tv.setText(result);
        delegate.htmlReadFinish(result[0],result[1]);
    }
}




