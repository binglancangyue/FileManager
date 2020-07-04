package com.cywl.launcher.documentmanagement.model.bean;

public class FileBean {
    private String name;
    private String path;
    private boolean isFile;
    private int type;
    private String time;

    public FileBean() {
    }

    public FileBean(String name, String path, boolean isFile, int type) {
        this.name = name;
        this.path = path;
        this.isFile = isFile;
        this.type = type;
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

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "FileBean{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", isFile=" + isFile +
                ", type=" + type +
                '}';
    }
}
