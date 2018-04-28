package com.hackathon.sha3by.sha3by;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Activity2 extends AppCompatActivity {
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String userName = sharedPref.getString("Name", "");

        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity2);


        users =new ArrayList<>();
        users.add(new User("Mohamed",0,1));
        users.add(new User("Ferida",0,0));
        users.add(new User("Ling",1,1));
        User self=new User ("Farah",1,0);

        UserAdapter adapter=new UserAdapter(this, users);
        ListView listView = findViewById(R.id.listview1);
        listView.setAdapter(adapter);



        Button button = findViewById(R.id.third_activity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity2.this, Activity3.class));
            }
        });
    }

    public class UserAdapter extends ArrayAdapter<User> {
        private Context mContext;
        private List<User> usersList = new ArrayList<>();

        public UserAdapter(Context context, List<User> list) {
            super(context, 0 , list);
            mContext = context;
            usersList = list;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            User user = users.get(position);

            if(convertView==null){
                convertView=getLayoutInflater().inflate(R.layout.user, parent, false);
            }

            TextView koala = convertView.findViewById(R.id.username);
            koala.setText(user.name);

            return convertView;
        }
    }

}

