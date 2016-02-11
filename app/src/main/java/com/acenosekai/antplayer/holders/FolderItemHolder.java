package com.acenosekai.antplayer.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.acenosekai.antplayer.R;
import com.mikepenz.iconics.view.IconicsImageView;

/**
 * Created by Acenosekai on 1/17/2016.
 * Rock On
 */
public class FolderItemHolder extends RecyclerView.ViewHolder {
    private TextView listText;
    private View itemView;
    private IconicsImageView listMenu;
    private IconicsImageView listIcon;

    public FolderItemHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.listText = (android.widget.TextView) itemView.findViewById(R.id.list_text);
        this.listMenu = (IconicsImageView) itemView.findViewById(R.id.menu_item);
        this.listIcon = (IconicsImageView) itemView.findViewById(R.id.item_icon);

    }

    public IconicsImageView getListMenu() {
        return listMenu;
    }

    public IconicsImageView getListIcon() {
        return listIcon;
    }

    public TextView getListText() {
        return listText;
    }

    public View getItemView() {
        return itemView;
    }
}
