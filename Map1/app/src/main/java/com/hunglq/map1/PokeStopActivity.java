package com.hunglq.map1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hunglq.map1.adapter.AccountItemAdapter;
import com.hunglq.map1.model.AccountItem;
import com.hunglq.map1.util.CustomRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.hunglq.map1.AccountItemActivity.accountItems;

public class PokeStopActivity extends AppCompatActivity {
    ImageView imgPokeSpinPSA;
    ImageView item1;
    ImageView item2;
    ImageView item3;
    boolean isSpin=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokestop);

        getAccountItem(MainActivity.url + "getAccountItem.php");

        imgPokeSpinPSA=(ImageView)findViewById(R.id.imgPokeSpinPSA);
        item1=(ImageView)findViewById(R.id.item1);
        item2=(ImageView)findViewById(R.id.item2);
        item3=(ImageView)findViewById(R.id.item3);
        Random random = new Random();
        final int i1 = random.nextInt(7);
        final int i2 = random.nextInt(7);
        final int i3 = random.nextInt(7);
        Picasso.get().load(MainActivity.items.get(i1).getImage()).into(item1);
        Picasso.get().load(MainActivity.items.get(i2).getImage()).into(item2);
        Picasso.get().load(MainActivity.items.get(i3).getImage()).into(item3);

        final Animation animation= AnimationUtils.loadAnimation(this,R.anim.pokespinani);
        final Animation aniFadeIn= AnimationUtils.loadAnimation(this,R.anim.fadein);
        final Animation aniFadeOut= AnimationUtils.loadAnimation(this,R.anim.fadeout);

        imgPokeSpinPSA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSpin==false){
                imgPokeSpinPSA.startAnimation(animation);
                item1.startAnimation(aniFadeIn);
                item2.startAnimation(aniFadeIn);
                item3.startAnimation(aniFadeIn);
                item1.setVisibility(View.VISIBLE);
                item2.setVisibility(View.VISIBLE);
                item3.setVisibility(View.VISIBLE);
                updateItem(MainActivity.url+"deleteAccountItem.php",i1+1, accountItems.get(i1).getAmount()+1);
                    accountItems.get(i1).setAmount(accountItems.get(i1).getAmount()+1);
                updateItem(MainActivity.url+"deleteAccountItem.php",i2+1, accountItems.get(i2).getAmount()+1);
                    accountItems.get(i2).setAmount(accountItems.get(i2).getAmount()+1);
                updateItem(MainActivity.url+"deleteAccountItem.php",i3+1, accountItems.get(i3).getAmount()+1);
                    accountItems.get(i3).setAmount(accountItems.get(i3).getAmount()+1);
                isSpin=true;
            }else{
                Toast.makeText(PokeStopActivity.this,
                        "Plese come back later!",
                        Toast.LENGTH_LONG).show();
            }
            }
        });

    }

    private void updateItem(String url, final int itemId, final int amount){
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Success")){
                        }
                        else {
                            Toast.makeText(PokeStopActivity.this,
                                    "Something went wrong!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PokeStopActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){

                Map<String, String> param = new HashMap<>();
                param.put("uid", MainActivity.uid);
                param.put("item", String.valueOf(itemId));
                param.put("amount", String.valueOf(amount));

                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getAccountItem(String url){
        Map<String, String> params = new HashMap<>();
        params.put("uid", MainActivity.uid);
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final CustomRequest request = new CustomRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(accountItems != null)
                            accountItems.clear();
                        for (int i = 0; i < response.length(); i++){
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = response.getJSONObject(i);
                                accountItems.add(new AccountItem(
                                        jsonObject.getString("UID"),
                                        jsonObject.getInt("ItemId"),
                                        jsonObject.getInt("Amount")
                                ));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PokeStopActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("QQQ", error.toString());
                    }
                });

        requestQueue.add(request);
    }
}
