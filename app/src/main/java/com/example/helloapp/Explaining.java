package com.example.helloapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Explaining extends AppCompatActivity {
    private Button GO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explaining);
        GO=(Button)findViewById(R.id.button9);
        GO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoActivity(UserData.class);
            }
        });
    }
    public void GotoActivity(Class Activity){
        Intent intent=new Intent(this,Activity);
        startActivity(intent);
    }
}
