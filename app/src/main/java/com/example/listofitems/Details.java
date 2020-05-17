 package com.example.listofitems;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

 public class Details extends AppCompatActivity implements View.OnClickListener {
    Button button;
     DatabaseReference mDatabase;

     RadioGroup rg;
     RadioButton rb;

     EditText tv;
     EditText tv2;
     EditText et;
     EditText et2;

     String urgent;
     String requests;
     String deliveryTime;
     String altName;
     String altPh;
     String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        mDatabase = FirebaseDatabase.getInstance().getReference("details");

        rg = (RadioGroup)findViewById(R.id.radio);

        tv =(EditText) findViewById(R.id.deliverytime);

        tv2 =(EditText) findViewById(R.id.req);

        et =(EditText) findViewById(R.id.altname);

        et2 =(EditText) findViewById(R.id.altph);
        Intent i= getIntent();
        userid=i.getStringExtra("USERID");
        button = findViewById(R.id.r_submit);
        button.setOnClickListener(this);
        Button button;





    }



     @Override
     public void onClick(View v) {

         rb = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
         urgent = (String) rb.getText();
         deliveryTime = String.valueOf(tv.getText());
         requests = String.valueOf(tv2.getText());
         altName = String.valueOf(et.getText());
         altPh = String.valueOf(et2.getText());
         String id = mDatabase.push().getKey();
         DetailsStructure ds = new DetailsStructure(userid,urgent,deliveryTime,requests,altName,altPh,"","","");
         mDatabase.child(id).setValue(ds);
         Intent intent = new Intent(Details.this, MapsActivity.class);
         intent.putExtra("id",id);
         startActivity(intent);
     }
 }

