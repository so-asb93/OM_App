package com.example.om_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class JoueursDetails extends AppCompatActivity {
    TextView name,position,number,nationality,transfer,age,height,end;

    String intent_name,intent_position,intent_number,intent_nationality,intent_transfer,intent_age,intent_height,intent_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joueurs_details);
        Intent intent = getIntent();

        name = findViewById(R.id.name);
        position = findViewById(R.id.position);
        number = findViewById(R.id.number);
        nationality = findViewById(R.id.nationality);
        transfer = findViewById(R.id.transfer);
        age = findViewById(R.id.age);
        height = findViewById(R.id.taille);
        end = findViewById(R.id.end);



        intent_name = intent.getStringExtra("name");
        intent_position = intent.getStringExtra("position");
        //intent_number = intent.getStringExtra("number");
        intent_number = String.valueOf(intent.getIntExtra("number", 0));
        intent_nationality = intent.getStringExtra("nationality");
        intent_transfer = intent.getStringExtra("transfer");
        intent_age = intent.getStringExtra("age");
        intent_height = intent.getStringExtra("height");
        intent_end = intent.getStringExtra("end");


        name.setText(intent_name);
        position.setText(intent_position);
        number.setText(intent_number);
        nationality.setText(intent_nationality);
        transfer.setText(intent_transfer);
        age.setText(intent_age);
        height.setText(intent_height);
        end.setText(intent_end);

    }

}