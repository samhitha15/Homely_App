package com.example.listofitems;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
//import android.widget.Toast;


public class Payment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button b1,b2;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        //final Random random = new Random();
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        //final TextView textGenerateNumber = (TextView)findViewById(R.id.cal);
        //Button amountb = (Button)findViewById(R.id.amt);
        //amountb.setOnClickListener(new View.OnClickListener() {
            /*@Override
            public void onClick(View v) {
                textGenerateNumber.setText(new StringBuilder().append("$").append(String.valueOf(random.nextInt(150 - 50) + 50)).toString());
            }*/
        Intent i=getIntent();
        String receivingdata= i.getStringExtra("del_cost");
        TextView tv=(TextView)findViewById(R.id.cal);
        tv.setText(receivingdata);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }


        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityCard();
            }
        });

    }

    private void openActivityCard() {
        Intent intent = new Intent(this, CardActivity.class);
        startActivity(intent);

    }

    private void openActivity2() {
        Intent intent = new Intent(Payment.this, Cod.class);
        startActivity(intent);
    }


}

