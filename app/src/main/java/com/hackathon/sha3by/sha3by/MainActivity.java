package com.hackathon.sha3by.sha3by;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.second_activity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Activity2.class));
            }
        });
    }

    public void addUser(){
        EditText name = (EditText) findViewById(R.id.personName);
        RadioButton ab = (RadioButton) findViewById(R.id.arabicButton);
        RadioButton eb = (RadioButton) findViewById(R.id.englishButton);

        MessageStore.getInstance().firebasedatabase.getReference("users").child("a")
                .push()
                .setValue(new User(name.getText().toString(), ab.isChecked(), eb.isChecked())
                );


    }
}
