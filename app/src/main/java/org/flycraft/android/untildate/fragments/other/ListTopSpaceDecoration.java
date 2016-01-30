package org.flycraft.android.untildate.fragments.other;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ListTopSpaceDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public ListTopSpaceDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(parent.getChildAdapterPosition(view) == 0)
            outRect.top = space;
    }
}