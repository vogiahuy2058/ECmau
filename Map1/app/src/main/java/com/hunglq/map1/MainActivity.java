package com.hunglq.map1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hunglq.map1.model.Item;
import com.hunglq.map1.model.PokeStop;
import com.hunglq.map1.model.Pokemon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // Webservice URL
    //public final static String url = "http://pokebizute15.000webhostapp.com/";
    public final static String url = "http://172.168.80.135:85/androidwebservice/";
    public static String uid;
    EditText edtUID, edtPwd;
    Button btnLogin, btnReg;
    public final static ArrayList<Pokemon> pokemonList = new ArrayList<>();
    public final static List<Item> items = new ArrayList<>();
    public final static List<PokeStop> pokeStops = new ArrayList<>();
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String UID = "uidKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        getPokeStop(url+"getPokeStop.php");
        readJsonPokemon(url+"getPokemon.php");
        getAllItem(MainActivity.url + "getItem.php");

        if (sharedpreferences.getString(UID, "").equals("")){
            addControls();
            addEvents();
        } else {
            uid = sharedpreferences.getString(UID, "");
            startActivity(new Intent(MainActivity.this, MapsActivity.class));
            finish();
        }
    }

    private void addControls() {
        edtUID = findViewById(R.id.edtUserName);
        edtPwd = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnReg = findViewById(R.id.btnRegistor);
    }

    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin(url+"checkLogin.php");
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegActivity.isSuccess = false;
                startActivity(new Intent(MainActivity.this, RegActivity.class));
            }
        });
    }

    private  void checkLogin(String url){
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Response == 1 => Login success
                        if(response.trim().equals("1")){
                            uid = edtUID.getText().toString();
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(UID, uid);
                            editor.commit();
                            startActivity(new Intent(MainActivity.this, MapsActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(MainActivity.this,
                                    "Username or Password is wrong!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){

                Map<String, String> param = new HashMap<>();
                param.put("uid", edtUID.getText().toString().trim());
                param.put("pwd", edtPwd.getText().toString().trim());

                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    // Get pokemon from server via webservice and add to pokemonList
    private  void readJsonPokemon(String url){
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(pokemonList != null)
                            pokemonList.clear();
                        for(int i = 0; i < response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                pokemonList.add(new Pokemon(
                                        jsonObject.getInt("Ma"),
                                        jsonObject.getString("Ten"),
                                        jsonObject.getString("Loai"),
                                        jsonObject.getInt("CP"),
                                        jsonObject.getString("MoTa"),
                                        jsonObject.getString("Hinh"),
                                        jsonObject.getInt("CatchPoint")
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
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    private void getAllItem(String url){
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(items != null)
                            items.clear();
                        for(int i = 0; i < response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                items.add(new Item(
                                        jsonObject.getInt("ID"),
                                        jsonObject.getString("Name"),
                                        jsonObject.getString("Description"),
                                        jsonObject.getString("Image"),
                                        jsonObject.getInt("Price")
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
                        Toast.makeText(getApplicationContext(), "(Adapter)" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    private void getPokeStop(String url){
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(pokeStops != null)
                            pokeStops.clear();
                        for(int i = 0; i < response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                pokeStops.add(new PokeStop(
                                        jsonObject.getInt("ID"),
                                        jsonObject.getString("Name"),
                                        jsonObject.getDouble("Lat"),
                                        jsonObject.getDouble("Lon")
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
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }
}
