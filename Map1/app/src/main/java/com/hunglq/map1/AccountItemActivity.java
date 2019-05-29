package com.hunglq.map1;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hunglq.map1.adapter.AccountItemAdapter;
import com.hunglq.map1.model.AccountItem;
import com.hunglq.map1.util.CustomRequest;
import com.hunglq.map1.util.DeleteItemDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountItemActivity extends AppCompatActivity {

    ListView lvItem;
    AccountItemAdapter adapter;
    public final static List<AccountItem> accountItems = new ArrayList<>();
    public static DeleteItemDialog deleteItemDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_item);

        lvItem = findViewById(R.id.lvItemAI);
        adapter = new AccountItemAdapter(this, R.layout.account_item);
        getAccountItem(MainActivity.url + "getAccountItem.php");
        lvItem.setAdapter(adapter);
        deleteItemDialog = new DeleteItemDialog(this);
        deleteItemDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                getAccountItem(MainActivity.url + "getAccountItem.php");
            }
        });
    }

    private void getAccountItem(String url){
        Map<String, String> params = new HashMap<>();
        params.put("uid", MainActivity.uid);
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final CustomRequest request = new CustomRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        adapter.clear();
                        if(accountItems != null)
                            accountItems.clear();
                        for (int i = 0; i < response.length(); i++){
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = response.getJSONObject(i);
                                accountItems.add(new AccountItem(
                                        jsonObject.getString("UID"),
                                        jsonObject.getInt("ItemId"),
                                        jsonObject.getInt("Amount")
                                ));
                                adapter.add(new AccountItem(
                                        jsonObject.getString("UID"),
                                        jsonObject.getInt("ItemId"),
                                        jsonObject.getInt("Amount")
                                ));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AccountItemActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("QQQ", error.toString());
                    }
                });

        requestQueue.add(request);
    }

    public void backToMap(View view) {
        finish();
    }
}
