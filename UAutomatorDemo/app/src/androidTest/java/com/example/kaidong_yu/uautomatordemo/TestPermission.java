package com.example.kaidong_yu.uautomatordemo;

import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;

import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;



import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;


@RunWith(AndroidJUnit4.class)
public class TestPermission {

    UiDevice uiDevice;
    // Instrumentation可以在主程序启动之前，创建模拟的Context；发送UI事件给应用程序；
    // 检查程序当前运行状态；控制Android如何加载应用程序，控制应用程序和控件的生命周期;
    // 可以直接调用控件的方法，对控件的属性进行查看和修改
    Instrumentation instrumentation;
    Context context;

    // 测试用例执行前，用于一些处理一些初始化工作
    @Before
    public void setUp() {
        instrumentation = InstrumentationRegistry.getInstrumentation();
        context = InstrumentationRegistry.getTargetContext();
        uiDevice = UiDevice.getInstance(instrumentation);
    }

    @Test
    public void testOverlay() throws UiObjectNotFoundException {
        String safetyPackageName = "com.trendmicro.freetmms.gmobi";

        UiObject Scan_first = uiDevice.findObject(new UiSelector().resourceId(safetyPackageName.concat(":id/tv_clean")));
        UiObject Scan_second = uiDevice.findObject(new UiSelector().resourceId(safetyPackageName.concat(":id/card_title_end")));
        UiObject Scan_third = uiDevice.findObject(new UiSelector().resourceId(safetyPackageName.concat(":id/btn_remove")));
        UiObject Scan_forth = uiDevice.findObject(new UiSelector().resourceId("android:id/button1"));




        //start Dr.Safety and search virus
        //startAPP(context,safetyPackageName);
        showAllowPermissionUI(context);




    }

    public void startAPP(Context context,String appPackageName) {
        final PackageManager packageManager = context.getPackageManager();
        try {
            Intent intent = packageManager.getLaunchIntentForPackage(appPackageName);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doStartApplicationWithPackageName(String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        final PackageManager packageManager = context.getPackageManager();
        PackageInfo packageinfo = null;
        try {
            packageinfo = packageManager.getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = packageManager
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            context.startActivity(intent);
        }
    }

    public static void showAllowPermissionUI(Context context) {
        try {
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("com.trendmicro.freetmms.gmobi");
            // package name and activity
            ComponentName comp = new ComponentName("com.trendmicro.freetmms.gmobi", "com.trendmicro.freetmms.gmobi/.component.ui.permission.AllowPermissionActivity");
            launchIntent.setComponent(comp);
            context.startActivity(launchIntent);

        } catch (android.content.ActivityNotFoundException anfe) {
            anfe.printStackTrace();
        }
    }
}

