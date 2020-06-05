package com.example.helloapp;
//βοηθητικη κλαση item για το MultiSelection Spinner που αντιπροσωπευει μια απο τις πολλες επιλογες που μπορει
//να εχει
public class Item {
    private String name;
    private int value;
    Item(String name,int value ){
        this.name=name;
        this.value=value;
    }

    public String getName() {
        return name;
    }

    public int getValue(){
        return value;
    }
}