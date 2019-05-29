package com.hunglq.map1.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hunglq.map1.R;
import com.hunglq.map1.model.Pokemon;

import java.io.IOException;
import java.io.InputStream;

public class PokemonAdapter extends ArrayAdapter<Pokemon> {
    Activity context;
    int resource;
    ImageView imgHinh;
    TextView txtMa, txtTen;

    public PokemonAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView = this.context.getLayoutInflater().inflate(this.resource, null);

        if(this.resource == R.layout.pokemon_item){
            imgHinh = mView.findViewById(R.id.imgHinhPkmItem);
            txtMa = mView.findViewById(R.id.txtMaPkmItem);
            txtTen = mView.findViewById(R.id.txtTenPkmItem);

            Pokemon pokemon = getItem(position);

            imgHinh.setImageBitmap(getBitmapFromAssets(pokemon.getHinh()));
            txtMa.setText("#" + pokemon.getMa());
            txtTen.setText(pokemon.getTen());
        }
        else if(this.resource == R.layout.pokemon_account_item){
            imgHinh = mView.findViewById(R.id.imgHinhPkmAcc);
            txtMa = mView.findViewById(R.id.txtCPPkmAcc);
            txtTen = mView.findViewById(R.id.txtTenPkmAcc);

            Pokemon pokemon = getItem(position);

            imgHinh.setImageBitmap(getBitmapFromAssets(pokemon.getHinh()));
            txtMa.setText("CP " + pokemon.getCp());
            txtTen.setText(pokemon.getTen());
        }

        return mView;
    }

    public Bitmap getBitmapFromAssets(String fileName) {
        InputStream inputStream = null;
        try {
            inputStream =  this.context.getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }
}
