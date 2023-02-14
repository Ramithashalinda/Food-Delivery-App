package com.rajendra.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.rajendra.foodapp.adapter.Bill_info;
import com.rajendra.foodapp.adapter.MapActivity2;
import com.rajendra.foodapp.adapter.PopularFoodAdapter;
import com.rajendra.foodapp.model.PopularFood;


public class DetailsActivity extends AppCompatActivity{

    TextView textViewname,textViewprice,textquantity;
    ImageView imageView;
    public  boolean clearcrt=true;
    ImageButton backbutton,plsebtn,minsebtn,cartBtn;
    float itemprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        imageView =findViewById(R.id.imageView8);
        textViewname =findViewById(R.id.textView11);
        textViewprice =findViewById(R.id.totalprice);
        String nametext= PopularFoodAdapter.nameV;
        final String price=PopularFoodAdapter.priceV;
        textViewprice.setText(price);
        Resources resources = getResources();
        textViewname.setText(nametext);

        final int resourceId = PopularFoodAdapter.imgV;
        imageView.setImageResource(resourceId);
        cartBtn=findViewById(R.id.addCartbtn);


        backbutton=findViewById(R.id.imageView5);
        plsebtn=findViewById(R.id.pluse);
        minsebtn=findViewById(R.id.minse);
        textquantity=findViewById(R.id.quantity);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        plsebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x = textquantity.getText().toString();
                String x3 = textViewprice.getText().toString();
                int x1 =Integer.parseInt(x.toString());
                x1 = x1 + 1;
                itemprice  =Integer.parseInt(x3.toString());
                itemprice=itemprice*x1;
                Bill_info.RSTot=x1;
                Bill_info.RSTot=itemprice;

                textViewprice.setText(x3);
                textquantity.setText(String.valueOf(x1));
            }
        });
        minsebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x = textquantity.getText().toString();
                String x3 = textViewprice.getText().toString();
                int x1 =Integer.parseInt(x.toString());
                x1 = x1 - 1;
                itemprice  =Integer.parseInt(x3.toString());
                itemprice=itemprice*x1;
                Bill_info.RSTot=x1;
                Bill_info.RSTot=itemprice;

                textViewprice.setText(String.valueOf(x3));
                textquantity.setText(String.valueOf(x1));
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (clearcrt==false){

                }
                else {
                    Bill_info.billItm.put(textViewname.getText().toString(), textquantity.getText().toString());

                    CartFood.programName[Bill_info.numberOfqueue] = textViewname.getText().toString();
                    CartFood.programDescription[Bill_info.numberOfqueue] = textquantity.getText().toString();
                    CartFood.programImages[Bill_info.numberOfqueue] = resourceId;
                    Bill_info.numberOfqueue++;
                    clearcrt = false;
                    new AlertDialog.Builder(DetailsActivity. this )
                            .setMessage( "Item add to cart" )
                            .setPositiveButton( "OK"  , new
                                    DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick (DialogInterface paramDialogInterface , int paramInt) {
                                            Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    })
                            .setNegativeButton(android.R.string.cancel, null)
                            .show();


                }


            }
        });


    }
}
