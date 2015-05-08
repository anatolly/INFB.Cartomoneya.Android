package com.intrafab.cartomoneya.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Artemiy Terekhov on 08.05.2015.
 */
public class HalfRelativeLayout extends RelativeLayout {

    public HalfRelativeLayout(Context context) {
        super(context);
    }

    public HalfRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HalfRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec / 2);
    }

}
