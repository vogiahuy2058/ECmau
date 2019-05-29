package com.hunglq.map1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hunglq.map1.model.AccountPokemon;
import com.hunglq.map1.model.Pokemon;

import java.io.IOException;
import java.io.InputStream;

public class PokemonAccountInfoActivity extends AppCompatActivity {

    TextView txtCP, txtName, txtType, txtLocation;
    ImageView imgPokemon, btnOk;
    Pokemon pokemon;
    AccountPokemon accountPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_account_info);

        txtCP = findViewById(R.id.txtCPPAI);
        txtName = findViewById(R.id.txtNamePAI);
        txtType = findViewById(R.id.txtTypePAI);
        txtLocation = findViewById(R.id.txtLocationPAI);
        imgPokemon = findViewById(R.id.imgPokemonPAI);
        btnOk = findViewById(R.id.btnOkPAI);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        accountPokemon = PokemonAccountActivity.accountPokemons.get(position);
        pokemon = MainActivity.pokemonList.get(accountPokemon.getPokemonId() - 1);

        txtCP.setText("CP: "+pokemon.getCp());
        txtLocation.setText(accountPokemon.getDate()+"\n"+accountPokemon.getLocation());
        txtType.setText("Type: "+pokemon.getLoai());
        txtName.setText(pokemon.getTen());
        imgPokemon.setImageBitmap(getBitmapFromAssets(pokemon.getHinh()));

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
