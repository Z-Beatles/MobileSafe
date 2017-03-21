package cn.waynechu.mobilesafe.chapter04.adapter;

import java.util.List;

import cn.waynechu.mobilesafe.chapter04.AppManagerActivity;
import cn.waynechu.mobilesafe.chapter04.entity.AppInfo;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AppManagerAdapter extends BaseAdapter {
	private List<AppInfo> UserAppInfos;
	private List<AppInfo> SystemAppInfos;
	private Context context;

	public AppManagerAdapter(List<AppInfo> userAppInfos,
			List<AppInfo> systemAppInfos, AppManagerActivity appManagerActivity) {
		super();
		UserAppInfos = userAppInfos;
		SystemAppInfos = systemAppInfos;
		this.context = context;
	}

	@Override
	public int getCount() {
		return UserAppInfos.size() + SystemAppInfos.size() + 2;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
