package ru.yandex.yamblz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.yandex.yamblz.R;
import ru.yandex.yamblz.ui.adapters.ContentAdapter;
import ru.yandex.yamblz.ui.adapters.ContentAdapter.ContentHolder;
import ru.yandex.yamblz.ui.other.BorderDecorator;
import ru.yandex.yamblz.ui.other.TagDecorator;
import ru.yandex.yamblz.ui.other.TouchCallback;

public class ContentFragment extends BaseFragment {
    private final BorderDecorator borderDecorator = new BorderDecorator();

    @BindView(R.id.rv)
    RecyclerView rv;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, container, false);
    }


    /**
     * We can't use {@link Adapter#setHasStableIds(boolean)} since items change.
     * Also, it gives no actual performance improvements in tests (used with
     * {@link Adapter#getItemId(int)}).
     * <p>
     * {@link RecyclerView#setHasFixedSize(boolean)} *disables* animation when columns
     * count changes, and gives no performance improvements in tests.
     * <p>
     * {@link RecyclerView#setItemViewCacheSize(int)} slows down application: in case of
     * fast 30-columns scrolling there will be lots of view holders we can't reuse unchanged.
     * <p>
     * {@link LinearLayoutManager#getExtraLayoutSpace(State)} on my device has no effect
     * in case of using a little extra space and adds lags when set to thousands of pixels.
     * <p>
     * Things really go better when we don't set text into view in {@link ContentHolder}
     * or when using {@link TextView#setMaxLines(int)} or {@link TextView#setSingleLine()}.
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rv.setAdapter(new ContentAdapter());
        rv.addItemDecoration(borderDecorator);
        rv.addItemDecoration(new TagDecorator());
        rv.getRecycledViewPool().setMaxRecycledViews(0, 100);

        new ItemTouchHelper(new TouchCallback(rv.getAdapter())).attachToRecyclerView(rv);
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
}
