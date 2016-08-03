package ru.yandex.yamblz.ui.fragments;

import android.content.res.Resources;
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

        public TouchCallback(ContentAdapter adapter) {
            this.adapter = adapter;
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
    }


    private static class BorderDecorator extends ItemDecoration {
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

        private static int dpToPx(float dp) {
            return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
        }
    }
}
