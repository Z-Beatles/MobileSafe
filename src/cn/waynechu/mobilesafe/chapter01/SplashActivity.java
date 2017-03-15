package cn.waynechu.mobilesafe.chapter01;


import cn.waynechu.mobilesafe.R;
import cn.waynechu.mobilesafe.chapter01.utils.MyUtils;
import cn.waynechu.mobilesafe.chapter01.utils.VersionUpdateUtils;
import android.os.Bundle;
import android.app.Activity;
import android.view.Window;
import android.widget.TextView;

public class SplashActivity extends Activity {
    /**
     * 欢迎界面
     */
    // 应用版本号
    private String mVersion;
    // 本地版本号
    private TextView mVersionTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置该Activity没有标题栏，在加载布局之前调用
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        //获取版本号
        mVersion = MyUtils.getVersion(getApplicationContext());
        //初始化控件（版本信息）
        initView();
        final VersionUpdateUtils updateUtils = new VersionUpdateUtils(mVersion,
                SplashActivity.this);
        new Thread() {
            public void run() {
                // 获取服务器版本信息
                updateUtils.getCloudVersion();
            }
        }.start();
    }

    /**
     * 初始化控件（版本信息）
     */
    private void initView() {
        mVersionTV = (TextView) findViewById(R.id.tv_splash_version);
        mVersionTV.setText("版本号 " + mVersion);
    }

}
