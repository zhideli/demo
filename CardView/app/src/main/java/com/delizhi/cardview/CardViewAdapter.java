package com.delizhi.cardview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by deli.zhi on 2017/10/23.
 */

class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {

    Context mContext;
    List<String> contents;

    public CardViewAdapter(Context context,List<String> contents){
        mContext = context;
        this.contents = contents;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder mViewHolder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.card_item,parent,false));
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(contents.get(position));
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.cardview);
        }
    }
}
