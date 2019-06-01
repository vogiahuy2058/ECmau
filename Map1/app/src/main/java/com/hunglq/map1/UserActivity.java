package com.hunglq.map1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hunglq.map1.adapter.UserFragmentAdapter;
import com.hunglq.map1.model.Order;
import com.hunglq.map1.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    Button btnLogOut;
    ViewPager vpUser;
    TabLayout tlUser;
    public final static List<Order> orderList = new ArrayList<>();
    public final static List<OrderItem> orderItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        btnLogOut = (Button)findViewById(R.id.btnLogOut);
        vpUser = findViewById(R.id.vpUser);
        vpUser.setAdapter(new UserFragmentAdapter(getSupportFragmentManager()));
        tlUser = findViewById(R.id.tlUser);
        tlUser.setupWithViewPager(vpUser);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.uid=null;
                SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.mypreference,
                        Context.MODE_PRIVATE);
                sharedPreferences.edit().remove(MainActivity.UID).commit();
                Toast.makeText(getBaseContext(), "Log out.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(UserActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
