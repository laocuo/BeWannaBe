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

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingActivity extends Activity implements OnClickListener {

    private Button b;
    private RadioGroup rg;
    private RadioButton dir_r0, dir_r1;
    public int direct;// 0:man;1:com

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_setting);

        b = (Button) findViewById(R.id.done);

        rg = (RadioGroup) findViewById(R.id.radioGroup);

        dir_r0 = (RadioButton) findViewById(R.id.dir_radio0);

        dir_r1 = (RadioButton) findViewById(R.id.dir_radio1);

//        direct = JumpApp.getApplication().getDirectSetting();

        if (direct == 1) {
            dir_r1.setChecked(true);
        }
        else {
            dir_r0.setChecked(true);
        }

        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.done: {
                if (R.id.dir_radio0 == rg.getCheckedRadioButtonId()) {
                    direct = 0;
                }
                else {
                    direct = 1;
                }
//                JumpApp.getApplication().setDirectSetting(direct);
                finish();
                break;
            }
        }
    }
}
