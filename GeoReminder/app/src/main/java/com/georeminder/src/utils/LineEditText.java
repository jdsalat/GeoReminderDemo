package com.georeminder.src.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

import com.georeminder.src.R;

/**
 * Created by Javed.Salat on 26-Sep-16.
 */
public class LineEditText extends EditText {
    // we need this constructor for LayoutInflater
    public LineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(context.getResources().getColor(R.color.ColorPrimary));
    }

    private Rect mRect;
    private Paint mPaint;

    @Override
    protected void onDraw(Canvas canvas) {

        int height = getHeight();
        int line_height = getLineHeight();

        int count = height / line_height;

        if (getLineCount() > count)
            count = getLineCount();

        Rect r = mRect;
        Paint paint = mPaint;
        int baseline = getLineBounds(0, r);

        for (int i = 0; i < count; i++) {

            canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
            baseline += getLineHeight();

            super.onDraw(canvas);
        }
    }
}
