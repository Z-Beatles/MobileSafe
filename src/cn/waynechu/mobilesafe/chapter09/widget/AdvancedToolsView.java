package cn.waynechu.mobilesafe.chapter09.widget;

/**
 * 自定义控件类
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.waynechu.mobilesafe.R;

public class AdvancedToolsView extends RelativeLayout {

	private TextView mDesriptionTV;
	private String desc = "";
	private Drawable drawable;
	private ImageView mLeftImgv;

	public AdvancedToolsView(Context context) {
		super(context);
		init(context);
	}

	public AdvancedToolsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 获取到属性对象
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.AdvancedToolsView);
		// 获取到desc属性，与attrs.xml中定义的属性绑定
		desc = mTypedArray.getString(R.styleable.AdvancedToolsView_desc);
		drawable = mTypedArray
				.getDrawable(R.styleable.AdvancedToolsView_android_src);
		// 回收属性对象
		mTypedArray.recycle();
		init(context);
	}

	public AdvancedToolsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * 初始化控件
	 * 
	 * @param context
	 */
	private void init(Context context) {
		// 将资源转化成view对象显示在自己身上
		View view = View.inflate(context, R.layout.ui_advancedtools_view, null);
		this.addView(view);
		mDesriptionTV = (TextView) findViewById(R.id.tv_decription);
		mLeftImgv = (ImageView) findViewById(R.id.imgv_left);
		mDesriptionTV.setText(desc);
		if (drawable != null)
			mLeftImgv.setImageDrawable(drawable);
	}
}
