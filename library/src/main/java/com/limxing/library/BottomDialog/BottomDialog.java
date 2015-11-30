package com.limxing.library.BottomDialog;


import android.app.Activity;


import android.os.Bundle;


import com.limxing.library.NoTitleBar.SystemBarTintManager;
import com.limxing.library.R;
import com.limxing.library.utils.LogUtils;

public class BottomDialog extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemBarTintManager.initSystemBar(this, R.color.transparent);
        setContentView(R.layout.bottomdialog);
    }
}
