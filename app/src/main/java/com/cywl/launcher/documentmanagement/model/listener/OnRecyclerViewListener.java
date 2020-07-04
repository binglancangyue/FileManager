package com.cywl.launcher.documentmanagement.model.listener;

public interface OnRecyclerViewListener {
    void onItemClickListener(int position, String path);

    void onItemLongClickListener(int position, String path);
}
