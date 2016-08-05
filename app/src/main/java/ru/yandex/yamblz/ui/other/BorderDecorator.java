package ru.yandex.yamblz.ui.other;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

import static ru.yandex.yamblz.util.Converter.dpToPx;

public class BorderDecorator extends ItemDecoration {
    private static final int BORDER_SIZE_DP = 2;
    private static final int BORDER_SIZE_PX = dpToPx(BORDER_SIZE_DP);

    private boolean hasBorder = false;

    public void changeStyle() {
        hasBorder = !hasBorder;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        boolean even = (parent.getChildAdapterPosition(view) % 2 == 0);
        int border = (even && hasBorder) ? BORDER_SIZE_PX : 0;
        outRect.set(border, border, border, border);
    }
}
