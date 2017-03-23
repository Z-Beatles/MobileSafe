package cn.waynechu.mobilesafe;

import cn.waynechu.mobilesafe.R;
import cn.waynechu.mobilesafe.chapter01.adapter.HomeAdapter;
import cn.waynechu.mobilesafe.chapter02.LostFindActivity;
import cn.waynechu.mobilesafe.chapter02.dialog.InterPasswordDialog;
import cn.waynechu.mobilesafe.chapter02.dialog.InterPasswordDialog.MyCallBack;
import cn.waynechu.mobilesafe.chapter02.dialog.SetUpPasswordDialog;
import cn.waynechu.mobilesafe.chapter02.receiver.MyDeviceAdminReceiver;
import cn.waynechu.mobilesafe.chapter02.utils.MD5Utils;
import cn.waynechu.mobilesafe.chapter03.SecurityPhoneActivity;
import cn.waynechu.mobilesafe.chapter04.AppManagerActivity;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

/**
 * 功能选择界面
 * 
 * @author Wayne Chu
 * 
 */
public class HomeActivity extends Activity {
    // 声明GridView
    private GridView gv_home;
    // 储存手机防盗密码的 sp
    private SharedPreferences msharedPreferences;
    // 设备管理员
    private DevicePolicyManager policyManager;
    // 申请权限
    private ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化布局
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        msharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        // 初始化GridView,设置Adapter填充数据
        gv_home = (GridView) findViewById(R.id.gv_home);
        gv_home.setAdapter(new HomeAdapter(HomeActivity.this));
        // 设置条目点击事件
        gv_home.setOnItemClickListener(new OnItemClickListener() {
            // parent代表gridView，view代表每个条目的view对象，position代表每个条目的位置
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                switch (position) {
                case 0:// 手机防盗
                    if (isSetUpPassword()) {
                        showInterPswdDialog();
                    } else {
                        showSetUpPswdDialog();
                    }
                    break;
                case 1:// 通讯卫士
                    startActivity(SecurityPhoneActivity.class);
                    break;
                case 2:// 软件管家
                	startActivity(AppManagerActivity.class);
                    break;
                case 3:// 病毒查杀

                    break;
                case 4:// 混存清理

                    break;
                case 5:// 进程管理

                    break;
                case 6:// 流量统计

                    break;
                case 7:// 高级工具

                    break;
                case 8:// 设置中心

                    break;

                default:
                    break;
                }
            }
        });
        // 获取设备管理员
        policyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        // 申请权限
        componentName = new ComponentName(this, MyDeviceAdminReceiver.class);
        // 判断有无权限
        boolean active = policyManager.isAdminActive(componentName);
        if (!active) {
            Intent intent = new Intent(
                    DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                    componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    "手机卫士提醒您：远程锁屏、重置锁屏密码以及远程清除数据的功能需要激活此设备管理器。否则以上功能将无法正常使用！");
            startActivity(intent);
        }
    }

    /**
     * 弹出设置密码对话框
     */
    private void showSetUpPswdDialog() {
        final SetUpPasswordDialog setUpPasswordDialog = new SetUpPasswordDialog(
                HomeActivity.this);
        setUpPasswordDialog.setCallBack(new SetUpPasswordDialog.MyCallBack() {
            @Override
            public void ok() {
                String firstPwsd = setUpPasswordDialog.mFirstPWDET.getText()
                        .toString().trim();
                String affirmPwsd = setUpPasswordDialog.mAffirmET.getText()
                        .toString().trim();
                if (!TextUtils.isEmpty(firstPwsd)
                        && !TextUtils.isEmpty(affirmPwsd)) {
                    if (firstPwsd.equals(affirmPwsd)) {
                        savePswd(affirmPwsd);
                        setUpPasswordDialog.dismiss();
                        showInterPswdDialog();
                    } else {
                        Toast.makeText(HomeActivity.this, "两次密码不一致！",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "密码不能为空！",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void cancle() {
                setUpPasswordDialog.dismiss();
            }
        });
        setUpPasswordDialog.setCancelable(true);
        setUpPasswordDialog.show();

    }

    /**
     * 保存密码
     * 
     * @param affirmPwsd
     */
    private void savePswd(String affirmPwsd) {
        Editor edit = msharedPreferences.edit();
        // MD5加密保护用户隐私
        edit.putString("PhoneAntiTheftPWD", MD5Utils.encode(affirmPwsd));
        edit.commit();
    }

    /**
     * 弹出输入密码对话框
     */
    private void showInterPswdDialog() {
        final String password = getPassword();
        final InterPasswordDialog mInterPasswordDialog = new InterPasswordDialog(
                HomeActivity.this);
        mInterPasswordDialog.setCallBack(new MyCallBack() {
            @Override
            public void confirm() {
                if (TextUtils.isEmpty(mInterPasswordDialog.getPassword())) {
                    Toast.makeText(HomeActivity.this, "密码不能为空",
                            Toast.LENGTH_SHORT).show();
                } else if (password.equals(MD5Utils.encode(mInterPasswordDialog
                        .getPassword()))) {
                    mInterPasswordDialog.dismiss();
                    startActivity(LostFindActivity.class);
                } else {
                    mInterPasswordDialog.dismiss();
                    Toast.makeText(HomeActivity.this, "密码有误，请重新输入！",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void cancle() {
                mInterPasswordDialog.dismiss();
            }
        });
        mInterPasswordDialog.setCancelable(true);
        // 让对话框显示
        mInterPasswordDialog.show();
    }

    /**
     * 获取密码
     * 
     * @return 在sp中储存的密码
     */
    private String getPassword() {
        String password = msharedPreferences.getString("PhoneAntiTheftPWD",
                null);
        if (TextUtils.isEmpty(password)) {
            return "";
        }
        return password;
    }

    /**
     * 判断用户是否设置过手机防盗密码
     * 
     * @return sp中PhoneAntiTheftPWD存在则返回true
     */
    protected boolean isSetUpPassword() {
        String password = msharedPreferences.getString("PhoneAntiTheftPWD",
                null);
        if (TextUtils.isEmpty(password)) {
            return false;
        }
        return true;
    }

    /**
     * 开启新的Activity不关闭自己
     * 
     * @param cls
     */
    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(HomeActivity.this, cls);
        startActivity(intent);
    }

    /**
     * 用于实现按两次返回键退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long mExitTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) < 2000) {
                System.exit(0);
            } else {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
