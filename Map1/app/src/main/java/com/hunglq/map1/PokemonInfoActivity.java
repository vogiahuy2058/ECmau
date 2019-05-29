package com.hunglq.map1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.hunglq.map1.adapter.PokemonAdapter;
import com.hunglq.map1.model.Pokemon;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PokemonInfoActivity extends AppCompatActivity {

    Pokemon pokemon;
    TextView txtCP, txtName, txtDescription, txtType;
    ImageView imgPokemon, btnOk;

    Geocoder geocoder;
    List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_info);

        pokemon = MainActivity.pokemonList.get(MapsActivity.pokemonID - 1);

        txtCP = findViewById(R.id.txtCP);
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        txtType = findViewById(R.id.txtType);
        imgPokemon = findViewById(R.id.imgPokemon);

        imgPokemon.setImageBitmap(getBitmapFromAssets(pokemon.getHinh()));
        txtName.setText(pokemon.getTen());
        txtCP.setText("CP " + pokemon.getCp());
        txtDescription.setText(pokemon.getMoTa());
        txtType.setText("Type: " + pokemon.getLoai());

        sendToServer(MainActivity.url+"catchPokemon.php");

        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendToServer(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("success")){
                            Toast.makeText(PokemonInfoActivity.this, "Success",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(PokemonInfoActivity.this,
                                    "Error!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PokemonInfoActivity.this, error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams(){

                Map<String, String> param = new HashMap<>();
                param.put("uid", MainActivity.uid.trim());
                param.put("pokemonId", String.valueOf(pokemon.getMa()));
                param.put("location", curAddress(MapsActivity.pokemonLocation));

                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    public String curAddress(LatLng latLng) {
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            String area = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String country = addresses.get(0).getCountryName();

            return area;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public Bitmap getBitmapFromAssets(String fileName) {
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return Bitmap.createScaledBitmap(bitmap, 500, 500, false);
    }
}
