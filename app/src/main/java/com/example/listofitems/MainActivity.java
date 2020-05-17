package com.example.listofitems;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    GridView gridView;
    String[] list = {"Books/Documents/Files","Medicines","Food","Electronic Gadgets","Gifts","Others"};



    int[] listImg = {R.drawable.books1,R.drawable.medicines1,R.drawable.food1,R.drawable.electrinics1,R.drawable.gifts1,R.drawable.others};
String USERID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.grid_view);

        MainAdapter adapter = new MainAdapter(MainActivity.this,list,listImg);
        gridView.setAdapter(adapter);
        Intent in=getIntent();
         USERID=in.getStringExtra("USERID");
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getApplicationContext(),Details.class);
                intent.putExtra("name",list[position]);
                intent.putExtra("image",listImg[position]);
                intent.putExtra("USERID",USERID);
                startActivity(intent);

            }


        });

        FirebaseAuth.getInstance().signOut();
    }


}
