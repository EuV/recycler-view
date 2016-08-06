package ru.yandex.yamblz.ui.adapters;

public interface IContentAdapter {
    void changeItem(int position);
    void removeItem(int position);
    boolean swapItems(int from, int to);
}
