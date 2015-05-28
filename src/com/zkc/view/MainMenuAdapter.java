package com.zkc.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zkc.barcodescan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IT on 2015-04-15.
 */
public class MainMenuAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<MainMenu> boxes = new ArrayList<>();

    public MainMenuAdapter(Context context, List<MainMenu> boxes) {
        this.boxes = boxes;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return boxes.size();
    }

    @Override
    public MainMenu getItem(int position) {
        return boxes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MainMenuItemView item;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.main_menu_item, null);
            item = new MainMenuItemView();
            item.uiMenuImage = (ImageView) convertView.findViewById(R.id.menu_icon);
            item.uiMenuTitle = (TextView) convertView.findViewById(R.id.menu_title);
            convertView.setTag(item);
        } else {
            item = (MainMenuItemView) convertView.getTag();
        }

        item.uiMenuImage.setImageResource(boxes.get(position).getImageResourceId());
        item.uiMenuTitle.setText(boxes.get(position).getTitleResourceId());
//        convertView.setBackgroundResource(boxes.get(position).getBgColorResourceId());
        return convertView;
    }

    class MainMenuItemView {
        ImageView uiMenuImage;
        TextView uiMenuTitle;
    }
}