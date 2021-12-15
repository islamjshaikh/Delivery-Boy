package com.precloud.deliverystar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.precloud.deliverystar.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Home_Adapter extends ArrayAdapter<String> {
    private final Context mContext;
    private List<String> menuList;
    private final int mLayoutResourceId;

    public Home_Adapter(Context context, int resource, List<String> menuList) {
        super(context, resource, menuList);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.menuList =menuList;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                convertView = inflater.inflate(mLayoutResourceId, parent, false);
            }

        }
        TextView name = null;
        if (convertView != null) {
            name = convertView.findViewById(R.id.txt_menu);
                name.setText(menuList.get(position));

        }



        return convertView;
    }
}
