package com.example.helloapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor c=null;

    private DatabaseAccess(Context context){
        this.openHelper=new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context){
        if (instance==null)
            instance=new DatabaseAccess(context);
        return instance;
    }

    public void open(){
        this.db=openHelper.getWritableDatabase();
    }

    public void close(){
        if(db!=null)
            this.db.close();
    }

      //επιστρεφει ολες τις πληροφοριες μιας χωρας που εχουμε στην βαση
    public String[] getInfoForCountry(String name){
        String[] Infos=new String[7];
        c=db.rawQuery("SELECT * FROM Countries WHERE Country='"+name+"'",new String[]{});
        for(int i=1;i<=7;i++) {
            while (c.moveToNext()) {
                String info = c.getString(i);
                if (info==null)
                    info="0";
                Infos[i-1]=info;
            }
            c=db.rawQuery("SELECT * FROM Countries WHERE Country='"+name+"'",new String[]{});
        }
        for(int i=0;i<7;i++)
            Infos[i]=Infos[i].replace(",","");

        return Infos;
    }
    //επιστρεφει ενα αθροιστικο deathrate με βαση του ποσες ασθενειες στελνουμε για να βγει
    // μια αυξημενη πιθανοτητα θανατου αναλογα με το πληθος των ασθενειων στο τελος
    public float getDeathRateDisease(String[] diseases,float userAge){
        float userDiseaseDeathRate=0;
        for (int i=0;i<diseases.length;i++) {
            c = db.rawQuery("SELECT Death_Rate FROM Diseases WHERE PreExistingDiseases='" + diseases[0] + "'", new String[]{});
            c.moveToNext();
            userDiseaseDeathRate+=Float.valueOf(c.getString(0))/(i+1);


        }
        return userDiseaseDeathRate;
    }
//επιστρεφει εναν πινακα 2d με δεδομενα αναλογα την ηλικια και το φυλο μεσα απο την βαση
    public String[][] CasesData(int Age,String Sex){
        String[][] Datas=new String[54][6];
        String age;
        if(Age==0)
            age="Under 1 year";
        else if(Age>=1 && Age<=4)
            age="1-4 years";
        else if(Age>=5 && Age<=14)
            age="5-14 years";
        else if(Age>=15 && Age<=24)
            age="15-24 years";
        else if(Age>=25 && Age<=34)
            age="25-34 years";
        else if(Age>=35 && Age<=44)
            age="35-44 years";
        else if(Age>=45 && Age<=54)
            age="45-54 years";
        else if(Age>=55 && Age<=64)
            age="55-64 years";
        else if(Age>=65 && Age<=74)
            age="65-74 years";
        else if(Age>=75 && Age<=84)
            age="75-84 years";
        else
            age="85 years and over";

        c=db.rawQuery("SELECT * FROM CasesMeasurements WHERE Age_group='"+age+"' and SEX='"+Sex+"'",new String[]{});
        for(int i=2;i<=7;i++) {
            int j=0;
            while (c.moveToNext()) {
                String data = c.getString(i);
                if (data==null)
                    data="0";
                Datas[j][i-2]=data;
                j++;
            }
            c=db.rawQuery("SELECT * FROM CasesMeasurements WHERE Age_group='"+age+"' and SEX='"+Sex+"'",new String[]{});
        }
        return Datas;
    }

    public String[][] GeneralCasesData(){
        String[][] Datas=new String[54][6];
        c=db.rawQuery("SELECT * FROM CasesMeasurements WHERE Sex='All sexes'",new String[]{});
        for(int i=2;i<=7;i++) {
            int j=0;
            while (c.moveToNext()) {
                String data = c.getString(i);
                if (data==null)
                    data="0";
                Datas[j][i-2]=data;
                j++;

            }
            c=db.rawQuery("SELECT * FROM CasesMeasurements WHERE Sex='All sexes'",new String[]{});
        }
        return Datas;
    }
//επιστρεφει δεδομενα αναλογα το φυλο οποτε αποκταται απλα μια γενικη εικονα για το φυλο που σταλθηκε ως ορισμα
    public String[][] GeneralCasesOnSexesData(String sex){
        String[][] Datas=new String[54][6];
        c=db.rawQuery("SELECT * FROM CasesMeasurements WHERE Sex='"+sex+" Total'",new String[]{});
        for(int i=2;i<=7;i++) {
            int j=0;
            while (c.moveToNext()) {
                String data = c.getString(i);
                if (data==null)
                    data="0";
                Datas[j][i-2]=data;
                j++;
            }
            c=db.rawQuery("SELECT * FROM CasesMeasurements WHERE Sex='"+sex+" Total'",new String[]{});
        }
        return Datas;
    }

    //επιστρεφει ολες τις χωρες που υπαρχουν στην βαση
    public String[] getCountries(){
        String[] Countries=new String[177];
        c=db.rawQuery("SELECT Country FROM Countries",new String[]{});
        int i=0;
        while (c.moveToNext()) {
            String cntr = c.getString(0);
            Countries[i]=cntr;
            i++;
        }
        return Countries;
    }
   //επιστρεφει το αθροισμα των παθοντων του κορονοιου απο ολες τις χωρες
    public String getTotalCases(){
        c = db.rawQuery("SELECT SUM(Total_Cases) FROM Countries",new String[]{});
        c.moveToNext();
        return c.getString(0);

    }
    //επιστρεφει το αθροισμα των νεκρων του κορονοιου απο ολες τις χωρες
    public String getTotalDeaths(){
        c = db.rawQuery("SELECT SUM(Total_Deaths)FROM Countries",new String[]{});
        c.moveToNext();
        return c.getString(0);

    }


}

