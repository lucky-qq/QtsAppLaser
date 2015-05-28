package com.zkc.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.zkc.barcodescan.R;
import com.zkc.domain.BoxWithBundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IT on 2015-04-15.
 */
public class BoxItemAdapter extends BaseAdapter {
    private float sourceX = 0;
    private LayoutInflater layoutInflater;
    private List<BoxWithBundle> boxes = new ArrayList<>();
    private BoxItemAction boxItemAction;

    public BoxItemAdapter(Context context, BoxItemAction boxItemAction) {
        this.boxItemAction = boxItemAction;
        layoutInflater = LayoutInflater.from(context);
    }

    public void add(BoxWithBundle box) {
        boxes.add(box);
        notifyDataSetChanged();
    }

    public void removeAll() {
        boxes.removeAll(boxes);
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        boxItemAction.afterDataSetChanged();
    }

    public boolean contains(BoxWithBundle box) {
        return boxes.contains(box);
    }

    @Override
    public int getCount() {
        return boxes.size();
    }

    @Override
    public BoxWithBundle getItem(int position) {
        return boxes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        BoxItem item;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.box_item, null);
            item = new BoxItem();
            item.uiBoxCode = (TextView) convertView.findViewById(R.id.box_code);
            item.uiBoxProduct = (TextView) convertView.findViewById(R.id.box_product);
            item.uiBoxNum = (TextView) convertView.findViewById(R.id.box_total_num);
            item.uiDelete = (Button) convertView.findViewById(R.id.box_delete);
            item.uiDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boxes.remove(position);
                    notifyDataSetChanged();
                }
            });
            convertView.setTag(item);
        } else {
            item = (BoxItem) convertView.getTag();
        }

        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                BoxItem item = (BoxItem) v.getTag();
                float currentX = event.getX();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        sourceX = currentX;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (currentX - sourceX <= -20 && item.uiDelete.getVisibility() == View.GONE) {
                            sourceX = currentX;
                            item.uiDelete.setVisibility(View.VISIBLE);
                            notifyDataSetChanged();
                        } else if (currentX - sourceX >= 20 && item.uiDelete.getVisibility() == View.VISIBLE) {
                            sourceX = currentX;
                            item.uiDelete.setVisibility(View.GONE);
                            notifyDataSetChanged();
                        }
                        break;
                }
                return true;
            }
        });

        item.uiBoxCode.setText(boxes.get(position).getBoxCode());
        item.uiBoxProduct.setText(boxes.get(position).getProduct());
        item.uiBoxNum.setText(Integer.toString(boxes.get(position).getTotalNum()));


        return convertView;
    }

    class BoxItem {
        TextView uiBoxCode;
        TextView uiBoxProduct;
        TextView uiBoxNum;
        Button uiDelete;
    }

    public interface BoxItemAction {
        void afterDataSetChanged();
    }
}