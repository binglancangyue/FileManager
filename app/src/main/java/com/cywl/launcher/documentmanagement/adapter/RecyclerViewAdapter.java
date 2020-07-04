package com.cywl.launcher.documentmanagement.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cywl.launcher.documentmanagement.R;
import com.cywl.launcher.documentmanagement.model.CustomValue;
import com.cywl.launcher.documentmanagement.model.bean.FileBean;
import com.cywl.launcher.documentmanagement.model.listener.OnRecyclerViewListener;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<FileBean> mData;
    private OnRecyclerViewListener onItemListener;
    private int mItemCount = 15;

    public void setOnRecyclerViewItemListener(OnRecyclerViewListener listener) {
        this.onItemListener = listener;
    }


    public RecyclerViewAdapter(Context context, List<FileBean> mData) {
        this.mContext = context;
        this.mData = mData;
        this.mInflater = LayoutInflater.from(mContext);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_recyclerview_file_info, parent,
                false));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FileBean data = mData.get(position);
        int type = data.getType();
        holder.tvName.setText(data.getName());
        holder.tvTime.setText(data.getTime());
        if (type >= 0) {
            Glide.with(mContext).load(CustomValue.FILE_ICON[type]).into(holder.ivImg);
        } else {
            Glide.with(mContext).load(R.drawable.litter_net_dev).into(holder.ivImg);
        }
    }

    @Override
    public int getItemCount() {
        if (mData.size() > mItemCount) {
            return mItemCount;
        }
        return mData.size();
    }

    public void setData(List<FileBean> data) {
        mData = data;
    }

    public void setItemCount(int count) {
        mItemCount = count;
    }

    final class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImg;
        TextView tvName;
        TextView tvTime;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_icon);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_last_time);
            View.OnClickListener mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemListener != null) {
                        int positon = getAdapterPosition() - 1;
                        String path = mData.get(positon).getPath();
                        onItemListener.onItemClickListener(positon, path);
                    }
                }
            };
            itemView.setOnClickListener(mOnClickListener);
            View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemListener != null) {
                        int positon = getAdapterPosition() - 1;
                        String path = mData.get(positon).getPath();
                        onItemListener.onItemLongClickListener(positon, path);
                    }
                    return true;
                }
            };
            itemView.setOnLongClickListener(mOnLongClickListener);
        }
    }

}
