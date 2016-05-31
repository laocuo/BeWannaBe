
package com.example.tsdf.view.CustomizedView;

import com.example.tsdf.R;
import com.example.tsdf.utils.LogUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class PowerCalculatorScrollItem extends HorizontalScrollView
        implements OnClickListener, OnLongClickListener {

    public interface ScrollItemClick {
        void OnScrollItemClick(int index, View v);
    }

    private Context mContext;
    private LinearLayout mll;
    private LayoutInflater mInflater;
    private int mPosition;
    private int mListItemW;
    private int mListItemH;
    private int mExpandItemW;
    private int mExpandItemH;
    PowerCalculatorListItem mListItem;
    LinearLayout mExpandItem;
    private float mStartX = 0;
    private ScrollState mScrollState = ScrollState.SCROLL_TO_LEFT;
    private ScrollItemClick mScrollItemClick;
    private PopupWindow mPop;

    // private ViewGroup firstChild = null;
    // private int subChildCount = 0;
    // private ArrayList<Integer> pointList = new ArrayList<Integer>();

    private enum ScrollState {
        SCROLL_TO_LEFT,
        SCROLL_TO_RIGHT
    }

    public PowerCalculatorScrollItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        setHorizontalScrollBarEnabled(false);
        mContext = context;
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
            boolean clampedY) {
        // TODO Auto-generated method stub
        // LogUtils.print("scrollX="+scrollX+" scrollY="+scrollY);
        // LogUtils.print("clampedX="+clampedX+" clampedY="+clampedY);
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    @Override
    protected void onFinishInflate() {
        // TODO Auto-generated method stub
        super.onFinishInflate();
        mll = (LinearLayout) findViewById(R.id.scroll_item_container);
        initView();
        initPopupWindow();
    }

    private void initView() {
        // TODO Auto-generated method stub
        mInflater = LayoutInflater.from(mContext);
        mListItem = (PowerCalculatorListItem) mInflater.inflate(
                R.layout.power_calculator_list_item, mll, false);
        measureView(mListItem, LogUtils.getScreenW(), 0);
        mListItemW = mListItem.getMeasuredWidth();
        mListItemH = mListItem.getMeasuredHeight();
        LayoutParams ll = new LayoutParams(mListItemW, mListItemH);
        mListItem.setOnClickListener(this);
        mListItem.setOnLongClickListener(this);
        mll.addView(mListItem, ll);
        mExpandItem = (LinearLayout) mInflater.inflate(R.layout.power_calculator_expand_item, mll,
                false);
        measureView(mExpandItem, 0, 0);
        mExpandItemW = mExpandItem.getMeasuredWidth();
        mExpandItemH = mExpandItem.getMeasuredHeight();
        ll = new LayoutParams(mExpandItemW, mExpandItemH);
        mExpandItem.setOnClickListener(this);
        mll.addView(mExpandItem);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                float distence = ev.getX() - mStartX;
                // LogUtils.print("onTouchEvent.ACTION_UP--distence="+distence);
                if (mScrollState == ScrollState.SCROLL_TO_LEFT && distence < 0
                        || mScrollState == ScrollState.SCROLL_TO_RIGHT && distence > 0) {
                    ScrollState dir = mScrollState;
                    if (Math.abs(distence) > mExpandItemW / 2) {
                        dir = getOppositeDir(mScrollState);
                        mScrollState = dir;
                    }
                    smoothScrollToDirection(dir);
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = ev.getX();
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private ScrollState getOppositeDir(ScrollState s) {
        // TODO Auto-generated method stub
        if (s == ScrollState.SCROLL_TO_LEFT) {
            return ScrollState.SCROLL_TO_RIGHT;
        } else {
            return ScrollState.SCROLL_TO_LEFT;
        }
    }

    private void smoothScrollToDirection(ScrollState s) {
        if (s == ScrollState.SCROLL_TO_LEFT) {
            smoothScrollTo(0, 0);
            // fullScroll(FOCUS_LEFT);
            // pageScroll(FOCUS_LEFT);
        } else {
            smoothScrollTo(mExpandItemW, 0);
            // fullScroll(FOCUS_RIGHT);
            // pageScroll(FOCUS_RIGHT);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // getChildInfo();
    }

    // public void getChildInfo() {
    // firstChild = (ViewGroup) getChildAt(0);
    // if(firstChild != null){
    // subChildCount = firstChild.getChildCount();
    // for(int i = 0;i < subChildCount;i++){
    // if(((View)firstChild.getChildAt(i)).getWidth() > 0){
    // pointList.add(((View)firstChild.getChildAt(i)).getLeft());
    // }
    // }
    // }
    // }

    public void wrapContent(int position, int mImageId) {
        // TODO Auto-generated method stub
        mPosition = position;
        mListItem.bindContent(mImageId);
    }

    private void measureView(View v, int w, int h) {
        int childWidthSpec = 0;
        int childHeightSpec = 0;
        ViewGroup.LayoutParams params = v.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
        }
        if (w > 0) {
            childWidthSpec = MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY);
        } else {
            if (params.width > 0) {
                childWidthSpec = MeasureSpec.makeMeasureSpec(params.width, MeasureSpec.EXACTLY);
            } else {
                childWidthSpec = MeasureSpec.makeMeasureSpec(params.width, MeasureSpec.UNSPECIFIED);
            }
        }
        if (h > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY);
        } else {
            if (params.height > 0) {
                childHeightSpec = MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY);
            } else {
                childHeightSpec = MeasureSpec.makeMeasureSpec(params.height,
                        MeasureSpec.UNSPECIFIED);
            }
        }
        v.measure(childWidthSpec, childHeightSpec);
    }

    public void setScrollItemClick(ScrollItemClick s) {
        mScrollItemClick = s;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v != null) {
            if (mScrollItemClick != null) {
                mScrollItemClick.OnScrollItemClick(mPosition, v);
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        // TODO Auto-generated method stub
        if (mScrollState == ScrollState.SCROLL_TO_LEFT && v != null) {
            LogUtils.print("onLongClick mPosition" + mPosition);
            if (!mPop.isShowing()) {
                mPop.showAsDropDown(this, 0, -1 * mListItemH, Gravity.RIGHT);
            }
            return true;
        } else {
            return false;
        }
    }

    private void initPopupWindow() {
        View v = mInflater.inflate(R.layout.power_calculator_popup_item, this, false);
        LayoutParams lp = (LayoutParams) v.getLayoutParams();
        // LogUtils.print("lp.width="+lp.width+" && lp.height"+lp.height);
        mPop = new PopupWindow(v, lp.width, lp.height);
        mPop.setFocusable(true);
        mPop.setAnimationStyle(R.style.PopupAnimation);
        mPop.setOutsideTouchable(true);
        v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                mPop.dismiss();
            }
        });
        // v.setOnKeyListener(new OnKeyListener() {
        // @Override
        // public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
        // // TODO Auto-generated method stub
        // LogUtils.print("onKey="+arg1);
        // if (arg2.getAction() == KeyEvent.ACTION_UP && arg1 == KeyEvent.KEYCODE_BACK) {
        // mPop.dismiss();
        // }
        // return false;
        // }});
    }
}
