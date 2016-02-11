package com.acenosekai.antplayer.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.acenosekai.antplayer.R;
import com.mikepenz.iconics.view.IconicsImageView;

/**
 * Created by Acenosekai on 1/23/2016.
 * Rock On
 */
public class MusicItemHolder extends RecyclerView.ViewHolder {
    private TextView listText;
    private TextView listDesc;
    private TextView listLength;
    private IconicsImageView listMenu;
    private View itemView;

    public MusicItemHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.listText = (TextView) itemView.findViewById(R.id.list_text);
        this.listDesc = (TextView) itemView.findViewById(R.id.list_text_desc);
        this.listLength = (TextView) itemView.findViewById(R.id.list_text_length);
        this.listMenu = (IconicsImageView) itemView.findViewById(R.id.menu_item);
    }

    public TextView getListLength() {
        return listLength;
    }

    public TextView getListText() {
        return listText;
    }

    public TextView getListDesc() {
        return listDesc;
    }

    public IconicsImageView getListMenu() {
        return listMenu;
    }

    public View getItemView() {
        return itemView;
    }
}
