package com.hunglq.map1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.hunglq.map1.adapter.PokemonAdapter;
import com.hunglq.map1.model.Pokemon;

public class PokedexActivity extends AppCompatActivity {

    GridView gvPokemon;
    public PokemonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex);

        gvPokemon = findViewById(R.id.gvPokemon);
        adapter = new PokemonAdapter(this, R.layout.pokemon_item);
        for (Pokemon pokemon : MainActivity.pokemonList)
            adapter.add(pokemon);
        gvPokemon.setAdapter(adapter);

        gvPokemon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PokedexActivity.this, PokemonDetailPkdActivity.class);
                Pokemon pokemon = adapter.getItem(position);
                intent.putExtra("Pokemon", pokemon);
                startActivity(intent);
            }
        });
    }

    public void backToMap(View view) {
        finish();
    }
}
