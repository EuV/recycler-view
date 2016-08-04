package ru.yandex.yamblz.ui.fragments;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ru.yandex.yamblz.R;
import ru.yandex.yamblz.ui.fragments.ContentAdapter.ContentHolder;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

class ContentAdapter extends Adapter<ContentHolder> {

    private final Random rnd = new Random();
    private final List<Integer> colors = new ArrayList<>();

    private int tagPositionA = NO_POSITION;
    private int tagPositionB = NO_POSITION;

    @Override
    public ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item, parent, false);
        ContentHolder holder = new ContentHolder(view);
        view.setOnClickListener(v -> change(holder.getAdapterPosition()));
        return holder;
    }


    @Override
    public void onBindViewHolder(ContentHolder holder, int position) {
        holder.bind(createColorForPosition(position));
    }


    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }


    public void remove(int position) {
        if (position == NO_POSITION) return;

        colors.remove(position);

        tagPositionA = getTagPosition(tagPositionA, position);
        tagPositionB = getTagPosition(tagPositionB, position);

        notifyItemRemoved(position);
    }


    public boolean swap(int from, int to) {
        if (from == to || from == NO_POSITION || to == NO_POSITION) {
            return false;
        }

        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(colors, i, i + 1);
            }
            tagPositionB = to - 1;
        } else {
            for (int i = from; i > to; i--) {
                Collections.swap(colors, i, i - 1);
            }
            tagPositionB = to + 1;
        }
        tagPositionA = to;

        notifyItemMoved(from, to);
        return true;
    }


    private void change(int position) {
        if (position == NO_POSITION) return;
        colors.set(position, generateColor());
        notifyItemChanged(position);
    }


    private int getTagPosition(int tagPosition, int removedPosition) {
        if (removedPosition == tagPosition) {
            return NO_POSITION;
        }

        if (removedPosition < tagPosition) {
            return Math.max(0, tagPosition - 1);
        }

        return tagPosition;
    }


    private Integer createColorForPosition(int position) {
        if (position >= colors.size()) {
            colors.add(generateColor());
        }
        return colors.get(position);
    }


    private int generateColor() {
        return Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
    }


    class ContentHolder extends ViewHolder {
        ContentHolder(View itemView) {
            super(itemView);
        }

        void bind(Integer color) {
            itemView.setBackgroundColor(color);
            ((TextView) itemView).setText("#".concat(Integer.toHexString(color).substring(2)));
        }

        public boolean isTagged() {
            return isTaggedA() || isTaggedB();
        }

        public boolean isTaggedA() {
            int pos = getAdapterPosition();
            return pos != NO_POSITION && pos == tagPositionA;
        }

        public boolean isTaggedB() {
            int pos = getAdapterPosition();
            return pos != NO_POSITION && pos == tagPositionB;
        }
    }
}
