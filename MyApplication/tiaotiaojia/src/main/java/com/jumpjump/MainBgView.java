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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MainBgView extends View {

    private ArrayList<Integer> width_list = null;
    private ArrayList<Integer> height_list = null;
    private Paint p;
    private int son_width;
    // private int son_height;
    private Bitmap SonBitmap = null;

    public MainBgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        p = new Paint();
        p.setColor(R.color.main_bg_view_line);
        p.setStrokeWidth(2f);
        SonBitmap = FactoryInterface.getBitmap(context, R.drawable.greenstar);
        son_width = SonBitmap.getWidth();
        // son_height = SonBitmap.getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        if (width_list != null) {
            draw_init(canvas);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    private void draw_init(Canvas canvas) {
        // TODO Auto-generated method stub
        int i;
        // draw line
        for (i = 0; i < 5; i++) {
            canvas.drawLine(width_list.get(0) + son_width / 2, height_list.get(i) + son_width / 2,
                    width_list.get(4) + son_width / 2, height_list.get(i) + son_width / 2, p);
        }

        for (i = 0; i < 5; i++) {
            canvas.drawLine(width_list.get(i) + son_width / 2, height_list.get(0) + son_width / 2,
                    width_list.get(i) + son_width / 2, height_list.get(4) + son_width / 2, p);
        }
        canvas.drawLine(width_list.get(0) + son_width / 2, height_list.get(0) + son_width / 2,
                width_list.get(4) + son_width / 2, height_list.get(4) + son_width / 2, p);
        canvas.drawLine(width_list.get(4) + son_width / 2, height_list.get(0) + son_width / 2,
                width_list.get(0) + son_width / 2, height_list.get(4) + son_width / 2, p);

        canvas.drawLine(width_list.get(2) + son_width / 2, height_list.get(0) + son_width / 2,
                width_list.get(4) + son_width / 2, height_list.get(2) + son_width / 2, p);
        canvas.drawLine(width_list.get(4) + son_width / 2, height_list.get(2) + son_width / 2,
                width_list.get(2) + son_width / 2, height_list.get(4) + son_width / 2, p);
        canvas.drawLine(width_list.get(2) + son_width / 2, height_list.get(4) + son_width / 2,
                width_list.get(0) + son_width / 2, height_list.get(2) + son_width / 2, p);
        canvas.drawLine(width_list.get(0) + son_width / 2, height_list.get(2) + son_width / 2,
                width_list.get(2) + son_width / 2, height_list.get(0) + son_width / 2, p);
    }

    public void setScreenParam(ArrayList<Integer> width_list2,
            ArrayList<Integer> height_list2) {
        // TODO Auto-generated method stub
        width_list = width_list2;
        height_list = height_list2;
    }
}
