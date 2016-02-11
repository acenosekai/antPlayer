package com.acenosekai.antplayer.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.acenosekai.antplayer.R;
import com.mikepenz.iconics.view.IconicsImageView;

/**
 * Created by Acenosekai on 1/16/2016.
 * Rock On
 */
public class StringRemovableItemHolder extends RecyclerView.ViewHolder {
    private TextView listText;
    private View itemView;
    private IconicsImageView removeItem;

    public StringRemovableItemHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.listText = (TextView) itemView.findViewById(R.id.list_text);
        this.removeItem = (IconicsImageView) itemView.findViewById(R.id.remove_item);
    }

    public TextView getListText() {
        return listText;
    }

    public View getItemView() {
        return itemView;
    }

    public IconicsImageView getRemoveItem() {
        return removeItem;
    }
}
