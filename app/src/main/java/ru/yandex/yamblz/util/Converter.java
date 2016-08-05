package ru.yandex.yamblz.util;

import android.content.res.Resources;

public final class Converter {
    public static int dpToPx(float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
