package cn.waynechu.mobilesafe.chapter01;

import cn.waynechu.mobilesafe.R;
import cn.waynechu.mobilesafe.chapter01.utils.MyUtils;
import cn.waynechu.mobilesafe.chapter01.utils.VersionUpdateUtils;
import android.os.Bundle;
import android.app.Activity;
import android.view.Window;
import android.widget.TextView;

/**
 * ��ӭ����
 */
public class SplashActivity extends Activity {
    // Ӧ�ð汾��
    private String mVersion;
    // ���ذ汾��
    private TextView mVersionTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ���ø�Activityû�б��������ڼ��ز���֮ǰ����
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        // ��ȡ�汾��
        mVersion = MyUtils.getVersion(getApplicationContext());
        // ��ʼ���ؼ����汾��Ϣ��
        initView();
        final VersionUpdateUtils updateUtils = new VersionUpdateUtils(mVersion,
                SplashActivity.this);
        new Thread() {
            public void run() {
                // ��ȡ�������汾��Ϣ
                updateUtils.getCloudVersion();
            }
        }.start();
    }

    /**
     * ��ʼ���ؼ����汾��Ϣ��
     */
    private void initView() {
        mVersionTV = (TextView) findViewById(R.id.tv_splash_version);
        mVersionTV.setText("�汾�� " + mVersion);
    }

}
