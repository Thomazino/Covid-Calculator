package com.example.helloapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;
import java.math.BigDecimal;

public class UserData extends AppCompatActivity {
    private TextView txt;
    private Button Start;
    private String[] UserDatas={"-1","-1","-1","-1"};
    private RadioGroup rg;
    private RadioButton rb;
    private int radioid;
    private EditText edit;
    Spinner Diseases,Countries;
    String[] dis={"None","Cardiovascular disease","Diabetes","Chronic respiratory disease","Hypertension","Cancer"};
    String[] cntrs;
    ArrayAdapter<String> ad1,ad2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        DatabaseAccess dt=DatabaseAccess.getInstance(getApplicationContext());
        Diseases=(Spinner)findViewById(R.id.spinner);
        rg=(RadioGroup)findViewById(R.id.rd);
        ad1=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dis);
        Diseases.setAdapter(ad1);
        Diseases.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UserDatas[2]=dis[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                UserDatas[2]="-1";
            }
        });
        dt.open();
        Countries=(Spinner)findViewById(R.id.spinner1);
        cntrs=dt.getCountries();
        ad2=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,cntrs);
        Countries.setAdapter(ad2);
        Countries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UserDatas[3]=cntrs[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                UserDatas[3]="1";
            }
        });
        edit=(EditText)findViewById(R.id.editText);
        edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction()==KeyEvent.ACTION_DOWN && keyCode==KeyEvent.KEYCODE_ENTER){
                    if(NotInteger(""+edit.getText()) || Integer.parseInt(""+edit.getText())>120) {
                        edit.setText("");
                        UserDatas[1]="-1";
                    }
                    else
                        UserDatas[1]=""+edit.getText();
                    return true;
                }
                return false;
            }
        });
        Start=(Button)findViewById(R.id.button11);
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioid=rg.getCheckedRadioButtonId();
                if (radioid!=-1) {
                    rb = (RadioButton) findViewById(radioid);
                    UserDatas[0]=""+rb.getText();
                }
                else
                    UserDatas[0]="-1";
                if(Start(UserDatas)) {
                    if(UserDatas[0].equals("None"))
                        UserDatas[0]=null;

                    GotoActivity(Probabilities.class,UserDatas);
                    /** ΕΔΩ ΑΛΛΑΖΕΙΣ ACTIVITY ΚΑΙ ΣΤΕΛΝΕΙΣ ΤΟΝ ΠΙΝΑΚΑ UserDatas*/
                }
                else{
                    txt=(TextView)findViewById(R.id.textView6);
                    txt.setVisibility(View.VISIBLE);
                }
            }
        });


        dt.close();

    }




    boolean NotInteger(String num){
        char a;
        for(int i=0;i<num.length();i++){
            a=num.charAt(i);
            if(a!='0' && a!='1' && a!='2' && a!='3' && a!='4' && a!='5' && a!='6' && a!='7' && a!='8' && a!='9' )
                return true;
        }
        return (num.length()==0);
    }

    boolean Start(String[] dts){
        for(int i=0;i<dts.length;i++){
            if(dts[i].equals("-1"))
                return false;
        }
        return true;
    }

    public void GotoActivity(Class Activity,String[] userData){
        Intent intent=new Intent(this,Activity);
        Bundle args = new Bundle();
        args.putSerializable("STRING",(Serializable)userData);
        intent.putExtra("BUNDLE",args);
        startActivity(intent);
    }
}