package com.hackathon.sha3by.sha3by;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormat;

import android.view.Window;
import android.view.WindowManager;

public class Activity3 extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        //Remove title bar
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String userName = sharedPref.getString("Name", "");


        setContentView(R.layout.activity3);
        Button fab =
                (Button) findViewById(R.id.b_send);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) findViewById(R.id.input);


                MessageStore.getInstance().firebasedatabase.getReference("message").child("a").child("b")
                        .push()
                        .setValue(new Message(input.getText().toString())
                        );

                // Clear the input
                input.setText("");
            }
        });


        Query query = MessageStore.getInstance().firebasedatabase.getReference("message").child("a").child("b");


        FirebaseListOptions<Message> firebaseListOptions = new FirebaseListOptions.Builder<Message>()
                .setQuery(query, Message.class).setLayout(R.layout.message).setLifecycleOwner(this).build();

        ListAdapter firebaseListAdapter = new FirebaseListAdapter<Message>(firebaseListOptions) {
            @Override
            protected void populateView(View v, Message model, int position) {
                // Get references to the views of message.xml
//                TextView messageText = (TextView) v.findViewById(R.id.message_text);
//                // Set their text
//                messageText.setText(model.messageText);


                String[] messageSplit = model.messageText.split(" ");
                SpannableString ss = new SpannableString(model.messageText);


                TextView tv = v.findViewById(R.id.tv);


                int counter =0;
                for (String i : messageSplit) {
                    final String word = i;
                    ss.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            Log.e("word",word);
                        }
                        @Override
                        public void updateDrawState(TextPaint ds) {
                            ds.setUnderlineText(false);
                        }
                    }, counter,counter+i.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    counter+=i.length()+1;
                }

                tv.setText(ss);
                tv.setMovementMethod(LinkMovementMethod.getInstance());

            }




        };


        final ListView messagelist = (ListView) findViewById(R.id.messages);

        messagelist
                .setAdapter(firebaseListAdapter);


    }
}

//
//    SpannableString ss = new SpannableString("Android is a Software stack");
//    ClickableSpan clickableSpan = new ClickableSpan() {
//        @Override
//        public void onClick(View textView) {
//            startActivity(new Intent(MyActivity.this, NextActivity.class));
//        }
//    };
//    ss.setSpan(clickableSpan,22,27,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
////where 22 and 27 are the starting and ending index of the String. Now word stack is clickable
//// onClicking stack it will open NextActiivty
//
//            TextView textView=(TextView)findViewById(R.id.hello);
//            textView.setText(ss);
//            textView.setMovementMethod(LinkMovementMethod.getInstance());



