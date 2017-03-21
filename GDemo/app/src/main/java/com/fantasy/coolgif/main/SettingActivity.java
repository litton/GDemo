package com.fantasy.coolgif.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.fantasy.coolgif.R;
import com.fantasy.coolgif.utils.PreferenceUtil;
import com.fantasy.coolgif.utils.Utils;

import java.util.List;

/**
 * Created by fanlitao on 17/3/15.
 */

public class SettingActivity extends Activity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        findViewById(R.id.like_gif).setOnClickListener(this);
        findViewById(R.id.heart_gif).setOnClickListener(this);

        if(Utils.isSuperAccount()) {
            findViewById(R.id.set_pos).setVisibility(View.VISIBLE);
            final EditText posEditText = (EditText) findViewById(R.id.pos);
            findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!TextUtils.isEmpty(posEditText.getText())) {
                        String postition = (String)posEditText.getText().toString();

                        int pos =  Integer.parseInt(postition);
                        PreferenceUtil.saveGifCurrentPos(pos);
                    }
                }
            });

        }

    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v.getId() == R.id.heart_gif) {
            intent = new Intent(this, CreateAnimatGifActivity.class);
            intent.putExtra(ViewGifsListActivity.ACTION_TYPE, ViewGifsListActivity.ACTION_HEART_TYPE);
        } else {
            intent = new Intent(this, ViewGifsListActivity.class);
            intent.putExtra(ViewGifsListActivity.ACTION_TYPE, ViewGifsListActivity.ACTION_LIKE_TYPE);
        }

        startActivity(intent);
    }
}
