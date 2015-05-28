package com.zkc.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.zkc.barcodescan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IT on 2015-04-15.
 */
public class MarkCheckItemAdapter extends BaseAdapter {
    private Context context;
    private float sourceX = 0;
    private LayoutInflater layoutInflater;
    private String boxCode;
    private List<String> markCodes = new ArrayList<>();

    public MarkCheckItemAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
        notifyDataSetChanged();
    }

    public void add(String markCode) {
        markCodes.add(markCode);
        notifyDataSetChanged();
    }

    public Integer getRightMarkNum() {
        int num = 0;
        for (String markCode: markCodes) {
            if(boxCode != null && boxCode.length() == 11 && boxCode.startsWith("B") && boxCode.substring(1).equals(markCode.substring(1))) {
               num++;
            }
        }
        return num;
    }

    public void clear() {
        markCodes.removeAll(markCodes);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return markCodes.size();
    }

    @Override
    public String getItem(int position) {
        return markCodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MarkItem item;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.mark_item_check, null);
            item = new MarkItem();
            item.uiMarkCode = (TextView) convertView.findViewById(R.id.mark_code);
            item.uiMarkState = (ImageView) convertView.findViewById(R.id.mark_state);
            item.uiDelete = (Button) convertView.findViewById(R.id.mark_delete);
            item.uiDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    markCodes.remove(position);
                    notifyDataSetChanged();
                }
            });
            convertView.setTag(item);
        } else {
            item = (MarkItem) convertView.getTag();
        }

        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MarkItem item = (MarkItem) v.getTag();
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
//                    case MotionEvent.ACTION_UP:
//                        float destX = currentX;
//                        if (destX - sourceX <= -20 && item.uiDelete.getVisibility() == View.GONE) {
//                            item.uiDelete.setVisibility(View.VISIBLE);
//                            notifyDataSetChanged();
//                        } else if (destX - sourceX >= 20 && item.uiDelete.getVisibility() == View.VISIBLE) {
//                            item.uiDelete.setVisibility(View.GONE);
//                            notifyDataSetChanged();
//                        }
//                        break;
                }
                return true;
            }
        });

        item.uiMarkCode.setText(markCodes.get(position));

        if(boxCode != null && boxCode.length() == 11 && boxCode.startsWith("B")
                && markCodes.get(position).length() == 11 && markCodes.get(position).startsWith("M")
                && boxCode.substring(1).equals(markCodes.get(position).substring(1))) {
            item.uiMarkState.setImageDrawable(context.getResources().getDrawable(R.drawable.yes));
        } else {
            item.uiMarkState.setImageDrawable(context.getResources().getDrawable(R.drawable.no));
        }

        return convertView;
    }

    class MarkItem {
        TextView uiMarkCode;
        ImageView uiMarkState;
        Button uiDelete;
    }

}