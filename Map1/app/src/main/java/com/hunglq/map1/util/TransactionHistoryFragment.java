package com.hunglq.map1.util;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hunglq.map1.MainActivity;
import com.hunglq.map1.R;
import com.hunglq.map1.adapter.OrderAdapter;
import com.hunglq.map1.model.Order;
import com.hunglq.map1.model.OrderItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.hunglq.map1.UserActivity.orderItemList;
import static com.hunglq.map1.UserActivity.orderList;

public class TransactionHistoryFragment extends Fragment {
    View mView;
    ListView lvTranHis;
    OrderAdapter adapter;
    public static int orderId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_transaction_history, container, false);

        final TextView txtMsg = mView.findViewById(R.id.txtMsgFTH);
        lvTranHis = mView.findViewById(R.id.lvTHF);
        adapter = new OrderAdapter(getActivity(), R.layout.order_item);
        lvTranHis.setAdapter(adapter);

        Map<String, String> params = new HashMap<>();
        params.put("uid", MainActivity.uid);
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        final CustomRequest request = new CustomRequest(Request.Method.POST, MainActivity.url+"getOrderByUser.php", params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(orderList != null)
                            orderList.clear();
                        adapter.clear();
                        for (int i = 0; i < response.length(); i++){
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = response.getJSONObject(i);
                                orderList.add(new Order(
                                        jsonObject.getInt("ID"),
                                        jsonObject.getString("Date"),
                                        jsonObject.getInt("Total"))
                                );
                                adapter.add(new Order(
                                        jsonObject.getInt("ID"),
                                        jsonObject.getString("Date"),
                                        jsonObject.getInt("Total"))
                                );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(orderList.size() == 0){
                            txtMsg.setVisibility(View.VISIBLE);
                        } else {
                            txtMsg.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(request);

        getOrderItem(MainActivity.url+"getOrderItemByUser.php");

        lvTranHis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                orderId = adapter.getItem(position).getId();
                OrderHistoryDialog orderHistoryDialog = new OrderHistoryDialog(getActivity());
                orderHistoryDialog.show();
            }
        });

        return mView;
    }

    private void getOrderItem(String url) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", MainActivity.uid);
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        final CustomRequest request = new CustomRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(orderItemList != null)
                            orderItemList.clear();
                        for (int i = 0; i < response.length(); i++){
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = response.getJSONObject(i);
                                orderItemList.add(new OrderItem(
                                        jsonObject.getInt("OrderId"),
                                        jsonObject.getInt("ItemId"),
                                        jsonObject.getInt("Amount"))
                                );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(request);
    }
}
