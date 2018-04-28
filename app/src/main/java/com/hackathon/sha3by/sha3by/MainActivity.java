package com.hackathon.sha3by.sha3by;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.second_activity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Activity2.class));
                addUser();

            }
        });


    }

    public void addUser(){
        EditText name = (EditText) findViewById(R.id.personName);
        RadioButton ab = (RadioButton) findViewById(R.id.arabicButton);

        RadioButton b4 = (RadioButton) findViewById(R.id.radioButton4);

        MessageStore.getInstance().firebasedatabase.getReference("users").push()
                .setValue(new User(name.getText().toString(), ab.isChecked() ? 1 : 0 , b4.isChecked() ? 0 :1   )
                );
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Name", name.getText().toString());
        editor.commit();

    }
}
