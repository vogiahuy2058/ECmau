package com.hunglq.map1.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hunglq.map1.R;
import com.hunglq.map1.model.Cart;
import com.hunglq.map1.model.Item;
import com.squareup.picasso.Picasso;

import static com.hunglq.map1.MainActivity.items;

public class CartAdapter extends ArrayAdapter<Cart> {
    Activity context;
    int resource;
    View mView;
    ImageView imgItem, imgPlus, imgMinus, imgDelete;
    TextView txtName, txtAmount, txtPrice;
    int amount;

    public CartAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        mView = this.context.getLayoutInflater().inflate(this.resource, null);

        imgItem = mView.findViewById(R.id.imgCartItem);
        imgDelete = mView.findViewById(R.id.imgDeleteCI);
        imgPlus = mView.findViewById(R.id.imgIncreaseCI);
        imgMinus = mView.findViewById(R.id.imgDecreaseCI);
        txtAmount = mView.findViewById(R.id.txtAmountCI);
        txtName = mView.findViewById(R.id.txtNameCI);
        txtPrice = mView.findViewById(R.id.txtPriceCI);

        Cart cart = getItem(position);
        Item item = items.get(cart.getItemid() - 1);

        txtName.setText(item.getName());
        txtPrice.setText(item.getPrice()+"");
        txtAmount.setText(cart.getAmount()+"");
        amount = cart.getAmount();
        Picasso.get()
                .load(item.getImage())
                .placeholder(android.R.drawable.ic_menu_report_image)
                .error(android.R.drawable.ic_menu_report_image)
                .into(imgItem);

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(getItem(position));
            }
        });

        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = 1;
                txtAmount.setText(amount+"");
            }
        });

        return mView;
    }
}
