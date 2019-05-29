package com.hunglq.map1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hunglq.map1.model.Pokemon;

import java.io.IOException;
import java.io.InputStream;

public class PokemonDetailPkdActivity extends AppCompatActivity {

    ImageView imgPokemon;
    TextView txtNoName, txtType, txtDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_detail_pkd);

        imgPokemon = findViewById(R.id.imgPokemonPdkDetail);
        txtNoName = findViewById(R.id.txtNoNamePokemonPkdDetail);
        txtType = findViewById(R.id.txtTypePokemonDetailPkd);
        txtDescription = findViewById(R.id.txtDesPokemonPkdDetail);

        Intent intent = getIntent();
        Pokemon pokemon = (Pokemon) intent.getSerializableExtra("Pokemon");

        imgPokemon.setImageBitmap(getBitmapFromAssets(pokemon.getHinh()));
        txtNoName.setText("#"+pokemon.getMa()+" " + pokemon.getTen());
        txtType.setText(pokemon.getLoai());
        txtDescription.setText(pokemon.getMoTa());
    }

    public Bitmap getBitmapFromAssets(String fileName) {
        InputStream inputStream = null;
        try {
            inputStream =  getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }

    public void backToPokedex(View view) {
        finish();
    }
}
