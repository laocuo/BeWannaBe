package com.example.tsdf.view.CustomizedView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.tsdf.R;
import com.example.tsdf.utils.L;

/**
 * Created by administrator on 12/8/15.
 */
public class PokaViewGroup extends FrameLayout {
    private int mleftSize,mtopSize;
    public PokaViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.getResources().obtainAttributes(attrs, R.styleable.namePokaViewGroup);
        mleftSize = typedArray.getInt(R.styleable.namePokaViewGroup_leftSize, 10);
        mtopSize = typedArray.getInt(R.styleable.namePokaViewGroup_topSize, 10);
        typedArray.recycle();
        L.d("mleftSize" + mleftSize);
        L.d("mtopSize"+mtopSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        for(int i=0;i<count;i++) {
            View child = getChildAt(i);
            child.getLayoutParams();
            L.d("child.getMeasuredWidth()" + child.getMeasuredWidth());
            L.d("child.getMeasuredHeight()" + child.getMeasuredHeight());
            L.d("child.getWidth()" + child.getWidth());
            L.d("child.getHeight()" + child.getHeight());
            PokaItemLayoutParams p = new PokaItemLayoutParams(child.getLayoutParams());
            p.setLeftAndTop(i*mleftSize, i*mtopSize);
            child.setLayoutParams(p);
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for(int i=0;i<count;i++) {
            View child = getChildAt(i);
            PokaItemLayoutParams p = (PokaItemLayoutParams) child.getLayoutParams();
            child.layout(l+p.mLeftSize, t+p.mTopSize, l+p.mLeftSize+child.getMeasuredWidth(), t+p.mTopSize+child.getMeasuredHeight());
        }
//        super.onLayout(changed, l, t, r, b);
    }

    private class PokaItemLayoutParams extends ViewGroup.LayoutParams {

        private int mLeftSize,mTopSize;

        public PokaItemLayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public void setLeftAndTop(int l, int t) {
            mLeftSize = l;
            mTopSize = t;
        }
    }
}
