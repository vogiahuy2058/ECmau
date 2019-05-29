package com.hunglq.map1.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hunglq.map1.MainActivity;
import com.hunglq.map1.R;
import com.hunglq.map1.model.AccountItem;
import com.hunglq.map1.model.Item;
import com.squareup.picasso.Picasso;

import static com.hunglq.map1.AccountItemActivity.deleteItemDialog;

public class ShopItemAdapter extends ArrayAdapter<Item> {
    Activity context;
    int resource;
    View mView;
    ImageView imgItem;
    TextView txtItemName,txtPrice;
    Item item;

    public ShopItemAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        mView = this.context.getLayoutInflater().inflate(this.resource, null);

        imgItem = mView.findViewById(R.id.imgItem);
        txtPrice = mView.findViewById(R.id.txtPrice);
        txtItemName = mView.findViewById(R.id.txtItemName);

        item=getItem(position);

        txtItemName.setText(item.getName());
        txtPrice.setText(item.getPrice()+"");
        Picasso.get()
                .load(item.getImage())
                .placeholder(android.R.drawable.ic_menu_report_image)
                .error(android.R.drawable.ic_menu_report_image)
                .into(imgItem);

        return mView;
    }

}
