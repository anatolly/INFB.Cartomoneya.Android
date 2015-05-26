package com.intrafab.cartomoneya.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.intrafab.cartomoneya.data.ShopBrand;

import java.util.List;

/**
 * Created by Artemiy Terekhov on 26.05.2015.
 */
public class ShopBrandAdapter extends ArrayAdapter<ShopBrand> {

    public ShopBrandAdapter(Context context, List<ShopBrand> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShopBrand item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(android.R.id.text1);
        tvName.setText(item.getName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        ShopBrand item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(android.R.id.text1);
        tvName.setText(item.getName());

        return convertView;
    }

//    private OnClickListener mListener;
//    private List<ShopBrand> mListItems = new ArrayList<ShopBrand>();
//
//    public interface OnClickListener {
//        public void onClickItem(ShopBrand itemShopBrand);
//    }
//
//    public ShopBrandAdapter(OnClickListener listener) {
//        mListener = listener;
//    }
//
//    @Override
//    public ShopBrand onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.view_card_list_item, parent, false);
//
//        return new ItemShopCardView(view, mListener);
//    }
//
//    @Override
//    public void onBindViewHolder(ItemShopCardView viewHolder, int i) {
//        final ShopCard item = mListItems.get(i);
//        viewHolder.setItem(item);
//    }
//
//    @Override
//    public int getItemCount() {
//        return mListItems.size();
//    }
//
//    public void add(ShopCard item) {
//        mListItems.add(item);
//    }
//
//    public void addAll(List<ShopCard> items) {
//        if (items != null)
//            mListItems.addAll(items);
//        notifyDataSetChanged();
//    }
//
//    public int size() {
//        return mListItems.size();
//    }
//
//    public void clear() {
//        mListItems.clear();
//    }
}
