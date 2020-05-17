package com.example.listofitems;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.cardform.view.CardForm;

import java.util.Objects;

@SuppressWarnings("unused")
public class CardActivity extends AppCompatActivity {
    private Integer stationID;
    private Integer totalcost;
    private String FuelType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        Button pay;
        pay = findViewById(R.id.btnBuy);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openpay();
            }

        });



        stationID = getIntent().getIntExtra("stationID",0);
        totalcost = getIntent().getIntExtra("Totalcost",0);
        FuelType = getIntent().getStringExtra("FuelType");

        final CardForm cardForm = findViewById(R.id.card_form);
        Button rbtnCompletePayment = findViewById(R.id.btnBuy);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                //.postalCodeRequired(true)
                //.mobileNumberRequired(true)
                //.mobileNumberExplanation("SMS is required on this number")
                .setup(CardActivity.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);


        rbtnCompletePayment.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                if (cardForm.isValid()) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CardActivity.this);
                    alertBuilder.setTitle("Confirm before purchase");
                    final AlertDialog.Builder builder = alertBuilder.setMessage("Card number: " + cardForm.getCardNumber() + "\n" +
                                    "Card expiry date: " + Objects.requireNonNull(cardForm.getExpirationDateEditText().getText()).toString()
                            //+ "\n" + "Card CVV: " + cardForm.getCvv()
                            //+ "\n" +"Postal code: " + cardForm.getPostalCode() + "\n"
                            //+"Phone number: " + cardForm.getMobileNumber()
                    );
                    alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            //Toast.makeText(cardPayment.this, "Thank you for purchase", Toast.LENGTH_LONG).show();
                            Intent del = new Intent(CardActivity.this,Cod.class);
                            del.putExtra("Totalcost",totalcost);
                            del.putExtra("stationID",stationID);
                            del.putExtra("FuelType",FuelType);
                            startActivity(del);
                        }
                    });
                    alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();

                }else {
                    Toast.makeText(CardActivity.this, "Please fill all the details correctly!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void openpay() {
        Intent intent = new Intent(this, Cod.class);
        startActivity(intent);
    }
}
