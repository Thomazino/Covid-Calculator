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
import java.util.ArrayList;

public class UserData extends AppCompatActivity {
    private TextView txt;
    private Button Start;
    private String[] userData={"-1","-1","-1"};
    private RadioGroup rg;
    private RadioButton rb;
    private String[] userDiseases;
    private int radioid;
    private EditText edit;
    boolean Redwarning=false;
    Spinner Countries;
    MultiSelectionSpinner Diseases;
    String[] dis={"Cardiovascular disease","Diabetes","Chronic respiratory disease","Hypertension","Cancer"};
    String[] cntrs;
    ArrayAdapter<String> ad2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        DatabaseAccess dt=DatabaseAccess.getInstance(getApplicationContext());
        ArrayList<Item> items = new ArrayList<>();
        for(int i=0;i<dis.length;i++)
            items.add(new Item(dis[i],i));

        Diseases=(MultiSelectionSpinner) findViewById(R.id.spinner);
        Diseases.setItems(items);
        rg=(RadioGroup)findViewById(R.id.rd);

        dt.open();
        Countries=(Spinner)findViewById(R.id.spinner1);
        cntrs=dt.getCountries();
        ad2=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,cntrs);
        Countries.setAdapter(ad2);
        Countries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userData[2]=cntrs[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                userData[2]="-1";
            }
        });
        edit=(EditText)findViewById(R.id.editText);
        edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction()==KeyEvent.ACTION_DOWN && keyCode==KeyEvent.KEYCODE_ENTER){
                    if(NotInteger(""+edit.getText()) || Integer.parseInt(""+edit.getText())>110) {
                        edit.setText("");
                        userData[1]="-1";
                    }
                    else
                        userData[1]=""+edit.getText();
                    return true;
                }
                return false;
            }
        });
        Start=(Button)findViewById(R.id.button11);
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NotInteger(""+edit.getText()) || Integer.parseInt(""+edit.getText())>110) {
                    edit.setText("");
                    userData[1]="-1";
                }
                else
                    userData[1]=""+edit.getText();
                radioid=rg.getCheckedRadioButtonId();
                if (radioid!=-1) {
                    rb = (RadioButton) findViewById(radioid);
                    userData[0]=""+rb.getText();
                }
                else
                    userData[0]="-1";
                if(Diseases.getSelectedItems().size()>0){
                    userDiseases=new String[Diseases.getSelectedItems().size()];
                    for(int i=0;i<Diseases.getSelectedItems().size();i++)
                        userDiseases[i]=Diseases.getSelectedItems().get(i).getName();

                }
                else{
                    userDiseases=new String[1];
                    userDiseases[0]="None";
                }
                if(Start(userData)) {
                    if(Redwarning){
                        Redwarning=false;
                        txt = (TextView) findViewById(R.id.textView6);
                        txt.setVisibility(View.INVISIBLE);
                    }
                    GotoActivity(Probabilities.class,userData,userDiseases);
                    /** ΕΔΩ ΑΛΛΑΖΕΙΣ ACTIVITY ΚΑΙ ΣΤΕΛΝΕΙΣ ΤΟΝ ΠΙΝΑΚΑ UserDatas
                     * ΚΑΙ ΣΤΕΛΝΕΙΣ ΚΑΙ ΤΟ SendedDiseases*/
                }
                else{
                    if(!Redwarning) {
                        Redwarning=true;
                        txt = (TextView) findViewById(R.id.textView6);
                        txt.setVisibility(View.VISIBLE);
                    }
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

    public void GotoActivity(Class Activity,String[] userData,String[] userDiseases){
        Intent intent=new Intent(this,Activity);
        Bundle args = new Bundle();
        args.putSerializable("STRING",(Serializable)userData);
        args.putSerializable("STRING DISEASES",(Serializable)userDiseases);
        intent.putExtra("BUNDLE",args);
        startActivity(intent);
    }
}