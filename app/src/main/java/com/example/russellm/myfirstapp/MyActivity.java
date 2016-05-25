package com.example.russellm.myfirstapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.*;
import java.net.*;
import android.util.*;
import android.os.*;
import android.view.inputmethod.*;
import org.json.*;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;

interface AsyncResponse {
    void processFinish(String output);
}



class DownloadWebpageTask extends AsyncTask<String, Void, String> {

    String DEBUG_TAG =  "My test app";
    public AsyncResponse delegate = null;


    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {

        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        /*
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
        */
        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(reader);
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
        }
        String content = contentBuilder.toString();
        return content;
    }


    // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 30000;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    @Override
    protected String doInBackground(String... urls) {

        // params comes from the execute() call: params[0] is the url.
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
       //textView.setText(result);
       // TextView tv = (TextView)findViewById(R.id.textView);
       // tv.setText(result);
        delegate.processFinish(result);

    }
}




public class MyActivity extends AppCompatActivity implements AsyncResponse {


    FragmentManager fm = getSupportFragmentManager();



    //@Override



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button clickButton = (Button) findViewById(R.id.clickButton);
        clickButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               // ***Do what you want with the click here***

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                if(imm.isAcceptingText()) { // verify if the soft keyboard is open
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }


                android.net.ConnectivityManager connMgr = (android.net.ConnectivityManager)
                        getSystemService(android.content.Context.CONNECTIVITY_SERVICE);

                android.net.NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    // fetch data
                    TextView tv = (TextView)findViewById(R.id.textView);
                    tv.setText("Network connection successful");
                    EditText et = (EditText)findViewById(R.id.urlToShow);
                    DownloadWebpageTask aTask = new DownloadWebpageTask();
                    aTask.delegate = MyActivity.this;
                    aTask.execute(et.getText().toString());
                    try {
                       // String ss = a.get();
                    } catch (Exception e) {

                    }
                } else {
                    // display error
                    TextView tv = (TextView)findViewById(R.id.textView);
                    tv.setText("Network connection unsuccessful");
                }

                EditText tv1 = (EditText)findViewById(R.id.editView);
                String s = tv1.getText().toString();
                long myNum = 0;
                try {
                    myNum = Long.parseLong(s);
                } catch(NumberFormatException nfe) {
                    System.out.println("Could not parse " + s);
                }
                long i = myNum * myNum;
                tv1.setText(String.valueOf(i));
            }
        });

        // Locate the button in activity_main.xml
        Button dfragbutton = (Button) findViewById(R.id.quizButton);

        // Capture button clicks
        dfragbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                DFragment dFragment = new DFragment();
                // Show DialogFragment
                dFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                dFragment.show(fm, "Dialog Fragment");
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //this override the implemented method from asyncTask
    @Override
    public void processFinish(String output){
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
        int i = 1;
        JSONObject jObject;
        try {
            jObject = new JSONObject(output);
        }
        catch (Exception e) {
            jObject = null;
        }

        TextView tv = (TextView)findViewById(R.id.textView);
        try {

            tv.setText("HTML: " + "Quiz Code: " + jObject.get("quizCode") + " name: " + jObject.get("quizText") + " whole thing: " + output);
        }
        catch(Exception e) {
            tv.setText("Parsing error. Input: " + output);

        }
    }
}
