package com.niro.niroapp.pubnub_chat.onetoone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.niro.niroapp.R;


public class OneToOne extends Fragment {
    public OneToOne() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.onetoone, container, false);
    }
}
