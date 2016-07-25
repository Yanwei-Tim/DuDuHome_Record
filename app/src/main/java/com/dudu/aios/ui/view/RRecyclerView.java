package com.dudu.aios.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * 简单封装的RecyclerView
 * <p>
 * Created by angcyo on 16-03-01-001.
 */
public class RRecyclerView extends RecyclerView {
    private int orientation = LinearLayoutManager.HORIZONTAL;

    public RRecyclerView(Context context) {
        this(context, null);
    }

    public RRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        String tag = (String) this.getTag();
        if (!TextUtils.isEmpty(tag) && "V".equalsIgnoreCase(tag)) {
            orientation = LinearLayoutManager.VERTICAL;
        }
        this.setLayoutManager(new LinearLayoutManager(context, orientation, false));
        this.setItemAnimator(new DefaultItemAnimator());

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Adapter adapter = getAdapter();
                if (adapter != null && adapter instanceof RBaseAdapter) {
                    ((RBaseAdapter) adapter).onScrollStateChanged(recyclerView, newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Adapter adapter = getAdapter();
                if (adapter != null && adapter instanceof RBaseAdapter) {
                    ((RBaseAdapter) adapter).onScrolled(recyclerView, dx, dy);
                }
            }
        });
    }


}
