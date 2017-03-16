package cn.waynechu.mobilesafe.chapter02;

import cn.waynechu.mobilesafe.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

/**
 * ����һ������ʵ������ʶ��Ĺ��ܣ�������̳и��༴��
 */
public abstract class BaseSetUpActivity extends Activity {

	public SharedPreferences sp;
	private GestureDetector mGestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// ��ʼ������ʶ����
		mGestureDetector = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() {
					// e1������ָ��һ�δ�����Ļ���¼���e2������ָ�뿪��Ļ��һ˲���ʱ��
					// velocityXˮƽ�����ϵ��ٶȣ���λpix/s��velocityY��ֱ�����ϵ��ٶ�
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						if (Math.abs(velocityX) < 200) {
							Toast.makeText(getApplicationContext(),
									"��Ч�������ƶ�̫��", Toast.LENGTH_SHORT).show();
							return true;
						}
						if ((e2.getRawX() - e1.getRawX()) > 200) {
							// �������һ�����Ļ����ʾ��һ������
							showPre();
							overridePendingTransition(R.anim.pre_in,
									R.anim.pre_out);
							return true;
						}
						if ((e1.getRawX() - e2.getRawX()) > 200) {
							// �������󻬶���Ļ����ʾ��һ������
							showNext();
							overridePendingTransition(R.anim.next_in,
									R.anim.next_out);
							return true;
						}
						return super.onFling(e1, e2, velocityX, velocityY);
					}

				});
	}

	public abstract void showPre();

	public abstract void showNext();

	// ������ʶ����ȥʶ���¼�
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// ���������¼�
		mGestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	/**
	 * �����µ�activity���ر��Լ�
	 * 
	 * @param cls
	 */
	public void startActivityAndFinishSelf(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
		finish();
	}

}