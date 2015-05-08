package com.intrafab.cartomoneya.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Artemiy Terekhov on 08.05.2015.
 */
public class TextFitTextView extends TextView {

    static final String TAG = "TextFitTextView";
    boolean fit = false;

    public TextFitTextView(Context context) {
        super(context);
    }

    public TextFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextFitTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setFitTextToBox(Boolean fit) {
        this.fit = fit;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (fit) _shrinkToFit();
    }

    protected void _shrinkToFit() {

//        int height = this.getHeight();
//        int lines = this.getLineCount();
//        Rect r = new Rect();
//        int y1 = this.getLineBounds(0, r);
//        int y2 = this.getLineBounds(lines - 1, r);
//
//        float size = this.getTextSize();
//        if (y2 > height && size >= 8.0f) {
//            this.setTextSize(size - 2.0f);
//            this.setTextScaleX();
//            _shrinkToFit();
//        }

        //Rect bounds = new Rect();
        Paint textPaint = getPaint();
        //textPaint.getTextBounds(getText().toString(), 0, getText().toString().length(), bounds);
        float width = textPaint.measureText(getText().toString());
        //int height = bounds.height();
        //int width = bounds.width();
        this.setTextScaleX(getWidth()/width);
    }
}
