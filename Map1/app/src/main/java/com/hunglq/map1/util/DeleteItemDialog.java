package com.hunglq.map1.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hunglq.map1.MainActivity;
import com.hunglq.map1.R;
import com.hunglq.map1.adapter.AccountItemAdapter;

import java.util.HashMap;
import java.util.Map;

public class DeleteItemDialog extends Dialog {

    Activity context;
    TextView txtItem, txtAmount;
    ImageView imgPlus, imgMinus;
    Button btnYes, btnCancel;
    int amount;

    public DeleteItemDialog(Activity context) {
        super(context);
        this.context = context;
        setContentView(R.layout.dialog_delete_item);
        addControls();
        addEvents();
    }

    private void addControls() {
        txtAmount = findViewById(R.id.txtAmountDDI);
        txtItem = findViewById(R.id.txtItemDDI);
        imgMinus = findViewById(R.id.imgMinusDDI);
        imgPlus = findViewById(R.id.imgPlusDDI);
        btnYes = findViewById(R.id.btnYesDDI);
        btnCancel = findViewById(R.id.btnCancelDDI);

        txtItem.setText("Discard " + AccountItemAdapter.itemName + "?");
        amount = 1;
    }

    private void addEvents() {
        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount < AccountItemAdapter.amount){
                    amount +=1;
                    txtAmount.setText(amount + "");
                }
            }
        });

        imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount > 1){
                    amount -=1;
                    txtAmount.setText(amount + "");
                }
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(MainActivity.url+"deleteAccountItem.php", AccountItemAdapter.itemId, AccountItemAdapter.amount - amount);
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private void deleteItem(String url, final int itemId, final int amount){
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Success")){
                            dismiss();
                        }
                        else {
                            Toast.makeText(context,
                                    "Something went wrong!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){

                Map<String, String> param = new HashMap<>();
                param.put("uid", MainActivity.uid);
                param.put("item", String.valueOf(itemId));
                param.put("amount", String.valueOf(amount));

                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        txtItem.setText("Discard " + AccountItemAdapter.itemName + "?");
        txtAmount.setText("1");
        amount = 1;
    }
}
