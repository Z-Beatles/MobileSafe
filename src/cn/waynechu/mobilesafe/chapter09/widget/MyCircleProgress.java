package cn.waynechu.mobilesafe.chapter09.widget;

import cn.waynechu.mobilesafe.R;
import cn.waynechu.mobilesafe.chapter04.utils.DensityUtil;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/** �Զ���ؼ������ȵİ�ť */
public class MyCircleProgress extends Button {

	private Paint paint;
	/** ���� */
	private int progress;
	private int max;
	/** ����Ϊ��ʱ����ɫ */
	private int mCircleColor;
	private int mProgressColor;
	private float roundWidth;
	private int mProgressTextSize;
	private float mDistanceOFbg;

	public MyCircleProgress(Context context) {
		this(context, null);
	}

	public MyCircleProgress(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyCircleProgress(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	/***
	 * ��ʼ���ؼ�
	 * 
	 * @param context
	 */
	private void init(Context context, AttributeSet attrs) {
		paint = new Paint();
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.MyCircleProgress);
		progress = typedArray.getInteger(R.styleable.MyCircleProgress_progress,
				0);
		max = typedArray.getInteger(R.styleable.MyCircleProgress_max, 100);
		mCircleColor = typedArray.getColor(
				R.styleable.MyCircleProgress_circleColor, Color.RED);
		mProgressColor = typedArray.getColor(
				R.styleable.MyCircleProgress_progressColor, Color.WHITE);
		roundWidth = DensityUtil.dip2px(context, 5);
		mDistanceOFbg = DensityUtil.dip2px(context, 5);
		mProgressTextSize = DensityUtil.dip2px(context, 16);
		typedArray.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// �õ�Բ�ĵ�λ��
		int centerX = getWidth() / 2;
		int centerY = getHeight() / 2;
		int radius = (int) (centerX - mDistanceOFbg);
		// �����ԲȦ
		paint.setColor(mCircleColor);
		paint.setAntiAlias(true); // ��Paint���Ͼ��
		paint.setStyle(Paint.Style.STROKE); // ���������ʽ�������
		paint.setStrokeWidth(roundWidth); // ���û��ʿ��
		canvas.drawCircle(centerX, centerY, radius, paint); // ��Բ
		// ����������
		paint.setColor(mProgressColor);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(roundWidth);
		// ���ڶ����Բ������״�ʹ�С�Ľ���
		RectF oval = new RectF(centerX - radius, centerY - radius, centerX
				+ radius, centerY + radius);
		paint.setStyle(Paint.Style.STROKE);
		// ��ת��ͼƬ�Ŀ����
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG));
		canvas.drawArc(oval, 0, 360 * progress / max, false, paint);
		// չʾ��������
		paint.setStrokeWidth(0);
		paint.setColor(mProgressColor);
		paint.setTextSize(mProgressTextSize);
		// ��������
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		int percent = (int) (((float) progress / (float) max) * 100);
		// �м�Ľ��Ȱٷֱȣ���ת����float�ٽ��г������㣬��Ȼ��Ϊ0
		float textWidth = paint.measureText(percent + "%");
		if (percent > 0) {
			// �������Ȱٷֱ�
			canvas.drawText(percent + "%", centerX - textWidth / 2,
					(float) (centerY + radius - mDistanceOFbg * 6), paint);
		}
	}

	/** ���ý��ȣ���Ϊ�̰߳�ȫ�ؼ������ڿ��Ƕ��ߵ����⣬��Ҫͬ�� */
	public synchronized void setProcess(int process) {
		if (process < 0) {
			throw new IllegalArgumentException("progress not less than 0");
		}
		if (process > max) {
			process = max;
		}
		if (process <= max) {
			this.progress = process;
			// �ػ�ͼƬ
			postInvalidate();
		}
	}

	public synchronized int getProcess() {
		return progress;
	}

	public synchronized void setMax(int max) {
		if (max < 0) {
			throw new IllegalArgumentException("max not less than 0");
		}
		this.max = max;
	}

}
