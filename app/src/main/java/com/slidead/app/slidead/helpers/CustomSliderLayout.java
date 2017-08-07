package com.slidead.app.slidead.helpers;

import android.content.Context;
import android.view.MotionEvent;

import com.daimajia.slider.library.SliderLayout;

/**
 * Created by Syukur on 8/5/2017.
 */

public class CustomSliderLayout extends SliderLayout {

    public CustomSliderLayout(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return false;
    }
}
