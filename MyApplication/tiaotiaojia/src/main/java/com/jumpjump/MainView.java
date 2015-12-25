/*
* Copyright (C) 2013-2016 laocuo@163.com .
* Modification based on code covered by the mentioned copyright
* and/or permission notice(s).
*/
/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jumpjump;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jumpjump.MainActivity.WIFI_CONNECT_OWNER;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("Assert")
public class MainView extends View {

    private ImageView selectedChess, currentChess;
    private ListView chessRecordList;
    private BaseAdapter mAdapter;
    private List<String> mChessRecData;
    public static final int UPDATE = 0x100;
    public static final int CHECKEND = 0x101;
    public static final int COM_UPDATE = 0x102;
    public static final int COM_CHECKEND = 0x103;
    public static final int GAMEOVER = 0x104;
    public static final int COM_PLAY_ANI = 0x105;
    public static final int INVALIDATE = 0x106;

    private final int DELAYMILLS = 500;

    private boolean bDisableToutch = false;
    private boolean bisComRuning = false;

    private final int JJ_IDLE = 0x101;
    private final int JJ_SELECTED = 0x102;

    private int mainState = 0;
    private int selectedId = -1;

    private ArrayList<Point> point_list;
    private ArrayList<Point> point_list_pre;

    private final int SON_NULL = 0x0;
    private final int SON_GREEN = 0x1;
    private final int SON_RED = 0x2;

    private int last_touched_son = SON_GREEN;

    private Context mContext = null;
    // private int screen_width;
    private int son_width;
    private int son_height;
    private Bitmap GreenBitmap = null;
    private Bitmap RedBitmap = null;
    private Bitmap YellowBitmap = null;

    private ArrayList<Integer> width_list = null;
    private ArrayList<Integer> height_list = null;

    private ServiceHandler servicehandler;

