
package com.example.tsdf.view.CustomizedView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class CustomHorizontalScrollView extends HorizontalScrollView {

    public interface OnOverScrolledListener {
        void onOverScrolled(int scrollX, boolean clampedX);
    }

    private OnOverScrolledListener mOnOverScrolledListener;

    public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
            boolean clampedY) {
        // TODO Auto-generated method stub
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (mOnOverScrolledListener != null) {
            mOnOverScrolledListener.onOverScrolled(scrollX, clampedX);
        }
    }

    public void setOnOverScrolledListener(OnOverScrolledListener l) {
        mOnOverScrolledListener = l;
    }
}
