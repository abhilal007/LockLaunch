package me.sparker0i.lawnchair.util;

import android.view.MotionEvent;

public interface TouchController {
    boolean onTouchEvent(MotionEvent ev);

    boolean onInterceptTouchEvent(MotionEvent ev);
}
