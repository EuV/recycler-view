package ru.yandex.yamblz.ui.adapters;

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
import ru.yandex.yamblz.ui.adapters.ContentAdapter.ContentHolder;

import static android.support.v7.widget.RecyclerView.NO_POSITION;
import static android.view.animation.AnimationUtils.loadAnimation;

public class ContentAdapter extends Adapter<ContentHolder> implements IContentAdapter {

    private final Random rnd = new Random();
    private final List<Integer> colors = new ArrayList<>();

    private int tagPositionA = NO_POSITION;
    private int tagPositionB = NO_POSITION;
    private boolean animationEnabled;

    @Override
    public ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item, parent, false);
        ContentHolder holder = new ContentHolder(view);
        view.setOnClickListener(v -> changeItem(holder.getAdapterPosition()));
        return holder;
    }


    @Override
    public void onBindViewHolder(ContentHolder holder, int position) {
        holder.bind(getOrCreateColorForPosition(position));
    }


    @Override
    public void onViewAttachedToWindow(ContentHolder holder) {
        if (animationEnabled) {
            holder.animate();
        }
    }


    @Override
    public void onViewDetachedFromWindow(ContentHolder holder) {
        holder.stopAnimation();
    }


    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }


    @Override
    public void changeItem(int position) {
        if (position == NO_POSITION) return;
        colors.set(position, generateColor());
        notifyItemChanged(position);
    }


    @Override
    public void removeItem(int position) {
        if (position == NO_POSITION) return;

        colors.remove(position);

        tagPositionA = getTagPosition(tagPositionA, position);
        tagPositionB = getTagPosition(tagPositionB, position);

        notifyItemRemoved(position);
    }


    @Override
    public boolean swapItems(int from, int to) {
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


    public void setAnimationEnabled(boolean enabled) {
        animationEnabled = enabled;
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


    private Integer getOrCreateColorForPosition(int position) {
        int size = colors.size();
        while (size++ <= position) {
            colors.add(generateColor());
        }
        return colors.get(position);
    }


    private int generateColor() {
        return Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
    }


    public class ContentHolder extends ViewHolder {
        ContentHolder(View itemView) {
            super(itemView);
        }

        void bind(Integer color) {
            itemView.setBackgroundColor(color);
            ((TextView) itemView).setText("#".concat(Integer.toHexString(color).substring(2)));
        }

        void animate() {
            itemView.startAnimation(loadAnimation(itemView.getContext(), R.anim.appearance));
        }

        void stopAnimation() {
            itemView.clearAnimation();
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
