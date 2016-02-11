package com.acenosekai.antplayer.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acenosekai on 1/17/2016.
 * Rock On
 */
public class Folder {
    private String name;
    private String path;
    private List<Folder> childs = new ArrayList<>();
    private Folder parent;
    private boolean directory;

    public Folder getParent() {
        return parent;
    }

    public void setParent(Folder parrent) {
        this.parent = parrent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Folder> getChilds() {
        return childs;
    }

    public void setChilds(List<Folder> childs) {
        this.childs = childs;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }
}
