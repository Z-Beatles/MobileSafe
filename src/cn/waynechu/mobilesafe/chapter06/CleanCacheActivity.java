package cn.waynechu.mobilesafe.chapter06;

import cn.waynechu.mobilesafe.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class CleanCacheActivity extends Activity implements OnClickListener {

	private PackageManager pm;
	private long cacheMemory;
	private AnimationDrawable animation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cleancache);
		initView();
		pm = getPackageManager();
		Intent intent = getIntent();
		cacheMemory = intent.getLongExtra("cacheMemory", 0);
		initDate();
	}

	private void initView() {
		findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.rose_red));
		((TextView)findViewById(R.id.tv_title)).setText("ª∫¥Ê«Â¿Ì");
		ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
		mLeftImgv.setOnClickListener(this);
		mLeftImgv.setImageResource(R.drawable.back);
		animation = (AnimationDrawable)findViewById(R.id.imgv_trashbin_cacheclean).getBackground();
		
		
	}

	private void initDate() {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}
