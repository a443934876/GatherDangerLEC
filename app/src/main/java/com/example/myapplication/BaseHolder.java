package com.example.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

/**
 *
 */
public abstract class BaseHolder<T> extends RecyclerView.ViewHolder{


    public BaseHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(List<T> data, int position, IParamContainer container, PublishSubject<T> itemClick);
}
