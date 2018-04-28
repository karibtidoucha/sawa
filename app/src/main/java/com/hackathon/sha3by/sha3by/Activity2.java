package com.hackathon.sha3by.sha3by;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;

public class Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*List<User> users =new ArrayList<>();
        users.add(new User("Mohamed",true,false,1));
        users.add(new User("Ferida",false,true,0));
        users.add(new User("Ling",false,true,1));
        User self=new User ("Farah",false,true,0);

        for (int i =0; i<=3; i++){
            if(users[i].ab==self.ab){

            }

        }
*/

        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity2);

        Button button = findViewById(R.id.third_activity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity2.this, Activity3.class));
            }
        });
    }
}
