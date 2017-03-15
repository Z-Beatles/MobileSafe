package cn.waynechu.mobilesafe.chapter02;

import cn.waynechu.mobilesafe.R;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * 向导界面1
 * 
 * @author waynechu
 * 
 */
public class SetUp1Activity extends BaseSetUpActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
        initView();
    }

    private void initView() {
        // 设置第一个小圆点的颜色
        ((RadioButton) findViewById(R.id.rb_first)).setChecked(true);
    }

    @Override
    public void showPre() {
        Toast.makeText(this, "当前页面已经是第一页", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNext() {
        startActivityAndFinishSelf(SetUp2Activity.class);
    }

}
