package com.fantasy.coolgif.main;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.widget.Button;
import com.fantasy.coolgif.R;
import java.util.List;

/**
 * Created by fanlitao on 17/3/15.
 */

public class SettingActivity extends PreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (hasHeaders()) {
            Button button = new Button(this);
            button.setText("Exit");
            setListFooter(button);
        }
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        System.out.println(fragmentName);
        return true;
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_setting, target);
    }

    public static class Prefs1Fragment extends PreferenceFragment {

    }

    public static class Pref2Fragment extends PreferenceFragment {

    }

}
