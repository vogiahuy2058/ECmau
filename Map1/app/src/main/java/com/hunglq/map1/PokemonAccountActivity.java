package com.hunglq.map1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hunglq.map1.adapter.PokemonAdapter;
import com.hunglq.map1.model.AccountPokemon;
import com.hunglq.map1.util.CustomRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.yavski.fabspeeddial.FabSpeedDial;

public class PokemonAccountActivity extends AppCompatActivity {

    GridView gvPokemon;
    PokemonAdapter adapter;
    TextView txtTotal;
    EditText edtSearch;
    public final static ArrayList<AccountPokemon> accountPokemons = new ArrayList<>();
    String order = "RECENT";
    String name = "";
    boolean isDelete;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_account);

        edtSearch = findViewById(R.id.edtSearchPA);
        txtTotal = findViewById(R.id.txtTotalPkmAcc);
        final FabSpeedDial fabSort = findViewById(R.id.fabPA);
        final LinearLayout linearLayout = findViewById(R.id.linearPA);
        gvPokemon = findViewById(R.id.gvPokemonAccount);
        adapter = new PokemonAdapter(PokemonAccountActivity.this, R.layout.pokemon_account_item);
        gvPokemon.setAdapter(adapter);
        final Snackbar snackbar = Snackbar.make(linearLayout, "Goodbye my love!!!", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isDelete = false;
                        Toast.makeText(getApplicationContext(), "You're just to good to be true !!!",
                                Toast.LENGTH_LONG).show();
                    }
                });

        readJsonPokemonAccount(MainActivity.url + "getPokemonOfAccount.php");

        gvPokemon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PokemonAccountActivity.this, PokemonAccountInfoActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        gvPokemon.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Snackbar.make(linearLayout, "Delete this Pokemon", Snackbar.LENGTH_LONG)
                        .setAction("YES", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                isDelete = true;
                                snackbar.show();
                                snackbar.addCallback(new Snackbar.Callback(){
                                    @Override
                                    public void onDismissed(Snackbar transientBottomBar, int event) {
                                        if(isDelete){
                                            deletePokemon(MainActivity.url+"deletePokemonOfAccount.php",
                                                    accountPokemons.get(position).getPokemonId()+"",
                                                    accountPokemons.get(position).getDate());
                                            isDelete = false;
                                        }
                                    }
                                });
                            }
                        }).show();
                return true;
            }
        });

        fabSort.setMenuListener(new FabSpeedDial.MenuListener() {

            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                linearLayout.setBackgroundColor((getColorWithAlpha(Color.rgb( 0, 87, 75), 0.7f)));
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                linearLayout.setBackgroundColor((getColorWithAlpha(Color.GREEN, 0.0f)));
                order = menuItem.getTitle().toString().trim();
                readJsonPokemonWithSearchAndSort(MainActivity.url + "getPokemonOrderBy.php", order,name);
                return true;
            }

            @Override
            public void onMenuClosed() {
                linearLayout.setBackgroundColor((getColorWithAlpha(Color.GREEN, 0.0f)));
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name = edtSearch.getText().toString();
                readJsonPokemonWithSearchAndSort(MainActivity.url + "getPokemonOrderBy.php", order, name);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }

    //Get list pokemon that caught by account
    private void readJsonPokemonAccount(String url) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", MainActivity.uid);
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final CustomRequest request = new CustomRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        adapter.clear();
                        if(accountPokemons != null)
                            accountPokemons.clear();
                        for (int i = 0; i < response.length(); i++){
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = response.getJSONObject(i);
                                accountPokemons.add(new AccountPokemon(
                                        jsonObject.getString("UID"),
                                        jsonObject.getInt("PokemonID"),
                                        jsonObject.getString("Date"),
                                        jsonObject.getString("Location")
                                ));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            adapter.add(MainActivity.pokemonList.get(accountPokemons.get(i).getPokemonId() - 1));
                            txtTotal.setText("Total: " + response.length());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PokemonAccountActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(request);
    }

    private void readJsonPokemonWithSearchAndSort(String url, String order, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", MainActivity.uid);
        params.put("order", order);
        params.put("name", name);
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final CustomRequest request = new CustomRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        adapter.clear();
                        if(accountPokemons != null)
                            accountPokemons.clear();
                        for (int i = 0; i < response.length(); i++){
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = response.getJSONObject(i);
                                accountPokemons.add(new AccountPokemon(
                                        jsonObject.getString("UID"),
                                        jsonObject.getInt("PokemonID"),
                                        jsonObject.getString("Date"),
                                        jsonObject.getString("Location")
                                ));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            adapter.add(MainActivity.pokemonList.get(accountPokemons.get(i).getPokemonId() - 1));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PokemonAccountActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("QQQ", error.toString());
                    }
                });

        requestQueue.add(request);
    }

    private void deletePokemon(String url, final String pokemonId, final String catchDate) {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(getApplicationContext(), "Deleted",
                                    Toast.LENGTH_LONG).show();
                            readJsonPokemonWithSearchAndSort(MainActivity.url + "getPokemonOrderBy.php", order,name);
                            txtTotal.setText("Total: " + (accountPokemons.size() - 1));

                        } else {
                            Toast.makeText(getApplicationContext(), "It's not a bug, it's a feature!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("QQQ", error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams(){

                Map<String, String> param = new HashMap<>();
                param.put("uid", MainActivity.uid);
                param.put("pokemonId",pokemonId.trim());
                param.put("catchDate", catchDate);

                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    //Close this activity
    public void backToMapFromPkmAcc(View view) {
        finish();
    }
}
