package org.flycraft.android.untildate.fragments.other;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

    void onDragEnd(RecyclerView.ViewHolder holder);

}