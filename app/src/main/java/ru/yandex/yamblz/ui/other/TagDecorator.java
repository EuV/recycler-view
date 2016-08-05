package ru.yandex.yamblz.ui.other;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

import ru.yandex.yamblz.ui.adapters.ContentAdapter.ContentHolder;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static ru.yandex.yamblz.util.Converter.dpToPx;

public class TagDecorator extends ItemDecoration {
    private static final int TAG_RADIUS_DP = 2;
    private static final int TAG_RADIUS_PX = dpToPx(TAG_RADIUS_DP);

    private static final Paint PAINT_A = new Paint();
    private static final Paint PAINT_B = new Paint();
    private static final Paint PAINT_BORDER = new Paint(ANTI_ALIAS_FLAG);

    static {
        PAINT_A.setColor(Color.RED);
        PAINT_B.setColor(Color.YELLOW);
        PAINT_BORDER.setColor(Color.BLACK);
        PAINT_BORDER.setStrokeWidth(TAG_RADIUS_PX / 3);
        PAINT_BORDER.setStyle(Style.STROKE);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, State state) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);

            ContentHolder holder = (ContentHolder) parent.getChildViewHolder(view);
            if (!holder.isTagged()) {
                continue;
            }

            float left = view.getLeft() + (view.getWidth() / 2) + view.getTranslationX();
            float top = view.getTop() + (view.getHeight() / 13) + view.getTranslationY();
            c.drawCircle(left, top, TAG_RADIUS_PX, holder.isTaggedA() ? PAINT_A : PAINT_B);
            c.drawCircle(left, top, TAG_RADIUS_PX, PAINT_BORDER);
        }
    }
}
