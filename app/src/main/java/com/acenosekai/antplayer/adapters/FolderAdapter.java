package com.acenosekai.antplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acenosekai.antplayer.R;
import com.acenosekai.antplayer.holders.FolderItemHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acenosekai on 1/16/2016.
 * Rock On
 */
public class FolderAdapter extends RecyclerView.Adapter<FolderItemHolder> {

    private List<String> listPath;
    private String currentPath;
    private OnClickFolder onClickFolder;

    public FolderAdapter(String parentPath, OnClickFolder onClickFolder) {
        this.onClickFolder = onClickFolder;
        buildList(parentPath);
    }

    private void buildList(String parentPath) {
        this.currentPath = parentPath;
        this.listPath = new ArrayList<>();
        File f = new File(parentPath);
        for (File child : f.listFiles()) {
            if (child.isDirectory()) {
                listPath.add(child.getPath());
            }
        }
    }

    public void up() {
        try {
            String[] pathArr = currentPath.split("/");
//            if (pathArr.length > 1) {
            String lPath = pathArr[pathArr.length - 1];
            String parentPath = currentPath.substring(0, currentPath.length() - (lPath.length() + 1));
            buildList(parentPath);
            onClickFolder.onClickFolder(parentPath);
            notifyDataSetChanged();
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public FolderItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_folder, parent, false);
        return new FolderItemHolder(v);
    }

    @Override
    public void onBindViewHolder(FolderItemHolder holder, int position) {
        final String path = listPath.get(position);
        String[] pathArr = path.split("/");
        holder.getListText().setText(pathArr[pathArr.length - 1]);

        holder.getListText().setSelected(true);
        holder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    buildList(path);
                    onClickFolder.onClickFolder(path);
                    notifyDataSetChanged();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        holder.getListMenu().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return listPath.size();
    }


    public interface OnClickFolder {
        void onClickFolder(String path);
    }
}
