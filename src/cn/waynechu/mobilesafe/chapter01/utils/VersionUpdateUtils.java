package cn.waynechu.mobilesafe.chapter01.utils;

import java.io.File;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import cn.waynechu.mobilesafe.R;
import cn.waynechu.mobilesafe.HomeActivity;
import cn.waynechu.mobilesafe.chapter01.entity.VersionEntity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * 版本更新工具类
 * 
 * @author waynechu
 * 
 */
public class VersionUpdateUtils {

    private static final int MESSAGE_NET_EEOR = 101;
    private static final int MESSAGE_IO_EEOR = 102;
    private static final int MESSAGE_JSON_EEOR = 103;
    private static final int MESSAGE_SHOEW_DIALOG = 104;
    protected static final int MESSAGE_ENTERHOME = 105;

    // 创建消息传递者用于更新UI
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_IO_EEOR:
                Toast.makeText(context, "IO异常", Toast.LENGTH_SHORT).show();
                enterHome();
                break;
            case MESSAGE_JSON_EEOR:
                Toast.makeText(context, "JSON解析异常", Toast.LENGTH_SHORT).show();
                enterHome();
                break;
            case MESSAGE_NET_EEOR:
                Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                enterHome();
                break;
            case MESSAGE_SHOEW_DIALOG:
                showUpdateDialog(versionEntity);
                break;
            case MESSAGE_ENTERHOME:
                Intent intent = new Intent(context, HomeActivity.class);
                context.startActivity(intent);
                context.finish();
                break;
            }
        };
    };

    private String mVersion;
    private Activity context;
    private VersionEntity versionEntity;
    private ProgressDialog mProgressDialog;
    private String url = "http://www.topblog.top/download/updateinfo.html";

    // private String url="http://192.168.2.62:8080/updateinfo.html";

    public VersionUpdateUtils(String Version, Activity activity) {
        mVersion = Version;
        context = activity;
    }

    public void getCloudVersion() {
        try {
            HttpClient client = new DefaultHttpClient();
            // 连接超时
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 5000);
            // 请求超时
            HttpConnectionParams.setSoTimeout(client.getParams(), 5000);
            // 发起请求
            HttpGet httpGet = new HttpGet(url);
            HttpResponse execute = client.execute(httpGet);
            if (execute.getStatusLine().getStatusCode() == 200) {
                // 请求成功并且获得服务器响应
                HttpEntity entity = execute.getEntity();
                String result = EntityUtils.toString(entity, "gbk");
                // 创建jsonObject对象
                JSONObject jsonObject = new JSONObject(result);
                // 获得最新版本信息
                versionEntity = new VersionEntity();
                String code = jsonObject.getString("code");
                versionEntity.versioncode = code;
                String des = jsonObject.getString("des");
                versionEntity.description = des;
                String apkurl = jsonObject.getString("apkurl");
                versionEntity.apkurl = apkurl;
                if (!mVersion.equals(versionEntity.versioncode)) {
                    handler.sendEmptyMessage(MESSAGE_SHOEW_DIALOG);
                }
            }
        } catch (ClientProtocolException e) {
            handler.sendEmptyMessage(MESSAGE_NET_EEOR);
            e.printStackTrace();
        } catch (IOException e) {
            handler.sendEmptyMessage(MESSAGE_IO_EEOR);
            e.printStackTrace();
        } catch (JSONException e) {
            handler.sendEmptyMessage(MESSAGE_JSON_EEOR);
            e.printStackTrace();
        }
    }

    /**
     * 获取到更新后弹出更新提示对话框
     * 
     * @param versionEntity
     */
    private void showUpdateDialog(final VersionEntity versionEntity) {
        // 创建 dialog
        AlertDialog.Builder builder = new Builder(context);
        builder.setTitle("检查到新版本：" + versionEntity.versioncode);
        builder.setMessage(versionEntity.description);
        // 根据服务器返回描述，设置升级描述信息
        builder.setCancelable(false);// 设置不能点击手机返回按钮隐藏对话框
        builder.setIcon(R.drawable.ic_launcher);// 设置对话框图标
        // 设置立即升级按钮点击事件
        builder.setPositiveButton("立即升级",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        initProgressDialog();
                        downloadNewApk(versionEntity.apkurl);
                    }
                });
        // 设置暂不升级按钮点击事件
        builder.setNegativeButton("暂不升级",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        enterHome();
                    }
                });
        builder.show();// 对话框必须调用show()方法否则不显示
    }

    /**
     * 初始化下载进度条对话框
     */
    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("准备下载");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.show();
    }

    /**
     * 下载新版本
     * 
     * @param apkurl
     */
    private void downloadNewApk(String apkurl) {
        RequestParams params = new RequestParams(apkurl);
        // 下载路径为 /mnt/sdcard/mobilesafe.apk
        params.setSaveFilePath(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/mobilesafe.apk");
        x.http().get(params, new Callback.ProgressCallback<File>() {

            @Override
            public void onCancelled(CancelledException arg0) {
                enterHome();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                mProgressDialog.setMessage("下载失败");
                mProgressDialog.dismiss();
                enterHome();
            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onSuccess(File arg0) {
                mProgressDialog.setMessage("下载成功");
                mProgressDialog.dismiss();
                MyUtils.installApk(context);
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                mProgressDialog.setMax((int) total);
                mProgressDialog.setMessage("正在下载...");
                mProgressDialog.setProgress((int) current);
            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onWaiting() {

            }

        });
    }

    private void enterHome() {
        handler.sendEmptyMessageDelayed(MESSAGE_ENTERHOME, 1000);
    }
}
