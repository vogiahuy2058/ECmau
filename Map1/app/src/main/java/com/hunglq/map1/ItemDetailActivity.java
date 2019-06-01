package com.hunglq.map1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hunglq.map1.adapter.ShopItemAdapter;
import com.hunglq.map1.model.Cart;
import com.hunglq.map1.model.Item;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static com.hunglq.map1.ShopActivity.cartList;
import static com.hunglq.map1.ShopActivity.database;

public class ItemDetailActivity extends AppCompatActivity {

    ImageView imgItemDetail,imgDecrease,imgIncrease;
    TextView txtItemDetailName,txtItemDescription,txtItemQuantity;
    Button btnBuyItem;
    int  quantity,price;
    Item item;
    //ShopItemAdapter shopItemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);


        imgItemDetail = findViewById(R.id.imgItemDetail);
        imgDecrease = findViewById(R.id.imgDecrease);
        imgIncrease = findViewById(R.id.imgIncrease);

        txtItemDetailName = findViewById(R.id.txtItemDetailName);
        txtItemDescription = findViewById(R.id.txtItemDescription);
        txtItemQuantity = findViewById(R.id.txtItemQuantity);

        btnBuyItem = findViewById(R.id.btnBuyItem);
        quantity=1;
        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("Item");
        price=item.getPrice();

        txtItemDetailName.setText(item.getName());
        txtItemDescription.setText(item.getDescription());
        txtItemQuantity.setText("1");
        Picasso.get()
                .load(item.getImage())
                .placeholder(android.R.drawable.ic_menu_report_image)
                .error(android.R.drawable.ic_menu_report_image)
                .into(imgItemDetail);

        imgIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    quantity+=1;
                    price=quantity*item.getPrice();
                    txtItemQuantity.setText(quantity+"");
            }
        });
        imgDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity>1){
                    quantity-=1;
                    price=quantity*item.getPrice();
                    txtItemQuantity.setText(quantity+"");

                }

            }
        });

        btnBuyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });
    }

    private void addToCart() {

        ContentValues values = new ContentValues();
        for(int i = 0; i < cartList.size(); i++){
            if(cartList.get(i).getItemid() == item.getId()){
                cartList.get(i).setAmount(quantity + cartList.get(i).getAmount());
                values.put("amount", quantity + cartList.get(i).getAmount());
                long kq =database.update("cart", values, "uid = ? AND itemid = ?",
                        new String[]{MainActivity.uid, String.valueOf(item.getId())});
                if (kq > 0) {
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(), "Update fail!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        values.put("uid", MainActivity.uid);
        values.put("itemid", item.getId());
        values.put("amount", quantity);
        long kq = database.insert("cart", null, values);
        if (kq > 0) {
            ShopActivity.cartList.add(new Cart(MainActivity.uid, item.getId(), quantity));
            finish();
        }
        else
            Toast.makeText(ItemDetailActivity.this, "Add fail!", Toast.LENGTH_SHORT).show();
    }

    public void backToMap(View view) {
        finish();
    }


}
