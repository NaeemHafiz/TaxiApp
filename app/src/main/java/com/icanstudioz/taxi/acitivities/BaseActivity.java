package com.icanstudioz.taxi.acitivities;

import android.content.Context;

import com.icanstudioz.taxi.custom.LocaleHelper;
import com.thebrownarrow.permissionhelper.ActivityManagePermission;


public class BaseActivity extends ActivityManagePermission {

    @Override
    protected void attachBaseContext(Context newBase) {
        Context c = LocaleHelper.onAttach(newBase);
        super.attachBaseContext(c);
    }
}
