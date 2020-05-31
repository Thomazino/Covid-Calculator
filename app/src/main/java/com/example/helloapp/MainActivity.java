package com.example.helloapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
   private Button Info,StatCalc,Datas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Info =(Button)findViewById(R.id.button);
        Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              GotoActivity(com.example.helloapp.Info.class);
            }
        });
        StatCalc =(Button)findViewById(R.id.button2);
        StatCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoActivity(Explaining.class);
            }
        });
        Datas=(Button)findViewById(R.id.button10);
        Datas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoActivity(Links.class);
            }
        });

    }
    public void GotoActivity(Class Activity){
        Intent intent=new Intent(this,Activity);
        startActivity(intent);
    }




}
