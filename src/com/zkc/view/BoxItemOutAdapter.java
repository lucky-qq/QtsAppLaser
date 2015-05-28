package com.zkc.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zkc.barcodescan.R;
import com.zkc.domain.Box;
import com.zkc.domain.BoxWithMarkPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IT on 2015-04-15.
 */
public class BoxItemOutAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<BoxWithMarkPage> boxes = new ArrayList<>();

    public BoxItemOutAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setBoxes(List<BoxWithMarkPage> boxes) {
        this.boxes = boxes;
        notifyDataSetChanged();
    }

    public void clear() {
        boxes.removeAll(boxes);
        notifyDataSetChanged();
    }

    public int indexBox(String boxCode) {
        for (int i = 0; i < boxes.size(); i++) {
            BoxWithMarkPage box = boxes.get(i);
            if (box.getBoxCode().equals(boxCode)) {
                return i;
            }
        }
        return -1;
    }

    public boolean canOut() {
        for (BoxWithMarkPage box : boxes) {
            if (!Box.STATE_CAN_OUT.equals(box.getState())) {
                return false;
            }
        }
        return true;
    }


    @Override
    public int getCount() {
        return boxes.size();
    }

    @Override
    public BoxWithMarkPage getItem(int position) {
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
            convertView = layoutInflater.inflate(R.layout.box_item_out, null);
            item = new BoxItem();
            item.uiBoxCode = (TextView) convertView.findViewById(R.id.box_code);
            item.uiBoxProduct = (TextView) convertView.findViewById(R.id.box_product);
            item.uiBoxState = (TextView) convertView.findViewById(R.id.box_state);
            item.uiCheckedPage = (TextView) convertView.findViewById(R.id.checked_page);
            item.uiMarkPage = (TextView) convertView.findViewById(R.id.mark_page);
            convertView.setTag(item);
        } else {
            item = (BoxItem) convertView.getTag();
        }

        item.uiBoxCode.setText(boxes.get(position).getBoxCode());
        item.uiBoxProduct.setText(boxes.get(position).getProduct());
        item.uiBoxState.setText(boxes.get(position).getState());
        if (Box.STATE_CAN_OUT.equals(boxes.get(position).getState())) {
            item.uiBoxState.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            item.uiBoxState.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }
        item.uiCheckedPage.setText(boxes.get(position).getCheckedPage().toString());
        if (boxes.get(position).getCheckedPage() >= boxes.get(position).getMarkPage()) {
            item.uiCheckedPage.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            item.uiCheckedPage.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }
        item.uiMarkPage.setText(boxes.get(position).getMarkPage().toString());
        return convertView;
    }

    class BoxItem {
        TextView uiBoxCode;
        TextView uiBoxProduct;
        TextView uiBoxState;
        TextView uiCheckedPage;
        TextView uiMarkPage;
    }
}