    private Paint p;
    private WIFI_CONNECT_OWNER mWIFI_CONNECT_OWNER = WIFI_CONNECT_OWNER.IDLE;
    private MainActivity mMainActivity;

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        if (width_list != null) {
            Refresh_All(canvas);
        }
    }

    private void Refresh_All(Canvas canvas) {
        // TODO Auto-generated method stub
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                Point point = point_list.get(i * 5 + j);
                if (!(point.isSelected && selectedChess.isShown())) {
                    canvas_draw_bmp(point, canvas, width_list.get(j), height_list.get(i), p);
                }
            }
        }
    }

    private void canvas_draw_bmp(Point point, Canvas canvas, Integer integer,
            Integer integer2, Paint p) {
        // TODO Auto-generated method stub
        if (true == point.isSelected) {
            canvas.drawBitmap(YellowBitmap, integer, integer2, p);
            return;
        }

        if (point.dir == SON_GREEN) {
            canvas.drawBitmap(GreenBitmap, integer, integer2, p);
        } else if (point.dir == SON_RED) {
            canvas.drawBitmap(RedBitmap, integer, integer2, p);
        }
    }

    public MainView(Context context, AttributeSet attr) {
        super(context, attr);
        // TODO Auto-generated constructor stub
        mContext = context;
        p = new Paint();
//        AnimatorSet anSet = new AnimatorSet();
        initView();
        mainState = JJ_IDLE;
        servicehandler = new ServiceHandler();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
    }

    private void initView() {
        // TODO Auto-generated method stub
        // DisplayMetrics dm = getResources().getDisplayMetrics();
        // screen_width = dm.widthPixels;
        GreenBitmap = FactoryInterface.getBitmap(mContext, R.drawable.greenstar);
        RedBitmap = FactoryInterface.getBitmap(mContext, R.drawable.redstar);
        YellowBitmap = FactoryInterface.getBitmap(mContext, R.drawable.yellowstar);
        son_width = GreenBitmap.getWidth();
        son_height = GreenBitmap.getHeight();

        point_list = new ArrayList<Point>();
        point_list_pre = new ArrayList<Point>();

        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                Point p = new Point();
                p.x = i;
                p.y = j;
                if (i == 0)
                {
                    p.dir = SON_GREEN;
                }
                if (i == 4)
                {
                    p.dir = SON_RED;
                }
                point_list.add(p);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (true == bDisableToutch)
        {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                return handleActionDownEvenet(event);
            }

            case MotionEvent.ACTION_MOVE: {
                break;
            }

            case MotionEvent.ACTION_UP: {
                return handleActionUpEvent(event);
            }
        }
        return super.onTouchEvent(event);
    }

    private boolean handleActionUpEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        boolean ret = true;
        switch (mainState)
        {
            case JJ_IDLE: {
                int i = getSelectSon(event);
                if (i >= 0)
                {
                    selectedId = i;
                    point_list.get(selectedId).isSelected = true;
                    mainState = JJ_SELECTED;
                    invalidate();
                }
                break;
            }

            case JJ_SELECTED: {
                int i = getSelectSpace(event);
                if (i >= 0)
                {
                    boolean b = judgeLine(point_list.get(selectedId), point_list.get(i), point_list);
                    if (true == b)
                    {
                        saveCurrent();
//                        moveSon(point_list.get(selectedId), point_list.get(i));
                        bDisableToutch = true;
                        final int end = i;
                        playAnimation(point_list.get(selectedId), point_list.get(i), new EmptyAnimator(){

                            @Override
                            public void onAnimationStart(Animator animation) {
                                // TODO Auto-generated method stub
                                selectedChess.setVisibility(View.VISIBLE);
                                servicehandler.sendEmptyMessage(INVALIDATE);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                // TODO Auto-generated method stub
                                selectedChess.setVisibility(View.GONE);
                                servicehandler.sendEmptyMessage(INVALIDATE);
                                moveSon(point_list.get(selectedId), point_list.get(end));
                                String rec = saveChessRecordList(point_list.get(end).dir, selectedId,end);
                                if (rec != null) {
                                    mMainActivity.notifyAnotherThread(rec);
                                }
                                Message m = servicehandler.obtainMessage();
                                m.what = UPDATE;
                                m.arg1 = end;
                                servicehandler.sendMessage(m);
                            }});
                    }
                }
                else
                {
                    back_to_init();
                }
                break;
            }
        }

        return ret;
    }

    private void playAnimation(Point s, Point e, AnimatorListener l) {
        // TODO Auto-generated method stub
        AnimatorSet anSet = new AnimatorSet();
        if (s.y == e.y && s.x == e.x) {
            return;
        }
        if (bisComRuning == true) {
            if (s.dir == SON_RED) {
                selectedChess.setImageResource(R.drawable.redstar);
            }
            if (s.dir == SON_GREEN) {
                selectedChess.setImageResource(R.drawable.greenstar);
            }
        } else {
            selectedChess.setImageResource(R.drawable.yellowstar);
        }
        selectedChess.setX(width_list.get(s.y));
        selectedChess.setY(height_list.get(s.x));
        ObjectAnimator moveAnimatorY=null, moveAnimatorX=null;
        if (s.x != e.x) {
            moveAnimatorY = ObjectAnimator.ofFloat(selectedChess, "translationY", height_list.get(s.x), height_list.get(e.x));
            moveAnimatorY.setDuration(DELAYMILLS);
        }
        if (s.y != e.y) {
            moveAnimatorX = ObjectAnimator.ofFloat(selectedChess, "translationX", width_list.get(s.y), width_list.get(e.y));
            moveAnimatorX.setDuration(DELAYMILLS);
        }
        if (moveAnimatorY != null && moveAnimatorX != null) {
            anSet.playTogether(moveAnimatorY, moveAnimatorX);
        } else if (moveAnimatorY != null) {
            anSet.play(moveAnimatorY);
        } else if (moveAnimatorX != null) {
            anSet.play(moveAnimatorX);
        }
        anSet.addListener(l);
        anSet.start();
    }

    private boolean handleActionDownEvenet(MotionEvent event) {
        // TODO Auto-generated method stub
        boolean ret = true;
        switch (mainState)
        {
            case JJ_IDLE: {

                break;
            }

            case JJ_SELECTED: {

                break;
            }
        }

        return ret;
    }

    private void eatSon(int i, ArrayList<Point> plist) {
        // TODO Auto-generated method stub
        Point p = plist.get(i);

        int pre_x = p.x - 1;
        int las_x = p.x + 1;
        int pre_y = p.y - 1;
        int las_y = p.y + 1;

        if (pre_x >= 0 && las_x <= 4)
        {
            judgeTwoPointInLine(plist.get(pre_x * 5 + p.y), p, plist.get(las_x * 5 + p.y), plist);
        }

        if (pre_y >= 0 && las_y <= 4)
        {
            judgeTwoPointInLine(plist.get(p.x * 5 + pre_y), p, plist.get(p.x * 5 + las_y), plist);
        }

        if (pre_x >= 0 && pre_y >= 0 && las_x <= 4 && las_y <= 4)
        {
            // ���ĸ���б������������
            if (!((p.x == 1 && p.y == 2) || (p.x == 3 && p.y == 2) || (p.x == 2 && p.y == 1) || (p.x == 2 && p.y == 3)))
            {
                judgeTwoPointInLine(plist.get(pre_x * 5 + pre_y), p, plist.get(las_x * 5 + las_y),
                        plist);
                judgeTwoPointInLine(plist.get(pre_x * 5 + las_y), p, plist.get(las_x * 5 + pre_y),
                        plist);
            }
        }

        int pre_x_2 = p.x - 2;
        int las_x_2 = p.x + 2;
        int pre_y_2 = p.y - 2;
        int las_y_2 = p.y + 2;

        if (pre_x_2 >= 0)
        {
            judgeTwoPointInMiddle(plist.get(pre_x_2 * 5 + p.y), plist.get(pre_x * 5 + p.y), p,
                    plist);
        }

        if (las_x_2 <= 4)
        {
            judgeTwoPointInMiddle(p, plist.get(las_x * 5 + p.y), plist.get(las_x_2 * 5 + p.y),
                    plist);
        }

        if (pre_y_2 >= 0)
        {
            judgeTwoPointInMiddle(plist.get(p.x * 5 + pre_y_2), plist.get(p.x * 5 + pre_y), p,
                    plist);
        }

        if (las_y_2 <= 4)
        {
            judgeTwoPointInMiddle(p, plist.get(p.x * 5 + las_y), plist.get(p.x * 5 + las_y_2),
                    plist);
        }

        if (pre_x_2 >= 0 && pre_y_2 >= 0)
        {
            Point mid = plist.get(pre_x * 5 + pre_y);
            if (!((mid.x == 1 && mid.y == 2) || (mid.x == 3 && mid.y == 2)
                    || (mid.x == 2 && mid.y == 1) || (mid.x == 2 && mid.y == 3)))
            {
                judgeTwoPointInMiddle(plist.get(pre_x_2 * 5 + pre_y_2), mid, p, plist);
            }
        }

        if (pre_x_2 >= 0 && las_y_2 <= 4)
        {
            Point mid = plist.get(pre_x * 5 + las_y);
            if (!((mid.x == 1 && mid.y == 2) || (mid.x == 3 && mid.y == 2)
                    || (mid.x == 2 && mid.y == 1) || (mid.x == 2 && mid.y == 3)))
            {
                judgeTwoPointInMiddle(plist.get(pre_x_2 * 5 + las_y_2), mid, p, plist);
            }
        }

        if (las_x_2 <= 4 && las_y_2 <= 4)
        {
            Point mid = plist.get(las_x * 5 + las_y);
            if (!((mid.x == 1 && mid.y == 2) || (mid.x == 3 && mid.y == 2)
                    || (mid.x == 2 && mid.y == 1) || (mid.x == 2 && mid.y == 3)))
            {
                judgeTwoPointInMiddle(plist.get(las_x_2 * 5 + las_y_2), mid, p, plist);
            }
        }

        if (las_x_2 <= 4 && pre_y_2 >= 0)
        {
            Point mid = plist.get(las_x * 5 + pre_y);
            if (!((mid.x == 1 && mid.y == 2) || (mid.x == 3 && mid.y == 2)
                    || (mid.x == 2 && mid.y == 1) || (mid.x == 2 && mid.y == 3)))
            {
                judgeTwoPointInMiddle(plist.get(las_x_2 * 5 + pre_y_2), mid, p, plist);
            }
        }
    }

    // �Ƿ�ɼ�
    private void judgeTwoPointInMiddle(Point pre_2, Point m, Point p, ArrayList<Point> plist) {
        // TODO Auto-generated method stub
        int pre_2_dir = pre_2.dir;
        int m_dir = m.dir;
        if (pre_2_dir != SON_NULL && m_dir != SON_NULL && pre_2_dir == p.dir && pre_2_dir != m_dir)
        {
            changeSon(m);
            eatSon(m.x * 5 + m.y, plist);
        }
    }

    // �Ƿ����
    private void judgeTwoPointInLine(Point pre, Point p, Point las, ArrayList<Point> plist) {
        // TODO Auto-generated method stub
        int pre_dir = pre.dir;
        int las_dir = las.dir;
        if (pre_dir != SON_NULL && pre_dir == las_dir && pre_dir != p.dir)
        {
            changeSon(pre);
            changeSon(las);
            eatSon(pre.x * 5 + pre.y, plist);
            eatSon(las.x * 5 + las.y, plist);
        }
    }

    // �Ƿ�ɼ�
    private boolean judgeTwoPointInMiddle_ddd(Point pre_2, Point m, Point p)
    {
        // TODO Auto-generated method stub
        int pre_2_dir = pre_2.dir;
        int m_dir = m.dir;
        if (pre_2_dir != SON_NULL && m_dir != SON_NULL && pre_2_dir == p.dir && pre_2_dir != m_dir)
        {
            return true;
        }
        return false;
    }

    // �Ƿ����
    private boolean judgeTwoPointInLine_ddd(Point pre, Point p, Point las)
    {
        // TODO Auto-generated method stub
        int pre_dir = pre.dir;
        int las_dir = las.dir;
        if (pre_dir != SON_NULL && pre_dir == las_dir && pre_dir != p.dir)
        {
            return true;
        }
        return false;
    }

    private void changeSon(Point p) {
        // TODO Auto-generated method stub
        if (p.dir == SON_GREEN)
        {
            p.dir = SON_RED;
        }
        else if (p.dir == SON_RED)
        {
            p.dir = SON_GREEN;
        }
    }

    // ��һ���Ŀ�ʼ JJ_IDLE
    private void back_to_init()
    {
        if (selectedId > -1)
        {
            point_list.get(selectedId).isSelected = false;
            selectedId = -1;
        }
        mainState = JJ_IDLE;
        invalidate();
    }

    private void moveSon(Point s, Point e) {
        // TODO Auto-generated method stub
        e.dir = s.dir;
        s.isSelected = false;
        s.dir = SON_NULL;
        last_touched_son = e.dir;
        refreshCurrentChess();
    }

    private void refreshCurrentChess() {
        // TODO Auto-generated method stub
        int dir = getReverseDir(last_touched_son);
        if (SON_GREEN == dir) {
            currentChess.setImageResource(R.drawable.greenstar);
        } else if (SON_RED == dir) {
            currentChess.setImageResource(R.drawable.redstar);
        }
    }

    private void moveSonCom(Point s, Point e) {
        // TODO Auto-generated method stub
        e.dir = s.dir;
        s.isSelected = false;
        s.dir = SON_NULL;
    }

    // whether two points in a line
    private boolean judgeLine(Point s, Point i, ArrayList<Point> plist) {
        boolean ret = false;
        int index = 0, num = 0, min = 0;
        // TODO Auto-generated method stub
        if (s.x == i.x && s.y == i.y)
        {
            return false;
        }

        if (s.x == i.x)
        {
            num = Math.abs(s.y - i.y);
            min = Math.min(s.y, i.y);
            for (index = 1; index < num; index++)
            {
                if (plist.get(s.x * 5 + min + index).dir != SON_NULL)
                {
                    return false;
                }
            }
            ret = true;
        }
        else if (s.y == i.y)
        {
            num = Math.abs(s.x - i.x);
            min = Math.min(s.x, i.x);
            for (index = 1; index < num; index++)
            {
                if (plist.get((min + index) * 5 + s.y).dir != SON_NULL)
                {
                    return false;
                }
            }
            ret = true;
        }
        else if (s.y == s.x && i.y == i.x)
        {
            num = Math.abs(s.x - i.x);
            min = Math.min(s.x, i.x);
            for (index = 1; index < num; index++)
            {
                if (plist.get((min + index) * 5 + min + index).dir != SON_NULL)
                {
                    return false;
                }
            }
            ret = true;
        }
        else if (s.x + s.y == 4 && i.x + i.y == 4)
        {
            num = Math.abs(s.x - i.x);
            min = Math.min(s.x, i.x);
            for (index = 1; index < num; index++)
            {
                if (plist.get((min + index) * 5 + 4 - min - index).dir != SON_NULL)
                {
                    return false;
                }
            }
            ret = true;
        }
        else if (s.x + s.y == 2 && i.x + i.y == 2)
        {
            num = Math.abs(s.x - i.x);
            min = Math.min(s.x, i.x);
            for (index = 1; index < num; index++)
            {
                if (plist.get((min + index) * 5 + 2 - min - index).dir != SON_NULL)
                {
                    return false;
                }
            }
            ret = true;
        }
        else if (s.x + s.y == 6 && i.x + i.y == 6)
        {
            num = Math.abs(s.x - i.x);
            min = Math.min(s.x, i.x);
            for (index = 1; index < num; index++)
            {
                if (plist.get((min + index) * 5 + 6 - min - index).dir != SON_NULL)
                {
                    return false;
                }
            }
            ret = true;
        }
        else if (s.y == s.x - 2 && i.y == i.x - 2)
        {
            num = Math.abs(s.x - i.x);
            min = Math.min(s.x, i.x);
            for (index = 1; index < num; index++)
            {
                if (plist.get((min + index) * 5 + min + index - 2).dir != SON_NULL)
                {
                    return false;
                }
            }
            ret = true;
        }
        else if (s.y == s.x + 2 && i.y == i.x + 2)
        {
            num = Math.abs(s.x - i.x);
            min = Math.min(s.x, i.x);
            for (index = 1; index < num; index++)
            {
                if (plist.get((min + index) * 5 + min + index + 2).dir != SON_NULL)
                {
                    return false;
                }
            }
            ret = true;
        }

        return ret;
    }

    private int getSelectSon(MotionEvent event) {
        // TODO Auto-generated method stub
        for (int i = 0; i < point_list.size(); i++)
        {
            Point p = point_list.get(i);
            if (p.dir > SON_NULL && last_touched_son != p.dir)
            {
                Rect ret = new Rect(width_list.get(p.y), height_list.get(p.x), width_list.get(p.y)
                        + son_width, height_list.get(p.x) + son_height);
                if (ret.contains((int) event.getX(), (int) event.getY()))
                {
                    return i;
                }
            }
        }
        return -1;
    }

    private int getSelectSpace(MotionEvent event) {
        // TODO Auto-generated method stub
        for (int i = 0; i < point_list.size(); i++)
        {
            Point p = point_list.get(i);
            if (p.dir <= SON_NULL)
            {
                Rect ret = new Rect(width_list.get(p.y), height_list.get(p.x), width_list.get(p.y)
                        + son_width, height_list.get(p.x) + son_height);
                if (ret.contains((int) event.getX(), (int) event.getY()))
                {
                    return i;
                }
            }
        }
        return -1;
    }

    private class Point implements Cloneable {
        int x;
        int y;

        @Override
        protected Object clone() throws CloneNotSupportedException {
            // TODO Auto-generated method stub
            return super.clone();
        }

        int dir;// 1:green;2:red
        boolean isSelected;
    }

    private class Step {
        public Step() {
        }
        public Step(int s, int e) {
            start = s;
            end = e;
        }
        public Step(int s, int e, int eat) {
            start = s;
            end = e;
        }
        public Step(int s, int e, int eat, int result) {
            // TODO Auto-generated constructor stub
            start = s;
            end = e;
            eatNum = eat;
            results = result;
        }
        int start;
        int end;
        int eatNum;
        int results;
    }

    @SuppressLint("HandlerLeak")
    private class ServiceHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case UPDATE: {
                    eatSon(msg.arg1, point_list);
                    back_to_init();
                    servicehandler.sendEmptyMessage(CHECKEND);
                    break;
                }

                case COM_PLAY_ANI: {
                    Step step = (Step)msg.obj;
                    final int start = step.start;
                    final int end = step.end;
                    playAnimation(point_list.get(step.start), point_list.get(step.end), new EmptyAnimator(){

                        @Override
                        public void onAnimationStart(Animator animation) {
                            // TODO Auto-generated method stub
                            selectedChess.setVisibility(View.VISIBLE);
                            servicehandler.sendEmptyMessage(INVALIDATE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            // TODO Auto-generated method stub
                            selectedChess.setVisibility(View.GONE);
                            moveSon(point_list.get(start), point_list.get(end));
                            saveChessRecordList(point_list.get(end).dir, start,end);
                            Message m = servicehandler.obtainMessage();
                            m.what = COM_UPDATE;
                            m.arg1 = end;
                            servicehandler.sendMessage(m);
                        }});
                    break;
                }

                case COM_UPDATE: {
                    eatSon(msg.arg1, point_list);
                    back_to_init();
                    servicehandler.sendEmptyMessage(COM_CHECKEND);
                    break;
                }

                case CHECKEND: {
                    if (true == checkIsOver())
                    {
                        gameover();
                    }
                    else
                    {
                        if (JumpApp.getApplication().isVsComputer() && mWIFI_CONNECT_OWNER == WIFI_CONNECT_OWNER.SERVER.IDLE) {
                            // It's computer's turn;
                            new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    try {
                                        bisComRuning = true;
                                        computerRun();
                                    } catch (CloneNotSupportedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        } else if (mWIFI_CONNECT_OWNER != WIFI_CONNECT_OWNER.SERVER.IDLE) {
                            bisComRuning = true;
                        } else {
                            bDisableToutch = false;
                        }
                    }
                    break;
                }

                case COM_CHECKEND: {
                    if (true == checkIsOver())
                    {
                        gameover();
                    }
                    else
                    {
                        bDisableToutch = false;
                    }
                    bisComRuning = false;
                    break;
                }

                case GAMEOVER: {
                    back_to_init();
                    gameover();
                    bisComRuning = false;
                    break;
                }

                case INVALIDATE: {
                    invalidate();
                    break;
                }
            }
            super.handleMessage(msg);
        }
    }

    public void Restart()
    {
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                Point p = point_list.get(i * 5 + j);
                p.x = i;
                p.y = j;
                if (i == 0)
                {
                    p.dir = SON_GREEN;
                }
                else if (i == 4)
                {
                    p.dir = SON_RED;
                }
                else
                {
                    p.dir = SON_NULL;
                }
                p.isSelected = false;
            }
        }

        last_touched_son = SON_GREEN;
        currentChess.setImageResource(R.drawable.redstar);
        bDisableToutch = false;
        mChessRecData.clear();
        mAdapter.notifyDataSetChanged();
        mainState = JJ_IDLE;
        resetClient();
        invalidate();
    }

    public boolean checkIsOver() {
        // TODO Auto-generated method stub
        int num_green = 0, num_red = 0;

        for (int i = 0; i < point_list.size(); i++)
        {
            if (point_list.get(i).dir == SON_GREEN)
            {
                num_green++;
            }
            else if (point_list.get(i).dir == SON_RED)
            {
                num_red++;
            }
        }

        if (num_green == 0 || num_red == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void computerRun() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        int dir = getReverseDir(last_touched_son);

        Step step = getComputerStep(dir, point_list);
        if (step == null) {
            servicehandler.sendEmptyMessage(GAMEOVER);
            return;
        }
        selectedId = step.start;
        point_list.get(step.start).isSelected = true;
//        moveSon(point_list.get(step.start), point_list.get(step.end));

        Message m = servicehandler.obtainMessage();
        m.what = COM_PLAY_ANI;
        m.obj = step;
        servicehandler.sendMessageDelayed(m, DELAYMILLS);

//        final int end = step.end;
//        playAnimation(point_list.get(step.start), point_list.get(step.end), new EmptyAnimator(){
//
//            @Override
//            public void onAnimationStart(Animator animation) {
//                // TODO Auto-generated method stub
//                selectedChess.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                // TODO Auto-generated method stub
//                selectedChess.setVisibility(View.GONE);
//                Message m = servicehandler.obtainMessage();
//                m.what = COM_UPDATE;
//                m.arg1 = end;
//                servicehandler.sendMessageDelayed(m, DELAYMILLS);
//            }});
//        Message m = servicehandler.obtainMessage();
//        m.what = COM_UPDATE;
//        m.arg1 = step.end;
//        servicehandler.sendMessageDelayed(m, DELAYMILLS);
    }

    private int getReverseDir(int dir) {
        // TODO Auto-generated method stub
        assert (dir != SON_NULL);

        int reverseDir = SON_NULL;

        if (SON_GREEN == dir)
        {
            reverseDir = SON_RED;
        }
        else if (SON_RED == dir)
        {
            reverseDir = SON_GREEN;
        }
        return reverseDir;
    }

    private Step getComputerStep(int dir, ArrayList<Point> plist) throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        int comdifficulty = JumpApp.getApplication().getComDifficulty();
        Log.d("JumpJump", "comdifficulty = "+comdifficulty);
        ArrayList<Step> mStepList = new ArrayList<Step>();
        int i, j, num, result, maxResult=-25;
        ArrayList<Point> point_list_temp = new ArrayList<Point>();

        int current_num = getSonNum(dir, plist);

        for (i = 0; i < plist.size(); i++)
        {
            Point p = plist.get(i);
            if (p.dir == dir)
            {
                for (j = 0; j < plist.size(); j++)
                {
                    Point space = plist.get(j);
                    if (space.dir == SON_NULL)
                    {
                        deepCopy(point_list_temp, plist);

                        if (true == judgeLine(point_list_temp.get(i), point_list_temp.get(j),
                                point_list_temp))
                        {
                            moveSonCom(point_list_temp.get(i), point_list_temp.get(j));
                            eatSon(j, point_list_temp);
                            num = getSonNum(dir, point_list_temp) - current_num;
                            if (comdifficulty > 0) {
                                int unDir = getReverseDir(dir);
                                Step next = getBestStep(unDir, point_list_temp);
                                result = num - next.results;
                            } else {
                                result = num;
                            }
                            mStepList.add(new Step(i,j,num,result));
                            if (result > maxResult) {
                                maxResult = result;
                            }
                        }
                    }
                }
            }
        }
        point_list_temp.clear();
        point_list_temp = null;
        if (mStepList.size() == 0) {
            return null;
        }
        ArrayList<Integer> resultList = new ArrayList<Integer>();
        for (i=0;i<mStepList.size();i++) {
            if (maxResult == mStepList.get(i).results) {
                resultList.add(i);
            }
        }
        Step step = new Step();
        Random random =  new Random();
        int pos = random.nextInt(resultList.size());
        Step resultstep = mStepList.get(resultList.get(pos));
        step.start = resultstep.start;
        step.end = resultstep.end;
        mStepList.clear();
        mStepList = null;
        resultList.clear();
        resultList = null;
        return step;
    }

    private Step getBestStep(int dir, ArrayList<Point> plist) throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        Step step = new Step(); // �ڶ���
        step.results = -25;

        int i, j, num;
        ArrayList<Point> point_list_temp = new ArrayList<Point>();

        int current_num = getSonNum(dir, plist);

        for (i = 0; i < plist.size(); i++)
        {
            Point p = plist.get(i);
            if (p.dir == dir)
            {
                for (j = 0; j < plist.size(); j++)
                {
                    Point space = plist.get(j);
                    if (space.dir == SON_NULL)
                    {
                        deepCopy(point_list_temp, plist);

                        if (true == judgeLine(point_list_temp.get(i), point_list_temp.get(j),
                                point_list_temp))
                        {
                            moveSonCom(point_list_temp.get(i), point_list_temp.get(j));
                            eatSon(j, point_list_temp);
                            num = getSonNum(dir, point_list_temp) - current_num; // ��ʾ���Ӷ���
                            if (num > step.results)
                            {
                                step.start = i;
                                step.end = j;
                                step.results = num;
                            }
                        }
                    }
                }
            }
        }
        point_list_temp.clear();
        point_list_temp = null;
        return step;
    }

    private int getSonNum(int dir, ArrayList<Point> plist) {
        // TODO Auto-generated method stub
        int num = 0;
        for (int i = 0; i < plist.size(); i++)
        {
            if (dir == plist.get(i).dir)
            {
                num++;
            }
        }

        assert (num > 0);

        return num;
    }

    // ���ӿɳ�
    @SuppressWarnings("unused")
    private boolean canEat(Point p) {
        // TODO Auto-generated method stub
        int pre_x = p.x - 1;
        int las_x = p.x + 1;
        int pre_y = p.y - 1;
        int las_y = p.y + 1;

        boolean ret = false;

        if (pre_x >= 0 && las_x <= 4)
        {
            ret = judgeTwoPointInLine_ddd(point_list.get(pre_x * 5 + p.y), p,
                    point_list.get(las_x * 5 + p.y));
            if (true == ret)
            {
                return ret;
            }
        }

        if (pre_y >= 0 && las_y <= 4)
        {
            ret = judgeTwoPointInLine_ddd(point_list.get(p.x * 5 + pre_y), p,
                    point_list.get(p.x * 5 + las_y));
            if (true == ret)
            {
                return ret;
            }
        }

        if (pre_x >= 0 && pre_y >= 0 && las_x <= 4 && las_y <= 4)
        {
            ret = judgeTwoPointInLine_ddd(point_list.get(pre_x * 5 + pre_y), p,
                    point_list.get(las_x * 5 + las_y));
            if (true == ret)
            {
                return ret;
            }
            ret = judgeTwoPointInLine_ddd(point_list.get(pre_x * 5 + las_y), p,
                    point_list.get(las_x * 5 + pre_y));
            if (true == ret)
            {
                return ret;
            }
        }

        int pre_x_2 = p.x - 2;
        int las_x_2 = p.x + 2;
        int pre_y_2 = p.y - 2;
        int las_y_2 = p.y + 2;

        if (pre_x_2 >= 0)
        {
            ret = judgeTwoPointInMiddle_ddd(point_list.get(pre_x_2 * 5 + p.y),
                    point_list.get(pre_x * 5 + p.y), p);
            if (true == ret)
            {
                return ret;
            }
        }

        if (las_x_2 <= 4)
        {
            ret = judgeTwoPointInMiddle_ddd(p, point_list.get(las_x * 5 + p.y),
                    point_list.get(las_x_2 * 5 + p.y));
            if (true == ret)
            {
                return ret;
            }
        }

        if (pre_y_2 >= 0)
        {
            ret = judgeTwoPointInMiddle_ddd(point_list.get(p.x * 5 + pre_y_2),
                    point_list.get(p.x * 5 + pre_y), p);
            if (true == ret)
            {
                return ret;
            }
        }

        if (las_y_2 <= 4)
        {
            ret = judgeTwoPointInMiddle_ddd(p, point_list.get(p.x * 5 + las_y),
                    point_list.get(p.x * 5 + las_y_2));
            if (true == ret)
            {
                return ret;
            }
        }

        if (pre_x_2 >= 0 && pre_y_2 >= 0)
        {
            ret = judgeTwoPointInMiddle_ddd(point_list.get(pre_x_2 * 5 + pre_y_2),
                    point_list.get(pre_x * 5 + pre_y), p);
            if (true == ret)
            {
                return ret;
            }
        }

        if (pre_x_2 >= 0 && las_y_2 <= 4)
        {
            ret = judgeTwoPointInMiddle_ddd(point_list.get(pre_x_2 * 5 + las_y_2),
                    point_list.get(pre_x * 5 + las_y), p);
            if (true == ret)
            {
                return ret;
            }
        }

        if (las_x_2 <= 4 && las_y_2 <= 4)
        {
            ret = judgeTwoPointInMiddle_ddd(point_list.get(las_x_2 * 5 + las_y_2),
                    point_list.get(las_x * 5 + las_y), p);
            if (true == ret)
            {
                return ret;
            }
        }

        if (las_x_2 <= 4 && pre_y_2 >= 0)
        {
            ret = judgeTwoPointInMiddle_ddd(point_list.get(las_x_2 * 5 + pre_y_2),
                    point_list.get(las_x * 5 + pre_y), p);
            if (true == ret)
            {
                return ret;
            }
        }

        return false;
    }

    private void gameover() {
        // TODO Auto-generated method stub
        Builder b = new AlertDialog.Builder(mContext);
        b.setMessage("GAME OVER!");
        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.create().show();
    }

    private void deepCopy(ArrayList<Point> dest, ArrayList<Point> src)
    {
        dest.clear();
        for (Point p : src)
        {
            try {
                dest.add((Point) p.clone());
            } catch (CloneNotSupportedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void saveCurrent()
    {
        deepCopy(point_list_pre, point_list);
        point_list_pre.get(selectedId).isSelected = false;
    }

    public void goBackStep()
    {
        if (JumpApp.getApplication().isVsComputer() == false)
        {
            return;
        }
        if (bisComRuning == true)
        {
            return;
        }
        if (point_list_pre.size() > 0)
        {
            point_list.clear();
            deepCopy(point_list, point_list_pre);
            point_list_pre.clear();
            back_to_init();
        }
    }

    public void setScreenParam(ArrayList<Integer> width_list2,
            ArrayList<Integer> height_list2) {
        // TODO Auto-generated method stub
        width_list = width_list2;
        height_list = height_list2;
    }

    public void setSelectChess(ImageView selectedChess, ImageView currentChess) {
        // TODO Auto-generated method stub
        this.selectedChess = selectedChess;
        this.currentChess = currentChess;
    }

    private class EmptyAnimator implements AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationCancel(Animator animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            // TODO Auto-generated method stub

        }

    }

    private String saveChessRecordList(int dir, int start, int end) {
        // TODO Auto-generated method stub
        String rec = "";
        if (dir == SON_NULL) return null;

        if (dir == SON_RED) {
            rec += "R:";
        } else if(dir == SON_GREEN) {
            rec += "G:";
        }
        String s = "",e = "";
        if (start < 10) {
            s += "0" + start;
        } else {
            s += "" + start;
        }
        if (end < 10) {
            e += "0" + end;
        } else {
            e += "" + end;
        }
        rec += ("[" + s + "->" + e + "]");
        mChessRecData.add(rec);
        mAdapter.notifyDataSetChanged();
        chessRecordList.smoothScrollToPosition(mChessRecData.size());
        return rec;
    }

    public void setChessRecordList(ListView list) {
        // TODO Auto-generated method stub
        chessRecordList = list;
        chessRecordList.setDividerHeight(0);
        chessRecordList.setScrollBarSize(0);
        mChessRecData = new ArrayList<String>();
        mAdapter = new BaseAdapter(){

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return mChessRecData.size();
            }

            @Override
            public Object getItem(int arg0) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public long getItemId(int arg0) {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public View getView(int i, View arg1, ViewGroup root) {
                // TODO Auto-generated method stub
                View v = arg1;
                if (v == null) {
                    v = LayoutInflater.from(mContext).inflate(R.layout.chess_record_list_item, root, false);
                }
                TextView tv = (TextView)v.findViewById(R.id.chess_record_list_item);
                tv.setText(mChessRecData.get(i));
                if (mChessRecData.get(i).startsWith("R:")) {
                    tv.setTextColor(Color.RED);
                } else if (mChessRecData.get(i).startsWith("G:")) {
                    tv.setTextColor(Color.GREEN);
                } else {
                    tv.setTextColor(Color.GRAY);
                }
                return v;
            }};
        chessRecordList.setAdapter(mAdapter);
    }

    public void setWifiConnectOwner(WIFI_CONNECT_OWNER m) {
        // TODO Auto-generated method stub
        mWIFI_CONNECT_OWNER = m;

        resetClient();
    }

    private void resetClient() {
        // TODO Auto-generated method stub
        if (mWIFI_CONNECT_OWNER == WIFI_CONNECT_OWNER.CLIENT) {
            bDisableToutch = true;
            bisComRuning = false;
        }
    }

    public void setMainActivity(MainActivity mainActivity) {
        // TODO Auto-generated method stub
        mMainActivity = mainActivity;
    }

    public void parseChessStep(String step) {
        // TODO Auto-generated method stub
        //R:[23->18]
        Step s = new Step();
        s.start = Integer.valueOf(step.substring(step.indexOf(":[")+":[".length(), step.indexOf("->")));
        s.end = Integer.valueOf(step.substring(step.indexOf("->")+"->".length(), step.indexOf("]")));
        selectedId = s.start;
        point_list.get(s.start).isSelected = true;
        Message m = servicehandler.obtainMessage();
        m.what = COM_PLAY_ANI;
        m.obj = s;
        servicehandler.sendMessageDelayed(m, DELAYMILLS);
    }

    public boolean isComRuning() {
        return bisComRuning;
    }
}
