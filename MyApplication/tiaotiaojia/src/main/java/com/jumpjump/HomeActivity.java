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

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeActivity extends Activity {

    private Button start, setting, help, wifi_connect;
    private HomeButtonClick hbc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_home);

        initView();

        setListener();

        initData();
    }

    private void initData() {
        // TODO Auto-generated method stub

    }

    private void setListener() {
        // TODO Auto-generated method stub
        start.setOnClickListener(hbc);
        setting.setOnClickListener(hbc);
        help.setOnClickListener(hbc);
        wifi_connect.setOnClickListener(hbc);
    }

    private void initView() {
        // TODO Auto-generated method stub
        start = (Button) findViewById(R.id.start);
        setting = (Button) findViewById(R.id.setting);
        help = (Button) findViewById(R.id.help);
        wifi_connect = (Button) findViewById(R.id.wifi_connect);
        hbc = new HomeButtonClick();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    protected class HomeButtonClick implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            switch (arg0.getId())
            {
                case R.id.start: {
                    Intent i = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(i);
                    break;
                }

                case R.id.setting: {
                    Intent i = new Intent(HomeActivity.this, SettingPreferenceActivity.class);
                    startActivity(i);
                    break;
                }

                case R.id.help: {
                    Intent i = new Intent(HomeActivity.this, HelpActivity.class);
                    startActivity(i);
                    break;
                }

                case R.id.wifi_connect: {
                    Intent i = new Intent(HomeActivity.this, MainActivity.class);
                    i.putExtra("WIFI_CONNECT", true);
                    startActivity(i);
                    break;
                }
            }
        }
    }


}
