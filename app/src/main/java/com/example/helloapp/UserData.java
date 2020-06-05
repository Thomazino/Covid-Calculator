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
    private String[] userData={"-1","-1","-1"}; //για να γινει το start πρεπει ολα να εχουν διαφορετικη τιμη απο -1 στο τελος
    //o πινακας userData αντιπροσωπευει τον πινακα των τελικων δεδομενων εκτος των ασθενειων που θα μεταφερθουν στην κλαση
    //propabilities για να υπολογιστουν ολα τα στατιστικα.
    private RadioGroup rg;
    private RadioButton rb;
    private String[] userDiseases; // αντιπροσωπευει τις ασθενειες που τελικα θα δηλωσει ο χρηστης(μπορει και καμια).
    private int radioid;
    private EditText edit;
    boolean Redwarning=false;
    Spinner Countries;
    MultiSelectionSpinner Diseases;
    String[] dis={"Cardiovascular disease","Diabetes","Chronic respiratory disease","Hypertension","Cancer"}; //οι ασθενειες που υπαρχουν στην βαση
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
                userData[2]="-1"; //απλα για σιγουρια αν και δεν γινεται να μην υπαρχει επιλεγμενη χωρα εστω by default
            }
        });
        edit=(EditText)findViewById(R.id.editText);
        edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction()==KeyEvent.ACTION_DOWN && keyCode==KeyEvent.KEYCODE_ENTER){
                    if(NotInteger(""+edit.getText()) || Integer.parseInt(""+edit.getText())>110) {
                        //αμα δεν δωσει ακεραιο θετικο αριθμο ή αμα δωσει και ειναι μεγαλυτερος του 110
                        //το edittext γινεται κενο και το userData[1] που ειναι η ηλικια γινεται -1
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
                //γενικος ελεγχος αν ολα τα δεδομενα ειναι σωστα. Αν δεν ειναι δεν αλλαζει το activity
                //και βγαινει κοκκινη ενδειξη
                if(NotInteger(""+edit.getText()) || Integer.parseInt(""+edit.getText())>110) {
                    //ελεγχος παλι για την ηλικια γτ αμα δεν πατηθει το τικ στο πληκτρολογιο το userData[1]
                    //δεν θα ειναι -1 οποτε πρεπει να αλλαξει και αυτο θα γινει αν παει στο else.
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
                    //αν εχει επιλεχθει εστω και 1 ασθενεια τοτε ο πινακας userDiseases
                    //αποθηκευει τα ονοματα αυτων των ασθενειων
                    userDiseases=new String[Diseases.getSelectedItems().size()];
                    for(int i=0;i<Diseases.getSelectedItems().size();i++)
                        userDiseases[i]=Diseases.getSelectedItems().get(i).getName();

                }
                else{
                    //αν δεν εχει δωθει καμια ασθενεια τοτε παιρνει ο πινακας την μοναδικη τιμη None
                    userDiseases=new String[1];
                    userDiseases[0]="None";
                }
                if(Start(userData)) {
                    if(Redwarning){
                        //αμα ετυχε ο χρηστης και εδωσε λαθος δεδομενα πριν δωσει σωστα
                        //τοτε εξαφανιζουμε το redwarning ωστε αμα ο χρηστης θελησει να γυρισει
                        //πισω στο activity με το backbutton του κινητου να μην υπαρχει καποια ανουσια
                        //ενδειξη οτι εκανε καποιο λαθος ενω καλα καλα δεν εισηγαγε νεα δεδομενα
                        Redwarning=false;
                        txt = (TextView) findViewById(R.id.textView6);
                        txt.setVisibility(View.INVISIBLE);
                    }
                    GotoActivity(Probabilities.class,userData,userDiseases);

                }
                else{
                    if(!Redwarning) {
                        //βγαινει exception αν κανω set Visible κατι που ειναι ηδη visible
                        //οποτε πρεπει να ελεγχουμε αν ειναι αορατο το red warning αν ειναι
                        //αυτο σημαινει οτι το boolean Redwarning ειναι false αλλα οταν θα μπει
                        //πρωτη φορα στην if γινεται true οποτε απλα οταν ξαναδινει ο χρηστης λαθος
                        //δεδομενα το redwarning παραμενει σταθερα εμφανισμενο.
                        Redwarning=true;
                        txt = (TextView) findViewById(R.id.textView6);
                        txt.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        dt.close();

    }



// συναρτηση που παιρνει ορισμα String και ελεγχει αν αυτο το string αποτελειται μονο απο τα chars 0,1,2,3,4,5,6,7,8,9.
 //Αν αποτελειται μονο απο αυτα τα chars τοτε ειναι ενας ακεραιος θετικος αριθμος ή μηδεν προφανως. Επισης στην for
 //δεν μπαινει αν ειναι κενο το num οποτε με το return num.length==0 αμα ειναι κενο string θα επιστρεψει true
    //ενω σε οποιαδηποτε αλλη περιπτωση ειναι σιγουρα ακεραιος αρα επιστρεφει false.
    boolean NotInteger(String num){
        char a;
        for(int i=0;i<num.length();i++){
            a=num.charAt(i);
            if(a!='0' && a!='1' && a!='2' && a!='3' && a!='4' && a!='5' && a!='6' && a!='7' && a!='8' && a!='9' )
                return true;
        }
        return (num.length()==0);
    }
//ελεγχει αν σε εναν πινακα ολα τα στοιχεια του διαφερουν απο το -1. Αν διαφερουν επιστρεφει true αλλιως false
    //ειναι η συναρτηση που θα καθορισει αν θα αλλαξει το activity ή οχι εν τελει
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