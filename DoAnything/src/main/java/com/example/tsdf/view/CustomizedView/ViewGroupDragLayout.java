package com.example.tsdf.view.CustomizedView;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by administrator on 12/2/15.
 */
public class ViewGroupDragLayout extends LinearLayout {
    private ViewDragHelper mViewDragHelper;
    private ViewGroupDragLayout rootView;
    private View mDragView;
    private View mAutoBackView;
    private View mEdgeTrackerView;
    private Point mAutoBackOriginPos = new Point();
    public ViewGroupDragLayout(Context context) {
        super(context);
    }

    public ViewGroupDragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        rootView = this;
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback(){
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (child == mEdgeTrackerView) {
                    int minleft = rootView.getPaddingLeft();
                    int maxleft = rootView.getWidth() - minleft - rootView.getPaddingRight() - child.getWidth();
                    if (left < minleft) {
                        return minleft;
                    } else if (left > maxleft) {
                        return maxleft;
                    }
                }
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                if (child == mEdgeTrackerView) {
                    int mintop = rootView.getPaddingTop();
                    int maxtop = rootView.getHeight() - mintop - rootView.getPaddingBottom() - child.getHeight();
                    if (top < mintop) {
                        return mintop;
                    } else if (top > maxtop) {
                        return maxtop;
                    }
                }
                return top;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                //mAutoBackView手指释放时可以自动回去
                if (releasedChild == mAutoBackView) {
                    mViewDragHelper.settleCapturedViewAt(mAutoBackOriginPos.x, mAutoBackOriginPos.y);
                    invalidate();
                } else {
                    super.onViewReleased(releasedChild, xvel, yvel);
                }
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                super.onEdgeDragStarted(edgeFlags, pointerId);
//                mViewDragHelper.captureChildView(mEdgeTrackerView, pointerId);
            }
        });
//        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        for(int i=0;i<count;i++) {
            if (i == 0) {
                mDragView = getChildAt(i);
            } else if (i == 1) {
                mAutoBackView = getChildAt(i);
            } else {
                mEdgeTrackerView = getChildAt(i);
            }
        }
    }

    @Override
    public void computeScroll() {
        if(mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mAutoBackOriginPos.x = mAutoBackView.getLeft();
        mAutoBackOriginPos.y = mAutoBackView.getTop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }
}
