package com.hunglq.map1.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hunglq.map1.MainActivity;
import com.hunglq.map1.R;
import com.hunglq.map1.model.AccountItem;
import com.hunglq.map1.model.Item;
import com.squareup.picasso.Picasso;

import static com.hunglq.map1.AccountItemActivity.deleteItemDialog;



public class AccountItemAdapter extends ArrayAdapter<AccountItem> {

    Activity context;
    int resource;
    View mView;
    ImageView imgItem, imgDelete;
    TextView txtAmount, txtName, txtDesciption;
    AccountItem accountItem;
    public static int amount;
    public static String itemName;
    public static int itemId;

    public AccountItemAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        mView = this.context.getLayoutInflater().inflate(this.resource, null);

        imgDelete = mView.findViewById(R.id.imgDeleteItem);
        imgItem = mView.findViewById(R.id.imgPictureItem);
        txtAmount = mView.findViewById(R.id.txtAmountItem);
        txtDesciption = mView.findViewById(R.id.txtDescriptionItem);
        txtName = mView.findViewById(R.id.txtNameItem);


        accountItem = getItem(position);

        txtAmount.setText("x" + accountItem.getAmount());

        final Item item = MainActivity.items.get(accountItem.getItemId() - 1);
        txtName.setText(item.getName());
        txtDesciption.setText(item.getDescription());
        Picasso.get()
                .load(item.getImage())
                .placeholder(android.R.drawable.ic_menu_report_image)
                .error(android.R.drawable.ic_menu_report_image)
                .into(imgItem);

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemName = item.getName();
                amount = getItem(position).getAmount();
                itemId = item.getId();
                deleteItemDialog.show();
            }
        });

        return mView;
    }


}
