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

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

public class SettingPreferenceActivity extends PreferenceActivity implements
        OnPreferenceChangeListener {
    private SharedPreferences sp;
    private CheckBoxPreference mComSwitch;
    private ListPreference mComDiffcult;
    private CheckBoxPreference mAudioSwitch;
    public static final String COMSWITCH = "pref_key_vs_com";
    public static final String COMDIFFCULTY = "pref_key_com_difficulty";
    public static final String AUDIOSWITCH = "pref_key_audio_open";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        addPreferencesFromResource(R.xml.settings_preferences);
        mComSwitch = (CheckBoxPreference) findPreference(COMSWITCH);
        mComSwitch.setOnPreferenceChangeListener(this);
        mComDiffcult = (ListPreference) findPreference(COMDIFFCULTY);
        mComDiffcult.setOnPreferenceChangeListener(this);
        mAudioSwitch = (CheckBoxPreference) findPreference(AUDIOSWITCH);
        mAudioSwitch.setOnPreferenceChangeListener(this);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        String value = sp.getString(SettingPreferenceActivity.COMDIFFCULTY, "0");
        initComDifficulty(mComSwitch.isChecked());
        mComDiffcult.setSummary(getVisualTextName(this,value,
                R.array.pref_key_com_difficulty_choices,
                R.array.pref_key_com_difficulty_values));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public boolean onPreferenceChange(Preference arg0, Object arg1) {
        // TODO Auto-generated method stub
        final String key = arg0.getKey();
        if (COMSWITCH.equals(key)) {
            initComDifficulty((Boolean)arg1);
        }else if (COMDIFFCULTY.equals(key)) {
            mComDiffcult.setSummary(getVisualTextName(this,(String)arg1,
                    R.array.pref_key_com_difficulty_choices,
                    R.array.pref_key_com_difficulty_values));
        }
        return true;
    }

    private void initComDifficulty(boolean isComChecked) {
        // TODO Auto-generated method stub
        if (isComChecked) {
            mComDiffcult.setEnabled(true);
        } else {
            mComDiffcult.setEnabled(false);
        }
    }

    public static CharSequence getVisualTextName(Context context,
            String enumName,
            int choiceNameResId,
            int choiceValueResId) {
        CharSequence[] visualNames = null;
        visualNames = context.getResources().getTextArray(choiceNameResId);
        CharSequence[] enumNames = context.getResources().getTextArray(choiceValueResId);
        if (visualNames.length != enumNames.length) {
            return "";
        }
        for (int i = 0; i < enumNames.length; i++) {
            if (enumNames[i].equals(enumName)) {
                return visualNames[i];
            }
        }
        return "";
    }
}
