package com.hunglq.map1.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.hunglq.map1.R;
import com.hunglq.map1.UserActivity;
import com.hunglq.map1.adapter.OrderItemAdapter;
import com.hunglq.map1.model.OrderItem;

import static com.hunglq.map1.UserActivity.orderItemList;
import static com.hunglq.map1.util.TransactionHistoryFragment.orderId;

public class OrderHistoryDialog extends Dialog {
    Activity context;
    ListView lvOrderHistory;
    OrderItemAdapter adapter;
    ImageView imgBack;
    public OrderHistoryDialog(Activity context) {
        super(context);
        this.context = context;
        setContentView(R.layout.dialog_order_history);
        addControls();
        addEvents();
    }

    private void addControls() {
        imgBack = findViewById(R.id.imgBack);
        lvOrderHistory = findViewById(R.id.lvDOH);
        adapter = new OrderItemAdapter(context, R.layout.order_history_item);
        lvOrderHistory.setAdapter(adapter);
        for(OrderItem orderItem : orderItemList){
            if(orderId == orderItem.getOrderId()){
                adapter.add(orderItem);
            }
        }
    }

    private void addEvents() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
