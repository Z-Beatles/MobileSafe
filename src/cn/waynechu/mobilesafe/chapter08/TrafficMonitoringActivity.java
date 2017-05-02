package cn.waynechu.mobilesafe.chapter08;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.content.Intent;
import android.telephony.SmsManager;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import cn.waynechu.mobilesafe.R;
import cn.waynechu.mobilesafe.chapter08.db.dao.TrafficDao;
import cn.waynechu.mobilesafe.chapter08.service.TrafficMonitoringService;
import cn.waynechu.mobilesafe.chapter08.utils.SystemInfoUtils;

public class TrafficMonitoringActivity extends Activity implements
		OnClickListener {
	private SharedPreferences mSP;
	private Button mCorrectFlowBtn;
	private TextView mTotalTV;
	private TextView mUsedTV;
	private TextView mToDayTV;
	private TrafficDao dao;
	private ImageView mRemindIMGV;
	private TextView mRemindTV;
	private CorrectFlowReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_trafficmonitoring);
		mSP = getSharedPreferences("config", MODE_PRIVATE);
		boolean flag = mSP.getBoolean("isset_operator", false);
		// 判断是否设置过运营商信息
		if (!flag) {
			startActivity(new Intent(this, OperatorSetActivity.class));
			finish();
		}
		if (!SystemInfoUtils
				.isServiceRunning(this,
						"cn.waynechu.mobilesafe.chapter08.service.TrafficMonitoringService")) {
			startService(new Intent(this, TrafficMonitoringService.class));
		}
		initView();
		registReceiver();
		initData();
	}

	private void initView() {
		findViewById(R.id.rl_titlebar).setBackgroundColor(
				getResources().getColor(R.color.light_green));
		ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
		((TextView) findViewById(R.id.tv_title)).setText("流量监控");
		mLeftImgv.setOnClickListener(this);
		mLeftImgv.setImageResource(R.drawable.back);
		mCorrectFlowBtn = (Button) findViewById(R.id.btn_correction_flow);
		mCorrectFlowBtn.setOnClickListener(this);
		mTotalTV = (TextView) findViewById(R.id.tv_month_totalgprs);
		mUsedTV = (TextView) findViewById(R.id.tv_month_usedgprs);
		mToDayTV = (TextView) findViewById(R.id.tv_today_gprs);
		mRemindIMGV = (ImageView) findViewById(R.id.imgv_traffic_remind);
		mRemindTV = (TextView) findViewById(R.id.tv_traffic_remind);
	}

	private void initData() {
		long totalflow = mSP.getLong("totalflow", 0);
		long usedflow = mSP.getLong("usedflow", 0);
		if (totalflow > 0 & usedflow >= 0) {
			float scale = usedflow / totalflow;
			if (scale > 0.9) {
				mRemindIMGV.setEnabled(false);
				mRemindTV.setText("您的套餐流量即将用完！");
			} else {
				mRemindIMGV.setEnabled(true);
				mRemindTV.setText("本月流量充足请放心使用");
			}
		}
		mTotalTV.setText("本月流量：" + Formatter.formatFileSize(this, totalflow));
		mUsedTV.setText("本月已用：" + Formatter.formatFileSize(this, usedflow));
		dao = new TrafficDao(this);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dataString = sdf.format(date);
		long moblieGPRS = dao.getMoblieGPRS(dataString);
		if (moblieGPRS < 0) {
			moblieGPRS = 0;
		}
		mToDayTV.setText("本日已用：" + Formatter.formatFileSize(this, moblieGPRS));
	}

	private void registReceiver() {
		receiver = new CorrectFlowReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(receiver, filter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgv_leftbtn:
			finish();
			break;
		case R.id.btn_correction_flow:
			// 判断运营商
			int i = mSP.getInt("operator", 0);
			SmsManager smsManager = SmsManager.getDefault();
			switch (i) {
			case 0:
				// 没有设置运营商
				Toast.makeText(this, "您还没有设置运营商信息", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				// 中国移动
				smsManager.sendTextMessage("10086", null, "LLCX", null, null);
				break;
			case 2:
				// 中国移动
				smsManager.sendTextMessage("10010", null, "CXLL", null, null);
				break;
			case 3:
				// 中国电信
				smsManager.sendTextMessage("10001", null, "108", null, null);
				break;
			}
		}
	}

	class CorrectFlowReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				String body = smsMessage.getMessageBody();
				String address = smsMessage.getOriginatingAddress();
				// 如果是联通短信
				if (!address.equals("10010")) {
					return;
				}
				String[] split = body.split("，");
				// 剩余流量
				long left = 0;
				// 已用流量
				long used = 0;
				// 超出流量
				long beyond = 0;
				for (int i = 0; i < split.length; i++) {
					if (split[i].contains("已用")) {
						String usedflow = split[i].substring(7,
								split[i].length());
						used = getStringTofloat(usedflow);
					} else if (split[i].contains("剩余")) {
						String leftflow = split[i].substring(4,
								split[i].length());
						left = getStringTofloat(leftflow);
					} else if (split[i].contains("流量加油包")) {
						String beyondflow = split[i].substring(5,
								split[i].length());
						beyond = getStringTofloat(beyondflow);
					}
				}
				Editor edit = mSP.edit();
				edit.putLong("totalflow", used + left);
				edit.putLong("usedflow", used + beyond);
				edit.commit();
				mTotalTV.setText("本月流量："
						+ Formatter.formatFileSize(context, (used + left)));
				mUsedTV.setText("本月已用："
						+ Formatter.formatFileSize(context, (used + beyond)));
			}
		}
	}

	/** String 转化为 Float 类型数据 **/
	private long getStringTofloat(String str) {
		long flow = 0;
		if (!TextUtils.isEmpty(str)) {
			if (str.contains("KB")) {
				String[] split = str.split("KB");
				float m = Float.parseFloat(split[0]);
				flow = (long) (m * 1024);
			} else if (str.contains("MB")) {
				String[] split = str.split("MB");
				float m = Float.parseFloat(split[0]);
				flow = (long) (m * 1024 * 1024);
			} else if (str.contains("GB")) {
				String[] split = str.split("GB");
				float m = Float.parseFloat(split[0]);
				flow = (long) (m * 1024 * 1024 * 1024);
			}
		}
		return flow;
	}

	@Override
	public void onDestroy() {
		if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}
		super.onDestroy();
	}
}
