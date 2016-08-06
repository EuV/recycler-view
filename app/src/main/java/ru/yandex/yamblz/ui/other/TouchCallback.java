package ru.yandex.yamblz.ui.other;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.view.View;

import ru.yandex.yamblz.ui.adapters.IContentAdapter;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;
import static android.support.v7.widget.helper.ItemTouchHelper.DOWN;
import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;
import static android.support.v7.widget.helper.ItemTouchHelper.UP;

public class TouchCallback extends Callback {
    private static final Paint PAINT = new Paint();

    static {
        PAINT.setColor(Color.RED);
    }

    private final IContentAdapter adapter;

    public TouchCallback(Adapter adapter) {
        if (!(adapter instanceof IContentAdapter)) {
            throw new IllegalArgumentException("Adapter must implement IContentAdapter interface!");
        }
        this.adapter = (IContentAdapter) adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
        return makeMovementFlags(LEFT | UP | RIGHT | DOWN, RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
        return adapter.swapItems(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }

    @Override
    public void onSwiped(ViewHolder viewHolder, int direction) {
        adapter.removeItem(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        if (actionState != ACTION_STATE_SWIPE) {
            return;
        }

        // Draw white -> red background when swiping
        View v = viewHolder.itemView;
        float factor = Math.min(dX / v.getWidth(), 1);
        PAINT.setAlpha((int) (factor * 255));
        c.drawRect(v.getLeft(), v.getTop(), v.getLeft() + Math.min(dX, v.getWidth()), v.getBottom(), PAINT);
    }
}
