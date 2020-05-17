package com.example.listofitems;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Cod extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button b,newb;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cod);

        b = findViewById(R.id.OK);
        newb = findViewById(R.id.button3);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();
            }

        });

        newb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity4();
            }
        });
    }

    private void openActivity4() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    private void openActivity3() {
        Intent intent = new Intent(this, RatingActivity.class);
        startActivity(intent);
    }


}
