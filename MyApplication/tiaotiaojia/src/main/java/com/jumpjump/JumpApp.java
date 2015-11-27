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

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class JumpApp extends Application {
    private static JumpApp sJumpApp = null;
    private SharedPreferences sp;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        sJumpApp = this;
        sp = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public static JumpApp getApplication() {
        return sJumpApp;
    }

    public boolean isVsComputer() {
        return sp.getBoolean(SettingPreferenceActivity.COMSWITCH, false);
    }

    public boolean isAudioOpen() {
        return sp.getBoolean(SettingPreferenceActivity.AUDIOSWITCH, false);
    }

    public int getComDifficulty() {
        String mDifficult = sp.getString(SettingPreferenceActivity.COMDIFFCULTY, "0");
        return Integer.valueOf(mDifficult);
    }
}
