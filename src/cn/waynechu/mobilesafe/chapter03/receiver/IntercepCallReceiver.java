package cn.waynechu.mobilesafe.chapter03.receiver;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import cn.waynechu.mobilesafe.chapter03.db.dao.BlackNumberDao;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 广播接收者用于拦截黑名单电话
 * 
 * @author waynechu
 * 
 */
public class IntercepCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(
                "config", Context.MODE_PRIVATE);
        boolean BlackNumStatus = mSharedPreferences.getBoolean(
                "BlackNumStatus", true);
        if (!BlackNumStatus) {
            // 未打开黑名单拦截功能
            return;
        }
        BlackNumberDao dao = new BlackNumberDao(context);
        if (!intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String mIncomNumber = "";
            // 如果是来电
            TelephonyManager mTelephonyManager = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);
            switch (mTelephonyManager.getCallState()) {
            case TelephonyManager.CALL_STATE_RINGING:
                mIncomNumber = intent.getStringExtra("incoming_number");
                int blackContactMode = dao.getBlackContactMode(mIncomNumber);
                if (blackContactMode == 1 || blackContactMode == 3) {
                    // 观察呼叫记录变化（另外一个应用程序数据库的变化） ，如果呼叫记录生成，就把呼叫记录删除
                    Uri uri = Uri.parse("content://call_log/calls");
                    context.getContentResolver().registerContentObserver(
                            uri,
                            true,
                            new CallLogObserver(new Handler(), mIncomNumber,
                                    context));
                    endcall(context);
                }
                break;
            }
        }
    }

    /**
     * 通过内容观察者观察数据库的变化
     * 
     * @author waynechu
     * 
     */
    private class CallLogObserver extends ContentObserver {
        private String incomingNumber;
        private Context context;

        public CallLogObserver(Handler handler, String incomingNumber,
                Context context) {
            super(handler);
            this.incomingNumber = incomingNumber;
            this.context = context;
        }

        // 观察到数据库内容发生变化时调用
        @Override
        public void onChange(boolean selfChange) {
            Log.i("CallLogObserver", "呼叫记录数据库的内容变化了");
            context.getContentResolver().unregisterContentObserver(this);
            deleteCallLog(incomingNumber, context);
            super.onChange(selfChange);
        }
    }

    /**
     * 删除呼叫记录
     * 
     * @param incomingNumber
     * @param context
     */
    private void deleteCallLog(String incomingNumber, Context context) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("conten://call_log/calls");
        Cursor cursor = resolver.query(uri, new String[] { "_id" }, "number=?",
                new String[] { incomingNumber }, "_id desc limit 1");
        if (cursor.moveToNext()) {
            String id = cursor.getString(0);
            resolver.delete(uri, "_id=?", new String[] { id });
        }
    }

    /**
     * 挂断电话，需要复制两个AIDL
     * 
     * @param context
     */
    /*
     * 接口定义语言（Interface Definition Language，IDL）来公开服务的接口
     */
    private void endcall(Context context) {
        try {
            Class mclass = context.getClassLoader().loadClass(
                    "android.os.ServiceManager");
            Method method = mclass
                    .getDeclaredMethod("getService", String.class);
            IBinder iBinder = (IBinder) method.invoke(null,
                    Context.TELEPHONY_SERVICE);
            ITelephony itelephony = ITelephony.Stub.asInterface(iBinder);
            itelephony.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
