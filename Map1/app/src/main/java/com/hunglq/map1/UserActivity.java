package com.hunglq.map1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class UserActivity extends AppCompatActivity {

    Button btnLogOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        btnLogOut = (Button)findViewById(R.id.btnLogOut);

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
