package cn.waynechu.mobilesafe.chapter02.dialog;

import cn.waynechu.mobilesafe.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * ����������߼��࣬�̳���Dialog
 * 
 * @author waynechu
 * 
 */
public class SetUpPasswordDialog extends Dialog implements
        android.view.View.OnClickListener {
    // �״����������ı���
    public EditText mFirstPWDET;
    // ȷ�������ı���
    public EditText mAffirmET;
    // �ص��ӿ�
    private MyCallBack myCallBack;

    // ���췽�������Զ���Ի�����ʽ
    public SetUpPasswordDialog(Context context) {
        super(context, R.style.dialog_custom);// �����Զ���Ի�����ʽ
    }

    // ����ص���������MyCallBack()�ӿ�
    public void setCallBack(MyCallBack myCallBack) {
        this.myCallBack = myCallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.setup_password_dialog);
        super.onCreate(savedInstanceState);
        initView();
    }

    /**
     * ��ʼ���ؼ�
     */
    private void initView() {
        mFirstPWDET = (EditText) findViewById(R.id.et_fistpwd);
        mAffirmET = (EditText) findViewById(R.id.et_affirm_password);
        findViewById(R.id.btn_ok).setOnClickListener(this);
        findViewById(R.id.btn_cancle).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_ok:
            myCallBack.ok();
            break;
        case R.id.btn_cancle:
            myCallBack.cancle();
        }

    }

    public interface MyCallBack {
        void ok();

        void cancle();
    }

}
