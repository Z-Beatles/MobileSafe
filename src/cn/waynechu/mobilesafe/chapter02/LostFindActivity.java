package cn.waynechu.mobilesafe.chapter02;

import cn.waynechu.mobilesafe.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * ����ָ�����
 * 
 * @author Wayne Chu
 * 
 */
public class LostFindActivity extends Activity implements OnClickListener {

    private SharedPreferences msharedPreferences;
    private TextView mSafePhoneTV;
    private ToggleButton mToggleButton;
    private RelativeLayout mInterSetupRL;
    private TextView mProtectStatusTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_lostfind);
        msharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        if (!isSetUp()) {
            startSetUp1Activity();
        }
        initView();

    }

    /**
     * ��ʼ���ؼ�
     */
    private void initView() {
        TextView mTitleTV = (TextView) findViewById(R.id.tv_title);
        mTitleTV.setText("�ֻ�����");
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        findViewById(R.id.rl_titlebar).setBackgroundColor(
                getResources().getColor(R.color.purple));
        mSafePhoneTV = (TextView) findViewById(R.id.tv_safephone);
        mSafePhoneTV.setText(msharedPreferences.getString("safephone", ""));
        mToggleButton = (ToggleButton) findViewById(R.id.togglebtn_lostfind);
        mInterSetupRL = (RelativeLayout) findViewById(R.id.rl_inter_setup_wizard);
        mInterSetupRL.setOnClickListener(this);
        mProtectStatusTV = (TextView) findViewById(R.id.tv_lostfind_protectstauts);
        // ��ѯ�ֻ������Ƿ�����Ĭ��Ϊ����
        boolean protecting = msharedPreferences.getBoolean("protecting", true);
        if (protecting) {
            mProtectStatusTV.setText("���������Ѿ�����");
            mToggleButton.setChecked(true);
        } else {
            mProtectStatusTV.setText("��������û�п���");
            mToggleButton.setChecked(false);
        }
        mToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                if (isChecked) {
                    mProtectStatusTV.setText("���������Ѿ�����");
                } else {
                    mProtectStatusTV.setText("��������û�п���");
                }
                Editor editor = msharedPreferences.edit();
                editor.putBoolean("protecting", isChecked);
                editor.commit();
            }
        });
    }

    private void startSetUp1Activity() {
        Intent intent = new Intent(LostFindActivity.this, SetUp1Activity.class);
        startActivity(intent);
        finish();
    }

    /**
     * �ж��Ƿ�����������
     * 
     * @return
     */
    private boolean isSetUp() {
        return msharedPreferences.getBoolean("isSetUp", false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.rl_inter_setup_wizard:
            // ���½���������
            startSetUp1Activity();
            break;
        case R.id.imgv_leftbtn:
            finish();
            break;
        }
    }
}
