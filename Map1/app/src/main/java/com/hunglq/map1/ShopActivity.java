package com.hunglq.map1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hunglq.map1.adapter.ShopItemAdapter;
import com.hunglq.map1.model.Cart;
import com.hunglq.map1.model.Item;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class ShopActivity extends AppCompatActivity {

    public static String DATABASE_NAME = "cart.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database = null;
    public static List<Cart> cartList = new ArrayList<>();
    GridView gvShop;
    ShopItemAdapter shopItemAdapter;
    EditText edtSearch;
    ImageView imgCart;
    TextView txtCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        processCopy();
        getCartItem();

        edtSearch = findViewById(R.id.edtSearchShop);
        imgCart = findViewById(R.id.imgCart);
        txtCart = findViewById(R.id.txtCart);

        gvShop= findViewById(R.id.gvShop);
        shopItemAdapter= new ShopItemAdapter(this,R.layout.shop_item);
        gvShop.setAdapter(shopItemAdapter);

        if(cartList.size() > 0){
            txtCart.setText(cartList.size()+"");
        } else {
            txtCart.setText("");
        }

        for (Item item:MainActivity.items) {
            shopItemAdapter.add(item);
        }

        gvShop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShopActivity.this, ItemDetailActivity.class);
                Item item = shopItemAdapter.getItem(position);
                intent.putExtra("Item", item);
                startActivity(intent);
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shopItemAdapter.clear();
                if(edtSearch.getText().toString().equals("")){
                    for (Item item:MainActivity.items) {
                        shopItemAdapter.add(item);
                    }
                } else {
                    for (Item item : MainActivity.items){
                        if(item.getName().toLowerCase().contains(edtSearch.getText().toString().trim().toLowerCase())){
                            shopItemAdapter.add(item);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopActivity.this, CartActivity.class));
            }
        });
    }

    private void getCartItem() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("Select * from cart", null);
        if(cartList!=null){
            cartList.clear();
        }
        while (cursor.moveToNext()){
            String uid = cursor.getString(0);
            int itemId = cursor.getInt(1);
            int amount = cursor.getInt(2);
            cartList.add(new Cart(uid, itemId, amount));
        }
        cursor.close();
    }


    public void backToMap(View view) {
        finish();
    }

    // Sao chép CSDL từ folder assets vào hệ thống
    private void processCopy() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        try {
            if (!dbFile.exists()) {
                copyDatabaseFromAsset();
                Toast.makeText(ShopActivity.this, "Copy successful",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(ShopActivity.this, "Copy fail",
                    Toast.LENGTH_LONG).show();
            Log.e("ERROR!!!!", ex.toString());
        }
    }

    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

    private void copyDatabaseFromAsset() {
        try {
            InputStream inputStream = getAssets().open(DATABASE_NAME);
            String outFileName = getDatabasePath();
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists()) {
                f.mkdir();
            }
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int leght;
            while ((leght = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, leght);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception ex) {
            Log.e("ERROR!!", ex.toString());
        }
    }
}
