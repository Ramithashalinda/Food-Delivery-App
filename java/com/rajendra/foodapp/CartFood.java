package com.rajendra.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rajendra.foodapp.adapter.Bill_info;
import com.rajendra.foodapp.adapter.MapActivity2;
import com.rajendra.foodapp.adapter.ProgramAdapter;

import java.util.ArrayList;

public class CartFood extends AppCompatActivity {

    private TextView textadress,textP,headers,textD,textS;
    private ListView LvProgram;
    private static int size=Bill_info.numberOfqueue;
     public  static   String[] programName=new  String[10];
    public static  String[] programDescription=new  String[10];
    public static int[] programImages=new int[10];

    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






        mFirestore = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_cart_food);
        headers = (TextView) findViewById(R.id.headertext);
        textadress = (TextView) findViewById(R.id.locationtext);

        textP = (TextView) findViewById(R.id.totalRS);
        textD = (TextView) findViewById(R.id.deliveryRS);
        textS = (TextView) findViewById(R.id.subtotalRs);

        LvProgram = (ListView) findViewById(R.id.lvProgram);
        ImageButton BackBtn = (ImageButton)findViewById(R.id.backbtn);
        Button btntoMap = (Button)findViewById(R.id.change_btn);
        Button orderBTN = (Button)findViewById(R.id.Orderbtn);
        ProgramAdapter programAdapter = new ProgramAdapter(this,programName,programImages,programDescription);
        LvProgram.setAdapter(programAdapter);

        textP.setText(String.valueOf(Bill_info.RSTot));
        textS.setText(String.valueOf(Bill_info.RSSub));
        textD.setText(String.valueOf(Bill_info.RSDel));
if(MapActivity2.locChecker==true){

    textadress.setText( MapActivity2.LocAddress);

}

BackBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(CartFood.this, MainActivity.class);
        startActivity(intent);
    }
});

       btntoMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CartFood.this, MapActivity2.class);
                startActivity(intent);
            }
        });
        orderBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(MapActivity2.locChecker==true){
                    Bill_info.billItm.put("Total price",String.valueOf(Bill_info.RSTot));
                    mFirestore.collection("Orders").add(Bill_info.billItm).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            Toast.makeText(CartFood.this,"Order confirmed successfully✔",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String error= e.getMessage();
                            Toast.makeText(CartFood.this,"Error:"+e,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {

                    Toast.makeText(CartFood.this,"Location must need to add ⚠",Toast.LENGTH_SHORT).show();
                }


            }
        });




    }



}