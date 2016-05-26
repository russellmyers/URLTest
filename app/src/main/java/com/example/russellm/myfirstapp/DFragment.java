package com.example.russellm.myfirstapp;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.view.inputmethod.*;


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

import android.text.TextWatcher;
import android.text.Editable;
import android.widget.ProgressBar;


/**
 * Created by RussellM on 24/05/2016.
 *
 */
public class DFragment extends DialogFragment implements AsyncResponse {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            getDialog().setTitle("DialogFragment Tutorial");
            final View rootView = inflater.inflate(R.layout.dialogfragment, container,
                    false);

            // Do something else

            //getDialog().getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            final Button installButton = (Button)rootView.findViewById(R.id.installButton);
            // Capture button clicks
            installButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {

                }
            });

            Button cancelButton = (Button)rootView.findViewById(R.id.cancelButton);
            // Capture button clicks
            cancelButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    getDialog().dismiss();

                }
            });

            final EditText quizCodeEditText = (EditText)rootView.findViewById(R.id.quizCodeEditText);

            quizCodeEditText.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                   // textView.setVisibility(View.VISIBLE);
                }

                public void afterTextChanged(Editable s) {

                    //InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(android.content.Context.INPUT_METHOD_SERVICE);

                    //if(imm.isAcceptingText()) { // verify if the soft keyboard is open
                    //    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    //}

                    getDialog().getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                    /*
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(android.app.Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    //getDialog().getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    */
                    ProgressBar progBar = (ProgressBar)rootView.findViewById(R.id.progressBar);

                   // if (quizCodeEditText.getText().toString().equals("GENEV")) {
                    if (quizCodeEditText.getText().toString().length() == 5) {
                        progBar.setVisibility(View.VISIBLE);
                        quizCodeEditText.setFocusableInTouchMode(false);
                        quizCodeEditText.setFocusable(false);
                        quizCodeEditText.setFocusableInTouchMode(true);
                        quizCodeEditText.setFocusable(true);
                        doSomething(quizCodeEditText.getText().toString());
                    }
                    else {
                        installButton.setEnabled(false);
                        progBar.setVisibility(View.INVISIBLE);
                    }
                    /*
                    if (s.length() == 0) {
                        quizCodeEditText.setVisibility(View.GONE);
                    } else {
                        quizCodeEditText.setText("You have entered : " + quizCodeEditText.getText());
                    }
                    */
                }
            });


            return rootView;
        }

        public void doSomething(String s) {

            getDialog().getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            android.net.ConnectivityManager connMgr = (android.net.ConnectivityManager)
                    getActivity().getSystemService(android.content.Context.CONNECTIVITY_SERVICE);

            android.net.NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                // fetch data
                TextView net = (TextView) getView().findViewById(R.id.netIndicView);
                net.setText("Network connection successful");
                String link = "http://quizupdate.genevol.com/testQuizCode.php?quizCode=";
                DownloadWebpageTask aTask = new DownloadWebpageTask();
                aTask.delegate = DFragment.this;
                aTask.execute(link + s);
                try {
                    // String ss = a.get();
                } catch (Exception e) {

                }
            } else {
                // display error
                TextView net = (TextView)getView().findViewById(R.id.netIndicView);
                net.setText("Network connection unsuccessful");
            }
        }

    //this override the implemented method from asyncTask
    @Override
    public void processFinish(String output){

        Button installButton = (Button)getView().findViewById(R.id.installButton);



        ProgressBar progBar = (ProgressBar)getView().findViewById(R.id.progressBar);
        progBar.setVisibility(View.INVISIBLE);
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

        String erS;
        try {
            erS =  "";
            erS = (String)jObject.get("error");
            erS = erS.substring(0,3);
            if (erS.equals("NOK")) {

            }
            else {
                installButton.setEnabled(true);


            }

        } catch (Exception e) {

        }

        TextView quizName = (TextView)getView().findViewById(R.id.quizName);
        try {

            quizName.setText("HTML: " + jObject.get("quizText"));

           // quizName.setText("HTML: " + "Quiz Code: " + jObject.get("quizCode") + " name: " + jObject.get("quizText") + " whole thing: " + output);
        }
        catch(Exception e) {
            quizName.setText("Parsing error. Input: " + output);

        }
    }

}

