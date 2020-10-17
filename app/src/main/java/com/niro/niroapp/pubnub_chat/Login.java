package com.niro.niroapp.pubnub_chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.niro.niroapp.R;
import com.niro.niroapp.pubnub_chat.groupchat.GroupChatActivity;


public class Login extends AppCompatActivity {
    TextView inputname;
    Button login;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);

        inputname = findViewById(R.id.editTextTextPersonName);
        login=findViewById(R.id.Login);
         name = inputname.getText().toString();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, GroupChatActivity.class);
                intent.putExtra("name",inputname.getText().toString());
                startActivity(intent);
            }
        });

    }
}
