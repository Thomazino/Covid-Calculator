package com.example.helloapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class Links extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);
        //ενωση καθε textview με το σωστο λινκ ωστε να μπορει να επισκεφτει ο χρηστης τους ιστοχωρους
        //που δημιουργηθηκε η βαση
        TextView link1=(TextView)findViewById(R.id.link1);
        link1.setMovementMethod(LinkMovementMethod.getInstance());
        TextView link2=(TextView)findViewById(R.id.link2);
        link2.setMovementMethod(LinkMovementMethod.getInstance());
        TextView link3=(TextView)findViewById(R.id.link3);
        link3.setMovementMethod(LinkMovementMethod.getInstance());
        TextView link4=(TextView)findViewById(R.id.link4);
        link4.setMovementMethod(LinkMovementMethod.getInstance());
        TextView link5=(TextView)findViewById(R.id.link5);
        link5.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
