package com.limxing.library.BottomDialog;


import android.app.Activity;


import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;


import com.limxing.library.NoTitleBar.SystemBarTintManager;
import com.limxing.library.R;
import com.limxing.library.utils.LogUtils;

public class BottomDialog extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SystemBarTintManager.initSystemBar(this, R.color.transparent);
        setContentView(R.layout.bottomdialog);
        findViewById(R.id.bottom_bac).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {


    }
}
