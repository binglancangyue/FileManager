package com.cywl.launcher.filemanager.model.listener;

import android.view.View;


public interface OnRecyclerViewItemListener {
    void onItemClickListener(View view, int position);

    void onItemLongClickListener(View view, int position);
}
