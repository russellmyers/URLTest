package com.example.russellm.myfirstapp;

import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;

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
            return rootView;
        }
}

