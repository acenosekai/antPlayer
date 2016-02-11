package com.acenosekai.antplayer.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.acenosekai.antplayer.R;

/**
 * Created by Acenosekai on 1/16/2016.
 * Rock On
 */
public class StringItemHolder extends RecyclerView.ViewHolder {
    private TextView listText;
    private View itemView;

    public StringItemHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.listText = (TextView) itemView.findViewById(R.id.list_text);
    }

    public TextView getListText() {
        return listText;
    }

    public View getItemView() {
        return itemView;
    }

}
