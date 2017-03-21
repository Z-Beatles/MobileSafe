package cn.waynechu.mobilesafe.chapter03;

import java.util.ArrayList;
import java.util.List;
import cn.waynechu.mobilesafe.R;
import cn.waynechu.mobilesafe.chapter03.adapter.BlackContactAdapter;
import cn.waynechu.mobilesafe.chapter03.adapter.BlackContactAdapter.BlackConactCallBack;
import cn.waynechu.mobilesafe.chapter03.db.dao.BlackNumberDao;
import cn.waynechu.mobilesafe.chapter03.entity.BlackContactInfo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ��ʾ��������Ϣ�߼�
 * 
 * @author waynechu
 * 
 */
public class SecurityPhoneActivity extends Activity implements OnClickListener {

    /** �к�����ʱ��ʾ��֡���� */
    private FrameLayout mHaveBlackNumber;
    /** û�к�����ʱ��ʾ��֡���� */
    private FrameLayout mNoBlackNumber;
    private BlackNumberDao dao;
    private ListView mListView;
    private int pagenumber = 0;
    private int pagesize = 10;
    private int totalNumber;
    private List<BlackContactInfo> pageBlackNumber = new ArrayList<BlackContactInfo>();
    private BlackContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_securityphone);
        initView();
        fillData();
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(
                getResources().getColor(R.color.bright_purple));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView) findViewById(R.id.tv_title)).setText("ͨѶ��ʿ");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        mHaveBlackNumber = (FrameLayout) findViewById(R.id.fl_havablacknumber);
        mNoBlackNumber = (FrameLayout) findViewById(R.id.fl_noblacknumber);
        findViewById(R.id.btn_addblacknumber).setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.lv_blacknumbers);
        mListView.setOnScrollListener(new OnScrollListener() {
            // ������ʼ�ͽ�����һ˲�������һ�Σ�ʵ�ַ�ҳ���صĹ���
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                // ��������������
                case OnScrollListener.SCROLL_STATE_IDLE:
                    // ��ȡ���һ���ɼ���Ŀ
                    int lastVisiblePosition = mListView
                            .getLastVisiblePosition();
                    // �����ǰ��Ŀ�����һ�� ����ѯ���������
                    if (lastVisiblePosition == pageBlackNumber.size() - 1) {
                        pagenumber++;
                        if (pagenumber * pagesize >= totalNumber) {
                            Toast.makeText(SecurityPhoneActivity.this,
                                    "û�и����������", Toast.LENGTH_SHORT).show();
                        } else {
                            pageBlackNumber.addAll(dao.getPageBlackNumber(
                                    pagenumber, pagesize));
                            adapter.notifyDataSetChanged();
                        }
                    }
                    break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void fillData() {
        dao = new BlackNumberDao(SecurityPhoneActivity.this);
        totalNumber = dao.getTotalNumber();
        if (totalNumber == 0) {
            // ���ݿ���û�к���������
            mHaveBlackNumber.setVisibility(View.GONE);
            mNoBlackNumber.setVisibility(View.VISIBLE);
        } else if (totalNumber > 0) {
            // ���ݿ����к���������
            mHaveBlackNumber.setVisibility(View.VISIBLE);
            mNoBlackNumber.setVisibility(View.GONE);
            pagenumber = 0;
            
            if (pageBlackNumber.size() > 0) {
                pageBlackNumber.clear();
            }
            pageBlackNumber
                    .addAll(dao.getPageBlackNumber(pagenumber, pagesize));
            if (adapter == null) {
                adapter = new BlackContactAdapter(pageBlackNumber,
                        SecurityPhoneActivity.this);
                adapter.setCallBack(new BlackConactCallBack() {

                    @Override
                    public void DataSizeChanged() {
                        fillData();
                    }
                });
                mListView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }

    }
    
    @Override
    protected void onResume() {
        //��Activity�ص�ǰ̨ʱ����,������ݿ�����Ŀ�����仯������պ������е����ݲ�������ӡ�
        super.onResume();
        //System.out.print("totalNumber:"+totalNumber);
        //int daototal=dao.getTotalNumber();
        //System.out.print("dao.getTotalNumber()"+daototal);
        if (totalNumber != dao.getTotalNumber()) {
            // ���ݷ����仯
            if (dao.getTotalNumber() > 0) {
                mHaveBlackNumber.setVisibility(View.VISIBLE);
                mNoBlackNumber.setVisibility(View.GONE);
            } else {
                mHaveBlackNumber.setVisibility(View.GONE);
                mNoBlackNumber.setVisibility(View.VISIBLE);
            }
            pagenumber = 0;
            pageBlackNumber.clear();
            pageBlackNumber
                    .addAll(dao.getPageBlackNumber(pagenumber, pagesize));
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.imgv_leftbtn:
            finish();
            break;
        case R.id.btn_addblacknumber:
            // ��ת����Ӻ�����ҳ��
            startActivity(new Intent(this, AddBlackNumberActivity.class));
            break;
        }
    }

}