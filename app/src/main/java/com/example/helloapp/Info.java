package com.example.helloapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class Info extends AppCompatActivity {
   private Button Question1,Question2,Question3,Question4,Question5,Question6; //οι ερωτησεις που μπορει να κανει ο χρηστης
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos);
        Question1=(Button)findViewById(R.id.button3);
        Question1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView scroll=(ScrollView) findViewById(R.id.scrollView1);
                scroll.fullScroll(ScrollView.FOCUS_UP);
                TextView cloud=(TextView) findViewById(R.id.text1);
                cloud.setText("COVID-19 is caused by a coronavirus called SARS-CoV-2. Coronaviruses are a large family of viruses that are common in people and may different species of animals, including camels, cattle, cats, and bats.  Rarely, animal coronaviruses can infect people and then spread between people. This occurred with MERS-CoV and SARS-CoV, and now with the virus that causes COVID-19. The SARS-CoV-2 virus is a betacoronavirus, like MERS-CoV and SARS-CoV. All three of these viruses have their origins in bats.However, the exact source of this virus is unknown.");
                cloud.setLines(27);

            }
        });

        Question2=(Button)findViewById(R.id.button4);
        Question2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView scroll=(ScrollView) findViewById(R.id.scrollView1);
                scroll.fullScroll(ScrollView.FOCUS_UP);
                TextView cloud=(TextView) findViewById(R.id.text1);
                cloud.setText("The virus that causes COVID-19 is thought to spread mainly from person to person, mainly through respiratory droplets produced when an infected person coughs or sneezes. These droplets can land in the mouths or noses of people who are nearby or possibly be inhaled into the lungs. Spread is more likely when people are in close contact with one another .COVID-19 seems to be spreading in the community spread in many affected geographic areas that people have been infected with the virus, including some who are not sure how or where they became infected.");
                cloud.setLines(27);
            }
        });

        Question3=(Button)findViewById(R.id.button5);
        Question3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView scroll=(ScrollView) findViewById(R.id.scrollView1);
                scroll.fullScroll(ScrollView.FOCUS_UP);
                TextView cloud=(TextView) findViewById(R.id.text1);
                cloud.setText("Generally coronaviruses survive for shorter periods at higher temperatures and higher humidity than in cooler or dryer environments. However, we don’t have direct data for this virus, nor do we have direct data for a temperature-based cutoff for inactivation at this point. The necessary temperature would also be based on the materials of the surface, the environment, etc.It is not yet known either if the weather and temperature affect the spread of COVID-19 like cold and flu.There is much more to learn about the transmissibility, severity, and other features associated with COVID-19.");
                cloud.setLines(27);
            }
        });

        Question4=(Button)findViewById(R.id.button6);
        Question4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView scroll=(ScrollView) findViewById(R.id.scrollView1);
                scroll.fullScroll(ScrollView.FOCUS_UP);
                TextView cloud=(TextView) findViewById(R.id.text1);
                cloud.setText("Wash your hands often with soap and water for at least 20 seconds especially after you have been in a public place, or after blowing your nose, coughing, or sneezing.Avoid close contact with people who are sick, even inside your home.Also,put distance between yourself and other people outside of your home.Cover your mouth and nose with a cloth face cover when around others.For the end,clean AND disinfect frequently touched surfaces daily. This includes tables, doorknobs, light switches, countertops, handles, desks, phones, keyboards, toilets, faucets, and sinks.");
                cloud.setLines(27);
            }
        });

        Question5=(Button)findViewById(R.id.button7);
        Question5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView scroll=(ScrollView) findViewById(R.id.scrollView1);
                scroll.fullScroll(ScrollView.FOCUS_UP);
                TextView cloud=(TextView) findViewById(R.id.text1);
                cloud.setText("Not everyone needs this test.Most people will have mild illness and can recover at home without medical care and may not need to be tested.Decisions about testing are made by state and localexternal icon health departments or healthcare providers. If you have symptoms of COVID-19 and want to get tested, call your healthcare provider first. You can also visit your state or local health department’s website to look for the latest local information on testing. Although supplies of tests are increasing, it may still be difficult to find a place to get tested.");
                cloud.setLines(27);
            }
        });

        Question6=(Button)findViewById(R.id.button8);
        Question6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView scroll=(ScrollView) findViewById(R.id.scrollView1);
                scroll.fullScroll(ScrollView.FOCUS_UP);
                TextView cloud=(TextView) findViewById(R.id.text1);
                cloud.setText("If you are sick,stay always at home unless there is a serious reason to go out.Older adults and people of any age with serious underlying medical conditions should call a health care provider as soon as symptoms start.Separate yourself from other people in your home. Avoid sharing household items,such as cups and towels.Wash items thoroughly after using them with soap and water.Disinfect high-touch surfaces daily in your sick room and bathroom. Have a healthy household member do the same for surfaces in other parts of the home and bring whatever you need from the outside.");
                cloud.setLines(27);
            }
        });


    }
}
