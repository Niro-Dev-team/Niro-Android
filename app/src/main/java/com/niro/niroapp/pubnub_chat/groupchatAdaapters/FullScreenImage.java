package com.niro.niroapp.pubnub_chat.groupchatAdaapters;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.niro.niroapp.R;


public class FullScreenImage extends AppCompatActivity {
ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreenimage);
        imageView = findViewById(R.id.fullscreenimage);
        String passedArg = getIntent().getExtras().getString("url");

        Glide.with(getApplicationContext())
                .load(passedArg)
                .into(imageView);


    }
}
