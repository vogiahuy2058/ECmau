package com.hunglq.map1.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hunglq.map1.R;
import com.hunglq.map1.model.Item;
import com.hunglq.map1.model.OrderItem;
import com.squareup.picasso.Picasso;

import static com.hunglq.map1.MainActivity.items;

public class OrderItemAdapter extends ArrayAdapter<OrderItem> {
    Activity context;
    int resource;
    View mView;
    ImageView imgItem;
    TextView txtName, txtAmount, txtPrice;

    public OrderItemAdapter(Activity context, int resource) {
        super(context, resource);
        this.resource = resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mView = this.context.getLayoutInflater().inflate(this.resource, null);

        txtName = mView.findViewById(R.id.txtNameOHI);
        txtAmount = mView.findViewById(R.id.txtAmountOHI);
        txtPrice = mView.findViewById(R.id.txtPriceOHI);
        imgItem = mView.findViewById(R.id.imgOHI);

        OrderItem orderItem = getItem(position);
        Item item = items.get(orderItem.getItemId() - 1);

        txtName.setText(item.getName());
        txtPrice.setText("$"+item.getPrice());
        txtAmount.setText("x"+orderItem.getAmount());
        Picasso.get()
                .load(item.getImage())
                .placeholder(android.R.drawable.ic_menu_report_image)
                .error(android.R.drawable.ic_menu_report_image)
                .into(imgItem);

        return  mView;
    }
}
