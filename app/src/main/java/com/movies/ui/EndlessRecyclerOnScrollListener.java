package com.movies.ui;

/**
 * Created by michaelpaschenko on 9/10/17.
 * Scroll listenter for endless scrolling
 */

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    public void setLoading(boolean mLoading) {
        this.mLoading = mLoading;
    }

    /**
     * True if we are still waiting for the last set of data to load.
     */
    private boolean mLoading = false;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int totalItemCount = recyclerView.getLayoutManager().getItemCount();
        int lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();

        if (!mLoading && (totalItemCount - 1 == lastVisibleItem)) {
            // End has been reached
            mLoading = true;
            onLoadMore();
        }
    }

    public abstract void onLoadMore();
}