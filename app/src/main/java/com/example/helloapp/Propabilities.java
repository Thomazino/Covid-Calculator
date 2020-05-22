package com.example.helloapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Propabilities extends AppCompatActivity {
   private TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propabilities);
        txt=(TextView)findViewById(R.id.textView);
        DatabaseAccess dt=DatabaseAccess.getInstance(getApplicationContext());
        dt.open();
        String[] infos=dt.getInfoForCountry("Chad");
        String deathrate=dt.getDeathRateDisease("Cancer");
        String[][] Cases=dt.CasesData(45,"Male");
        String[][] GeneralCases=dt.GeneralCasesData();
        String[][] GeneralCasesOnSexes=dt.GeneralCasesOnSexesData("Male Total");
        String[] Countries=dt.getCountries();
        txt.setText(Countries[176]);
        dt.close();
    }
}
