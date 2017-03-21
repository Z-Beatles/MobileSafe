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
 * 显示黑名单信息逻辑
 * 
 * @author waynechu
 * 
 */
public class SecurityPhoneActivity extends Activity implements OnClickListener {

    /** 有黑名单时显示的帧布局 */
    private FrameLayout mHaveBlackNumber;
    /** 没有黑名单时显示的帧布局 */
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
        ((TextView) findViewById(R.id.tv_title)).setText("通讯卫士");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        mHaveBlackNumber = (FrameLayout) findViewById(R.id.fl_havablacknumber);
        mNoBlackNumber = (FrameLayout) findViewById(R.id.fl_noblacknumber);
        findViewById(R.id.btn_addblacknumber).setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.lv_blacknumbers);
        mListView.setOnScrollListener(new OnScrollListener() {
            // 滑动开始和结束的一瞬间各触发一次，实现分页加载的功能
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                // 滑不动，到底了
                case OnScrollListener.SCROLL_STATE_IDLE:
                    // 获取最后一个可见条目
                    int lastVisiblePosition = mListView
                            .getLastVisiblePosition();
                    // 如果当前条目是最后一个 增查询更多的数据
                    if (lastVisiblePosition == pageBlackNumber.size() - 1) {
                        pagenumber++;
                        if (pagenumber * pagesize >= totalNumber) {
                            Toast.makeText(SecurityPhoneActivity.this,
                                    "没有更多的数据了", Toast.LENGTH_SHORT).show();
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
            // 数据库中没有黑名单数据
            mHaveBlackNumber.setVisibility(View.GONE);
            mNoBlackNumber.setVisibility(View.VISIBLE);
        } else if (totalNumber > 0) {
            // 数据库中有黑名单数据
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
        //当Activity回到前台时调用,如果数据库总条目发生变化，则清空黑名单中的数据并重新添加。
        super.onResume();
        //System.out.print("totalNumber:"+totalNumber);
        //int daototal=dao.getTotalNumber();
        //System.out.print("dao.getTotalNumber()"+daototal);
        if (totalNumber != dao.getTotalNumber()) {
            // 数据发生变化
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
            // 跳转至添加黑名单页面
            startActivity(new Intent(this, AddBlackNumberActivity.class));
            break;
        }
    }

}