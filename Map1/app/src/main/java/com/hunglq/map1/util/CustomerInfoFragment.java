package com.hunglq.map1.util;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hunglq.map1.MainActivity;
import com.hunglq.map1.PokemonAccountActivity;
import com.hunglq.map1.R;
import com.hunglq.map1.model.AccountPokemon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.hunglq.map1.PokemonAccountActivity.accountPokemons;

public class CustomerInfoFragment extends Fragment {
    View mView;
    TextView txtUserName, txtPay, txtPokemon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_customer_info, container, false);

        //txtPay = mView.findViewById(R.id.txtTotalFCI);
        txtPokemon = mView.findViewById(R.id.txtPokemonFCI);
        txtUserName = mView.findViewById(R.id.txtUsername);

        readJsonPokemonAccount(MainActivity.url + "getPokemonOfAccount.php");

        txtUserName.setText("User name: " + MainActivity.uid);

        return mView;
    }

    private void readJsonPokemonAccount(String url) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", MainActivity.uid);
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        final CustomRequest request = new CustomRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
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
                        }
                        txtPokemon.setText("Total pokemon: " + accountPokemons.size());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(request);
    }
}
