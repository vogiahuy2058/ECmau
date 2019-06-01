package com.hunglq.map1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.icu.text.UnicodeSetSpanner;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hunglq.map1.config.Config;

import com.hunglq.map1.adapter.CartAdapter;
import com.hunglq.map1.model.Cart;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.hunglq.map1.MainActivity.items;
import static com.hunglq.map1.ShopActivity.cartList;
import static com.hunglq.map1.ShopActivity.database;

public class CartActivity extends AppCompatActivity {
    public static final int PAYPAL_REQUEST_CODE = 7171;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    ListView lvItem;
    CartAdapter adapter;
    TextView txtTotal;
    Button btnCheckout;
    int total = 0;

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        if(cartList.size() > 0){
            //Start paypal service
            Intent intent = new Intent(this, PayPalService.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            startService(intent);

            final LinearLayout layout = findViewById(R.id.cartEmpty);
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
                    processPayment();

                }
            });

            adapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    total = 0;
                    if(cartList.size() > 0)
                        cartList.clear();
                    if(adapter.getCount() == 0){
                        layout.setVisibility(View.VISIBLE);
                        String whereClause = "uid=?";
                        String whereArgs[] = {MainActivity.uid};
                        int kq = database.delete("cart", whereClause, whereArgs);
                        if(kq <= 0){
                            Toast.makeText(getApplicationContext(), "Something went wrong!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String whereClause = "uid=?";
                        String whereArgs[] = {MainActivity.uid};
                        int kq = database.delete("cart", whereClause, whereArgs);
                        if(kq <= 0){
                            Toast.makeText(getApplicationContext(), "Something went wrong!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        for (int i = 0; i < adapter.getCount(); i++) {
                            cartList.add(adapter.getItem(i));
                            total += (adapter.getItem(i).getAmount() * items.get(adapter.getItem(i).getItemid() - 1).getPrice());
                            txtTotal.setText("Total: $" + total);
                            ContentValues values = new ContentValues();
                            values.put("uid", MainActivity.uid);
                            values.put("itemid", (adapter.getItem(i).getItemid()));
                            values.put("amount", adapter.getItem(i).getAmount());
                            long add = database.insert("cart", null, values);
                            if (add <= 0)
                                Toast.makeText(CartActivity.this, "Somethong went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
    private void processPayment() {
        //total = Integer.valueOf(txtTotal.getText().toString().split("$")[1]);
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(total)), "USD",
                "Pay for this order", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }
    public void close(View view) {
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetail = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, PaymentDetailsActivity.class)
                                .putExtra("PaymentDetailsActivity", paymentDetail)
                                .putExtra("PaymentAmount", total)

                        );
                        sendCartToServer(MainActivity.url+"createOrder.php");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if(resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
    }

    private void sendCartToServer(String url) {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(Integer.parseInt(response) > 0) {
                            sendCartItem(MainActivity.url+"insertOrderItem.php", response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){

                Map<String, String> param = new HashMap<>();
                param.put("uid", MainActivity.uid);
                param.put("total", String.valueOf(total));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void sendCartItem(String url, final String orderId) {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Success")){
                            cartList.clear();
                            String whereClause = "uid=?";
                            String whereArgs[] = {MainActivity.uid};
                            int kq = database.delete("cart", whereClause, whereArgs);
                            if(kq <= 0) {
                                Toast.makeText(getApplicationContext(), "Something went wrong!",
                                        Toast.LENGTH_SHORT).show();
                            }
                            finish();
                        } else {
                            Log.e("RRR", response);
                            Toast.makeText(getApplicationContext(), "Server Error!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                JSONArray jsonArray = new JSONArray();
                for(int i=0;i<cartList.size();i++)
                {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("orderId",orderId);
                        jsonObject.put("itemId",cartList.get(i).getItemid());
                        jsonObject.put("amount",cartList.get(i).getAmount());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }
                HashMap<String,String> hashMap = new HashMap<String, String>();
                hashMap.put("json",jsonArray.toString());
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }
}
