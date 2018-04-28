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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Activity2 extends AppCompatActivity {
    private List<User> users = new ArrayList<>();
    UserAdapter adapter = new UserAdapter(this, users);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String userName = sharedPref.getString("Name", "");
        int arabic = sharedPref.getInt("Arabic", 0);
        int avatar = sharedPref.getInt("Avatar", 0);
        User currUser = new User(userName, arabic, avatar);

        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity2);

        ListView listView = findViewById(R.id.listview1);
        listView.setAdapter(adapter);



        Button button = findViewById(R.id.third_activity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity2.this, Activity3.class));
            }
        });

        MessageStore.getInstance().firebasedatabase.getReference().child("users").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        users.clear();
                        for(DataSnapshot userValue: dataSnapshot.getChildren()){
                            User user = userValue.getValue(User.class);
                            users.add(user);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

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

