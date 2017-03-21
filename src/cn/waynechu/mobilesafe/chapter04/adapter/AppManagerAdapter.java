package cn.waynechu.mobilesafe.chapter04.adapter;

import java.util.List;
import cn.waynechu.mobilesafe.R;
import cn.waynechu.mobilesafe.R.color;
import cn.waynechu.mobilesafe.chapter04.entity.AppInfo;
import cn.waynechu.mobilesafe.chapter04.utils.DensityUtil;
import cn.waynechu.mobilesafe.chapter04.utils.EngineUtils;
import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 显示应用程序信息
 * 
 * @author Administrator
 * 
 */
public class AppManagerAdapter extends BaseAdapter {
	private List<AppInfo> UserAppInfos;
	private List<AppInfo> SystemAppInfos;
	private Context context;

	public AppManagerAdapter(List<AppInfo> userAppInfos,
			List<AppInfo> systemAppInfos, Context context) {
		super();
		UserAppInfos = userAppInfos;
		SystemAppInfos = systemAppInfos;
		this.context = context;
	}

	@Override
	public int getCount() {
		// 因为有两个条目要用于显示用户进程，因此系统进程需要加2
		return UserAppInfos.size() + SystemAppInfos.size() + 2;
	}

	@Override
	public Object getItem(int position) {
		if (position == 0) {
			// 第0个位置显示的是用户程序个数的标签
			return null;
		} else if (position == (UserAppInfos.size() + 1)) {
			return null;
		}
		AppInfo appInfo;
		if (position < (UserAppInfos.size() + 1)) {
			// 用户程序
			appInfo = UserAppInfos.get(position - 1);
			// 多了一个TextView标签，位置-1
		} else {
			int location = position - UserAppInfos.size() - 2;
			appInfo = SystemAppInfos.get(location);
		}
		return appInfo;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 如果position为0，则为TextView
		if (position == 0) {
			TextView tv = getTextView();
			tv.setText("用户程序：" + UserAppInfos.size() + "个");
			return tv;
		} else if (position == (UserAppInfos.size() + 1)) {
			// 系统应用
			TextView tv = getTextView();
			tv.setText("系统程序：" + SystemAppInfos.size() + "个");
			return tv;
		}
		// 获取到当前App的对象
		AppInfo appInfo;
		if (position < (UserAppInfos.size() + 1)) {
			appInfo = UserAppInfos.get(position - 1);
		} else {
			// 系统应用
			appInfo = SystemAppInfos.get(position - UserAppInfos.size() - 2);
		}
		ViewHolder viewHolder = null;
		// instanceof是一个二元操作符(==、>、<一类)，是Java的保留关键字。作用是判断左边对象是否为其右边类的实例
		if (convertView != null & convertView instanceof LinearLayout) {
			viewHolder = (ViewHolder) convertView.getTag();
		} else {
			viewHolder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_appmanager_list,
					null);
			viewHolder.mAppIconImgv = (ImageView) convertView
					.findViewById(R.id.imgv_appicon);
			viewHolder.mAppLocationTV = (TextView) convertView
					.findViewById(R.id.tv_appisroom);
			viewHolder.mAppSizeTV = (TextView) convertView
					.findViewById(R.id.tv_appsize);
			viewHolder.mAppNameTV = (TextView) convertView
					.findViewById(R.id.tv_appname);
			viewHolder.mLuanchAppTV = (TextView) convertView
					.findViewById(R.id.tv_launch_app);
			viewHolder.mSettingAppTV = (TextView) convertView
					.findViewById(R.id.tv_setting_app);
			viewHolder.mShareAppTV = (TextView) convertView
					.findViewById(R.id.tv_share_app);
			viewHolder.mUninstallTV = (TextView) convertView
					.findViewById(R.id.tv_uninstall_app);
			viewHolder.mAppOptionLL = (LinearLayout) convertView
					.findViewById(R.id.ll_option_app);
			convertView.setTag(viewHolder);
		}
		if (appInfo != null) {
			viewHolder.mAppLocationTV.setText(appInfo
					.getAppLocation(appInfo.isInRoom));
			viewHolder.mAppIconImgv.setImageDrawable(appInfo.icon);
			viewHolder.mAppSizeTV.setText(Formatter.formatFileSize(context,
					appInfo.appSize));
			viewHolder.mAppNameTV.setText(appInfo.appName);
			if (appInfo.isSelected) {
				viewHolder.mAppOptionLL.setVisibility(View.VISIBLE);
			} else {
				viewHolder.mAppOptionLL.setVisibility(View.GONE);
			}
		}
		MyClickListener listener = new MyClickListener(appInfo);
		viewHolder.mLuanchAppTV.setOnClickListener(listener);
		viewHolder.mSettingAppTV.setOnClickListener(listener);
		viewHolder.mShareAppTV.setOnClickListener(listener);
		viewHolder.mUninstallTV.setOnClickListener(listener);

		return convertView;
	}

	private TextView getTextView() {
		TextView tv = new TextView(context);
		tv.setBackgroundColor(context.getResources().getColor(color.graye5));
		tv.setPadding(DensityUtil.dip2px(context, 5),
				DensityUtil.dip2px(context, 5), DensityUtil.dip2px(context, 5),
				DensityUtil.dip2px(context, 5));
		tv.setTextColor(context.getResources().getColor(color.black));
		return tv;
	}

	static class ViewHolder {
		TextView mLuanchAppTV;
		TextView mUninstallTV;
		TextView mShareAppTV;
		TextView mSettingAppTV;
		ImageView mAppIconImgv;
		TextView mAppLocationTV;
		TextView mAppSizeTV;
		TextView mAppNameTV;
		LinearLayout mAppOptionLL;
	}

	class MyClickListener implements OnClickListener {
		private AppInfo appInfo;

		public MyClickListener(AppInfo appInfo) {
			super();
			this.appInfo = appInfo;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_launch_app:
				// 启动应用
				EngineUtils.startApplication(context, appInfo);
				break;
			case R.id.tv_share_app:
				// 分享应用
				EngineUtils.shareApplication(context, appInfo);
				break;
			case R.id.tv_setting_app:
				// 设置应用
				EngineUtils.settingAppDetail(context, appInfo);
				break;
			case R.id.tv_uninstall_app:
				// 卸载应用，需要注册广播接收者
				if (appInfo.packageName.equals(context.getPackageName())) {
					Toast.makeText(context, "您没有权限卸载该应用！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				EngineUtils.uninstallApplication(context, appInfo);
				break;
			}
		}
	}
}
