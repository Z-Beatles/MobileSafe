package cn.waynechu.mobilesafe.chapter03;

import java.util.ArrayList;
import java.util.List;
import cn.waynechu.mobilesafe.R;
import cn.waynechu.mobilesafe.chapter03.adapter.BlackContactAdapter;
import cn.waynechu.mobilesafe.chapter03.adapter.BlackContactAdapter.BlackContactCallBack;
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

public class SecurityPhoneActivity extends Activity implements OnClickListener {

	/** 有黑名单时显示的帧布局 */
	private FrameLayout mHaveBlackNumber;
	/** 无黑名单时显示的帧布局 */
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

	@Override
	protected void onResume() {
		super.onResume();
		if (totalNumber != dao.getTotalNumber()) {
			// 数据发生变化
			if (totalNumber != dao.getTotalNumber()) {
			}
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

	/**
	 * 用于填充数据，重新刷新界面
	 */
	private void fillData() {
		dao = new BlackNumberDao(SecurityPhoneActivity.this);
		totalNumber = dao.getTotalNumber();
		if (totalNumber == 0) {
			// 如果数据库中没有黑名单数据
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
				adapter.setCallBack(new BlackContactCallBack() {

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

	/**
	 * 初始化控件
	 */
	private void initView() {
		findViewById(R.id.rl_titlebar).setBackgroundColor(
				getResources().getColor(R.color.bright_purple));
		ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
		// 设置标题栏为通讯卫士
		((TextView) findViewById(R.id.tv_title)).setText("通讯卫士");
		mLeftImgv.setOnClickListener(this);
		mLeftImgv.setImageResource(R.drawable.back);
		mHaveBlackNumber = (FrameLayout) findViewById(R.id.fl_havablacknumber);
		mNoBlackNumber = (FrameLayout) findViewById(R.id.fl_noblacknumber);
		findViewById(R.id.btn_addblacknumber).setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.lv_blacknumbers);
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				// 屏幕没有滑动的状态
				case OnScrollListener.SCROLL_STATE_IDLE:
					// 获取最后一个可见的条目
					int lastVisiblePosition = mListView
							.getLastVisiblePosition();
					// 如果当前条目是最后一个，则查询更多的数据
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
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

			}
		});

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
