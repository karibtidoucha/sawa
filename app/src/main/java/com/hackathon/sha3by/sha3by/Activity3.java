package com.hackathon.sha3by.sha3by;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
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

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity3);
        Button fab =
                (Button)findViewById(R.id.b_send);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);


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

        ListAdapter firebaseListAdapter = new FirebaseListAdapter<Message>(firebaseListOptions)
        {
            @Override
            protected void populateView(View v, Message model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView) v.findViewById(R.id.message_text);


                // Set their text
                messageText.setText(model.messageText);

            }
        };


        final ListView messagelist = (ListView)findViewById(R.id.messages);

        messagelist
                .setAdapter(firebaseListAdapter);


        }
}
