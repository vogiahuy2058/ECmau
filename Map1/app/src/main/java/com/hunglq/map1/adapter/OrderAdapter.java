package com.hunglq.map1.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hunglq.map1.R;
import com.hunglq.map1.model.Order;
import com.hunglq.map1.model.OrderItem;

import static com.hunglq.map1.UserActivity.orderItemList;

public class OrderAdapter extends ArrayAdapter<Order> {
    Activity context;
    int resource;
    View mView;
    TextView txtDate, txtId, txtAmount, txtTotal;
    int amount;

    public OrderAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mView = this.context.getLayoutInflater().inflate(this.resource, null);

        txtAmount = mView.findViewById(R.id.txtAmountOI);
        txtDate = mView.findViewById(R.id.txtDateOrder);
        txtId = mView.findViewById(R.id.txtOrderID);
        txtTotal = mView.findViewById(R.id.txtTotalOI);

        amount = 0;
        Order order = getItem(position);
        for (OrderItem orderItem : orderItemList){
            if(order.getId() == orderItem.getOrderId()){
                amount += orderItem.getAmount();
            }
        }

        txtAmount.setText(amount+" item(s)");
        txtDate.setText(order.getDate());
        txtId.setText("ID: "+order.getId());
        txtTotal.setText("Total: $"+order.getTotal());

        return mView;
    }
}
