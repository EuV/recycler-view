package ru.yandex.yamblz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.OnClick;
import ru.yandex.yamblz.R;

public class ContentFragment extends BaseFragment {

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
        rv.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rv.setAdapter(new ContentAdapter());
        rv.getRecycledViewPool().setMaxRecycledViews(0, 100);
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
