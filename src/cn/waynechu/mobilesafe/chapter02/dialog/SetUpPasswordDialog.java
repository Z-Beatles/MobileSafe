package cn.waynechu.mobilesafe.chapter02.dialog;

import cn.waynechu.mobilesafe.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * 设置密码的逻辑类，继承自Dialog
 * 
 * @author waynechu
 * 
 */
public class SetUpPasswordDialog extends Dialog implements
        android.view.View.OnClickListener {
    // 首次输入密码文本框
    public EditText mFirstPWDET;
    // 确认密码文本框
    public EditText mAffirmET;
    // 回调接口
    private MyCallBack myCallBack;

    // 构造方法引入自定义对话框样式
    public SetUpPasswordDialog(Context context) {
        super(context, R.style.dialog_custom);// 引入自定义对话框样式
    }

    // 定义回调函数传递MyCallBack()接口
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
     * 初始化控件
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
