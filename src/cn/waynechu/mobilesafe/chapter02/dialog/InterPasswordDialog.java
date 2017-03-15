package cn.waynechu.mobilesafe.chapter02.dialog;

import cn.waynechu.mobilesafe.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InterPasswordDialog extends Dialog implements
        android.view.View.OnClickListener {

    /** ���������ı��� */
    private EditText mInterET;
    /** ȷ�ϰ�ť */
    private Button mOKBtn;
    /** ȡ����ť */
    private Button mCancleBtn;
    /** �ص��ӿ� */
    private MyCallBack myCallBack;

    public InterPasswordDialog(Context context) {
        super(context, R.style.dialog_custom);
    }

    public void setCallBack(MyCallBack myCallBack) {
        this.myCallBack = myCallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.inter_password_dialog);
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        mInterET = (EditText) findViewById(R.id.et_inter_password);
        mOKBtn = (Button) findViewById(R.id.btn_comfirm);
        mCancleBtn = (Button) findViewById(R.id.btn_dismiss);
        mOKBtn.setOnClickListener(this);
        mCancleBtn.setOnClickListener(this);

    }

    public String getPassword() {
        return mInterET.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_comfirm:
            myCallBack.confirm();
            break;
        case R.id.btn_dismiss:
            myCallBack.cancle();
            break;
        }
    }

    /**
     * �ص��ӿ�
     * 
     * @author waynechu
     */
    public interface MyCallBack {
        void confirm();

        void cancle();
    }
}
