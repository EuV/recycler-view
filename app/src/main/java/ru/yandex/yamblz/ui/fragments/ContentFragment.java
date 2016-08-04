package ru.yandex.yamblz.ui.fragments;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.OnClick;
import ru.yandex.yamblz.R;
import ru.yandex.yamblz.ui.fragments.ContentAdapter.ContentHolder;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;
import static android.support.v7.widget.helper.ItemTouchHelper.DOWN;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;
import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.UP;

public class ContentFragment extends BaseFragment {
    private final BorderDecorator borderDecorator = new BorderDecorator();

    @BindView(R.id.rv)
    RecyclerView rv;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ContentAdapter adapter = new ContentAdapter();
        rv.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rv.addItemDecoration(borderDecorator);
        rv.setAdapter(adapter);
        rv.getRecycledViewPool().setMaxRecycledViews(0, 100);
        new ItemTouchHelper(new TouchCallback(adapter)).attachToRecyclerView(rv);
    }


    @OnClick(R.id.minus)
    void decreaseSpanCount() {
        setSpanCount(Math.max(getSpanCount() - 1, 1));

    }


    @OnClick(R.id.plus)
    void increaseSpanCount() {
        setSpanCount(Math.min(getSpanCount() + 1, 30));
    }


    @OnClick(R.id.one)
    void setOneSpan() {
        setSpanCount(1);
    }


    @OnClick(R.id.thirty)
    void setThirtySpans() {
        setSpanCount(30);
    }


    @OnClick(R.id.stylish)
    void changeRecyclerStyle() {
        borderDecorator.changeStyle();
        rv.invalidateItemDecorations();
    }


    private int getSpanCount() {
        return ((GridLayoutManager) rv.getLayoutManager()).getSpanCount();
    }


    private void setSpanCount(int count) {
        GridLayoutManager manager = (GridLayoutManager) rv.getLayoutManager();
        if (count == manager.getSpanCount()) return;
        manager.setSpanCount(count);
        rv.getAdapter().notifyItemRangeChanged(0, 0);
    }


    private static class TouchCallback extends Callback {
        private final ContentAdapter adapter;
        private final Paint paint = new Paint();

        public TouchCallback(ContentAdapter adapter) {
            this.adapter = adapter;
            paint.setColor(Color.RED);
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
            return makeMovementFlags(LEFT | UP | RIGHT | DOWN, RIGHT);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
            return adapter.swap(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }

        @Override
        public void onSwiped(ViewHolder viewHolder, int direction) {
            adapter.remove(viewHolder.getAdapterPosition());
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ACTION_STATE_SWIPE) {
                View v = viewHolder.itemView;
                float factor = Math.min(dX / v.getWidth(), 1);
                paint.setAlpha((int) (factor * 255));
                c.drawRect(v.getLeft(), v.getTop(), v.getLeft() + Math.min(dX, v.getWidth()), v.getBottom(), paint);
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }


    private static class BorderDecorator extends ItemDecoration {
        private static final int BORDER_SIZE_DP = 2;
        private static final int BORDER_SIZE_PX = dpToPx(BORDER_SIZE_DP);

        private static final int MARK_RADIUS_DP = 2;
        private static final int MARK_RADIUS_PX = dpToPx(MARK_RADIUS_DP);

        private static final Paint PAINT_A = new Paint(ANTI_ALIAS_FLAG);
        private static final Paint PAINT_B = new Paint(ANTI_ALIAS_FLAG);
        private static final Paint PAINT_BORDER = new Paint(ANTI_ALIAS_FLAG);

        static {
            PAINT_A.setColor(Color.RED);
            PAINT_B.setColor(Color.YELLOW);
            PAINT_BORDER.setColor(Color.BLACK);
            PAINT_BORDER.setStrokeWidth(MARK_RADIUS_PX / 3);
            PAINT_BORDER.setStyle(Style.STROKE);
        }

        private boolean hasBorder = false;

        public void changeStyle() {
            hasBorder = !hasBorder;
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, State state) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                View v = parent.getChildAt(i);
                ContentHolder holder = (ContentHolder) parent.getChildViewHolder(v);
                if (holder.isTagged()) {
                    float left = v.getLeft() + (v.getWidth() / 2) + v.getTranslationX() - MARK_RADIUS_PX;
                    float top = v.getTop() + (v.getHeight() / 13) + v.getTranslationY();
                    c.drawCircle(left, top, MARK_RADIUS_PX, holder.isTaggedA() ? PAINT_A : PAINT_B);
                    c.drawCircle(left, top, MARK_RADIUS_PX, PAINT_BORDER);
                }
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
            boolean even = (parent.getChildAdapterPosition(view) % 2 == 0);
            int border = (even && hasBorder) ? BORDER_SIZE_PX : 0;
            outRect.set(border, border, border, border);
        }

        private static int dpToPx(float dp) {
            return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
        }
    }
}
