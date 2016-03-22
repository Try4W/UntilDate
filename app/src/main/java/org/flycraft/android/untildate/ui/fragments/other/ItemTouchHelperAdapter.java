package org.flycraft.android.untildate.ui.fragments.other;

import android.support.v7.widget.RecyclerView;

public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

    void onDragEnd(RecyclerView.ViewHolder holder);

}