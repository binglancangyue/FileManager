package com.cywl.launcher.filemanager.model.listener;

public interface OnRecyclerViewListener {
    void onItemClickListener(int position, String path);

    void onItemLongClickListener(int position, String path);
}
