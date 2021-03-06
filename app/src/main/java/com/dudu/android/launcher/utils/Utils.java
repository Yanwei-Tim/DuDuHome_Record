package com.dudu.android.launcher.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.dudu.android.launcher.LauncherApplication;
import com.dudu.android.launcher.R;
import com.dudu.navi.event.NaviEvent;

import org.slf4j.LoggerFactory;

import de.greenrobot.event.EventBus;

public class Utils {

    private static final String TAG = "Utils";

    public static boolean isTaxiVersion() {
        int code = LauncherApplication.getContext().
                getResources().getInteger(R.integer.dudu_version_code);
        return code == Constants.VERSION_TYPE_TAXI;
    }

    public static void startThirdPartyApp(Context context, String packageName) {
        Intent intent;
        PackageManager packageManager = context.getPackageManager();
        intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }

    public static boolean startThirdPartyApp(Context context, String packageName, int stringId) {
        Intent intent;
        PackageManager packageManager = context.getPackageManager();
        intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断当前是demo版本还是正式版
     *
     * @param context
     * @return
     */
    public static boolean isDemoVersion(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            String versionName = packageInfo.versionName;
            if (versionName.contains("demo")) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(TAG, e.getMessage() + "");
        }
        //没写IMEI号的机器 也算Demo版本
        String imei = DeviceIDUtil.getIMEI(context);
        if (imei != null && imei.length() == 15) {
            return false;
        }

        return true;
    }

    public static boolean isTRVersion(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            String versionName = packageInfo.versionName;
            if (versionName.contains("TR")) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(TAG, e.getMessage() + "");
        }

        return false;
    }

    public static void checkSimCardSerialNumber(Context context) {
        String serialNumber = SharedPreferencesUtils.getStringValue(context,
                Constants.KEY_SIM_CARD_SERIAL_NUMBER, "");
        if (TextUtils.isEmpty(serialNumber)) {
            serialNumber = DeviceIDUtil.getSimSerialNumber(context);
            SharedPreferencesUtils.putStringValue(context, Constants.KEY_SIM_CARD_SERIAL_NUMBER,
                    serialNumber);
            return;
        }

        LogUtils.i(TAG, "sim card serial number: " + serialNumber);
        String curSerialNumber = DeviceIDUtil.getSimSerialNumber(context);
        if (TextUtils.isEmpty(curSerialNumber)) {
            return;
        }

        if (serialNumber.equals(curSerialNumber)) {
            LogUtils.i(TAG, "sim card serial number match...");
            DialogUtils.dismissSimCardReplaceDialog();
        } else {
            LogUtils.i(TAG, "sim card serial number don't match...");
            DialogUtils.showSimCardReplaceDialog(context);
        }
    }

    /**
     * 检查用户是否已激活
     *
     * @param context
     * @return
     */
    public static boolean checkUserStateIsActive(Context context) {
        boolean flag = false;
        //检查是否已激活
        //接口连接代码，暂时先空下来
        return flag;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static void checkSimCardState(Context context) {
//        if (isDemoVersion(context)) {
//            return;
//        }

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int state = tm.getSimState();
        switch (state) {
            case TelephonyManager.SIM_STATE_READY:
                LoggerFactory.getLogger("init.receiver.sim").debug("sim card ready:{}", state);
//                DialogUtils.dismissWithoutSimCardDialog();

                // 如果插入了sim卡则检测是否匹配--暂时不开此功能
//                checkSimCardSerialNumber(context);
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
            case TelephonyManager.SIM_STATE_ABSENT:
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                LoggerFactory.getLogger("init.receiver.sim").debug("sim card not ready:{}", state);
//                DialogUtils.showWithoutSimCardDialog(context);
                break;
        }
    }


    public static String getOBDType(Context context) {
        PackageInfo packageInfo;
        String obd = "";
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            String versionName = packageInfo.versionName;
            if (versionName.contains("thread")) {
                obd = "thread";
            } else if (versionName.contains("pod")) {
                obd = "pod";
            } else if (versionName.contains("xfa")) {
                obd = "xfa";
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(TAG, e.getMessage() + "");
        }

        return obd;
    }

    public static String getJDType(Context context) {
        PackageInfo packageInfo;
        String jd = "didi";
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            String versionName = packageInfo.versionName;
            if (versionName.contains("didi")) {
                jd = "didi";
            } else if (versionName.contains("feidi")) {
                jd = "feidi";
            } else if (versionName.contains("uber")) {
                jd = "uber";
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(TAG, e.getMessage() + "");
        }

        return jd;
    }

    public static void openJD(Context context) {
        boolean has_start = false;
        switch (getJDType(context)) {

            case "feidi":
                has_start = startThirdPartyApp(context, "com.miu360.feidi.taxi", R.string.error_no_didi);
                break;
            case "didi":
                has_start = startThirdPartyApp(context, "com.sdu.didi.gsui", R.string.error_no_didi);
                break;
            case "uber":
                has_start = startThirdPartyApp(context, "com.ubercab.driver", R.string.error_no_uber);
                break;
            default:
                has_start = startThirdPartyApp(context, "com.sdu.didi.gsui", R.string.error_no_didi);
                break;
        }

        //显示返回的按钮
        if (has_start) {
            EventBus.getDefault().post(NaviEvent.FloatButtonEvent.SHOW);
        }
    }
}
