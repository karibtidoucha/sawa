package com.hackathon.sha3by.sha3by;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
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
import android.text.Editable;

import com.google.firebase.database.Query;


import android.view.WindowManager;

public class Activity3 extends AppCompatActivity {

    private String lastText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String userName = sharedPref.getString("Name", "");


        setContentView(R.layout.activity3);
        Button fab = findViewById(R.id.b_send);


        final EditText input = (EditText) findViewById(R.id.input);
        input.addTextChangedListener(new TextWatcher() {


            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length()>3 && !(s.toString().equals(lastText) )){
                    lastText = s.toString();
                    Log.e("TEST", s.toString());
                    Spannable spannable=new SpannableString(s.toString());
                    spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, 2, 0);
                    input.setText(spannable);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!input.getText().toString().equals("")) {

                    MessageStore.getInstance().firebasedatabase.getReference("message").child("a").child("b")
                            .push()
                            .setValue(new Message(input.getText().toString())
                            );
                }

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
                            //push out a pop up
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


        final ListView messagelist = findViewById(R.id.messages);

        messagelist
                .setAdapter(firebaseListAdapter);

    }
}
