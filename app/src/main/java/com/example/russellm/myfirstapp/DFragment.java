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


/**
 * Created by RussellM on 24/05/2016.
 *
 */
public class DFragment extends DialogFragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            getDialog().setTitle("DialogFragment Tutorial");
            View rootView = inflater.inflate(R.layout.dialogfragment, container,
                    false);

            // Do something else

            Button installButton = (Button)getView().findViewById(R.id.installButton);
            // Capture button clicks
            installButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {

                }
            });

            Button cancelButton = (Button)getView().findViewById(R.id.cancelButton);
            // Capture button clicks
            cancelButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {

                }
            });


            return rootView;
        }
}

