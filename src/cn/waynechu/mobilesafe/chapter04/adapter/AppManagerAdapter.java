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
 * ��ʾӦ�ó�����Ϣ
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
		// ��Ϊ��������ĿҪ������ʾ�û����̣����ϵͳ������Ҫ��2
		return UserAppInfos.size() + SystemAppInfos.size() + 2;
	}

	@Override
	public Object getItem(int position) {
		if (position == 0) {
			// ��0��λ����ʾ�����û����������Item
			return null;
		} else if (position == (UserAppInfos.size() + 1)) {
			// ��UserAppInfos.size() + 1��λ����ʾ����ϵͳ���������Item
			return null;
		}
		AppInfo appInfo;
		if (position < (UserAppInfos.size() + 1)) {
			// �û����򣬶���һ��TextView��ǩ��λ��-1
			appInfo = UserAppInfos.get(position - 1);
		} else {
			// ϵͳ���򣬶�������TextView��ǩ��λ��-2
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
		// ���positionΪ0����ΪTextView
		if (position == 0) {
			TextView tv = getTextView();
			tv.setText("�û�����" + UserAppInfos.size() + "��");
			return tv;
		} else if (position == (UserAppInfos.size() + 1)) {
			// ϵͳӦ��
			TextView tv = getTextView();
			tv.setText("ϵͳ����" + SystemAppInfos.size() + "��");
			return tv;
		}
		// ��ȡ����ǰApp�Ķ���
		AppInfo appInfo;
		if (position < (UserAppInfos.size() + 1)) {
			// �û�����position 0 ΪtextView
			appInfo = UserAppInfos.get(position - 1);
		} else {
			// ϵͳӦ��
			appInfo = SystemAppInfos.get(position - UserAppInfos.size() - 2);
		}
		ViewHolder viewHolder = null;
		// instanceof��һ����Ԫ������(==��>��<һ��)����Java�ı����ؼ��֡��������ж���߶����Ƿ�Ϊ���ұ����ʵ��
		if (convertView != null & convertView instanceof LinearLayout) {
			// View�е�setTag(Onbect)��ʾ��View���һ����������ݣ�Ȼ����getTag()���������ȡ������
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
			MyClickListener listener = new MyClickListener(appInfo);
			viewHolder.mLuanchAppTV.setOnClickListener(listener);
			viewHolder.mSettingAppTV.setOnClickListener(listener);
			viewHolder.mShareAppTV.setOnClickListener(listener);
			viewHolder.mUninstallTV.setOnClickListener(listener);
		}
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
				// ����Ӧ��
				EngineUtils.startApplication(context, appInfo);
				break;
			case R.id.tv_share_app:
				// ����Ӧ��
				EngineUtils.shareApplication(context, appInfo);
				break;
			case R.id.tv_setting_app:
				// ����Ӧ��
				EngineUtils.settingAppDetail(context, appInfo);
				break;
			case R.id.tv_uninstall_app:
				// ж��Ӧ�ã���Ҫע��㲥������
				if (appInfo.packageName.equals(context.getPackageName())) {
					Toast.makeText(context, "��û��Ȩ��ж�ظ�Ӧ�ã�", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				EngineUtils.uninstallApplication(context, appInfo);
				break;
			}
		}
	}
}
