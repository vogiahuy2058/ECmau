package com.hunglq.map1;

import android.database.DataSetObserver;
import android.icu.text.UnicodeSetSpanner;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hunglq.map1.adapter.CartAdapter;
import com.hunglq.map1.model.Cart;

import static com.hunglq.map1.MainActivity.items;
import static com.hunglq.map1.ShopActivity.cartList;

public class CartActivity extends AppCompatActivity {

    ListView lvItem;
    CartAdapter adapter;
    TextView txtTotal;
    Button btnCheckout;
    int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        if(cartList.size() > 0){
            LinearLayout layout = findViewById(R.id.cartEmpty);
            layout.setVisibility(View.GONE);

            txtTotal = findViewById(R.id.txtTotalCI);
            btnCheckout = findViewById(R.id.btnCheckoutCI);

            lvItem = findViewById(R.id.lvItemCI);
            adapter = new CartAdapter(CartActivity.this, R.layout.cart_item);
            lvItem.setAdapter(adapter);

            adapter.clear();
            for (Cart cart : cartList) {
                adapter.add(cart);
                total += (cart.getAmount() * items.get(cart.getItemid() - 1).getPrice());
            }

            txtTotal.setText("Total: $" + total);

            btnCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(CartActivity.this, "OK", Toast.LENGTH_SHORT).show();
                }
            });

            adapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    Toast.makeText(getApplicationContext(), "Yeah", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void close(View view) {
        finish();
    }
}
