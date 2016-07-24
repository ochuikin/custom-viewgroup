package ru.yandex.yamblz.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by olegchuikin on 24/07/16.
 */

public class HorizontalLinearLayout extends ViewGroup {

    private final String TAG = "HorizontalLinearLayout";

    public HorizontalLinearLayout(Context context) {
        super(context);
    }

    public HorizontalLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Log.d(TAG, "HorizontalLinearLayout.onMeasure()");

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int totalHeight = 0;
        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);

        View wideView = null;
        int wideViewWidth = totalWidth;

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            LayoutParams layoutParams = child.getLayoutParams();

            if (layoutParams.width == LayoutParams.MATCH_PARENT && wideView != null){
                throw new RuntimeException("Should be only one MATCH_PARENT child!");
            }

            if (layoutParams.width == LayoutParams.MATCH_PARENT){
                wideView = child;
            } else {
                wideViewWidth -= child.getMeasuredWidth();
            }

            totalHeight = Math.max(totalHeight, child.getMeasuredHeight());
        }

        if (wideView != null){
            int wideViewWidthSpec = MeasureSpec.makeMeasureSpec(wideViewWidth, MeasureSpec.EXACTLY);
            int wideViewHeightSpec = MeasureSpec.makeMeasureSpec(wideView.getMeasuredHeight(), MeasureSpec.EXACTLY);
            wideView.measure(wideViewWidthSpec, wideViewHeightSpec);
        }

        setMeasuredDimension(totalWidth, totalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childCount = getChildCount();

        int left = 0;

        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);

            child.layout(left, 0, left + child.getMeasuredWidth(), child.getMeasuredHeight());

            left += child.getMeasuredWidth();

        }

    }
}
