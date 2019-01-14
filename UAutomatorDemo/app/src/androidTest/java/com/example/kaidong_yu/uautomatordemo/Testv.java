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
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;

import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertFalse;


@RunWith(AndroidJUnit4.class)
public class Testv {

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
    public void testScan() throws UiObjectNotFoundException {
        String safetyPackageName = "com.trendmicro.freetmms.gmobi";
        String virusPackageName = "com.androidantivirus.testvirus";
//        String[] hintKeywordArray = new String[]{"INSTALL","安装"};
//        for (String word : hintKeywordArray){
//            UiObject install = uiDevice.findObject(new UiSelector().textContains(word));
//
//        }
        UiObject install = uiDevice.findObject(new UiSelector().textContains("INSTALL"));
        UiObject accept = uiDevice.findObject(new UiSelector().text("ACCEPT"));
        UiObject Scan_first = uiDevice.findObject(new UiSelector().resourceId(safetyPackageName.concat(":id/tv_clean")));
        UiObject Scan_second = uiDevice.findObject(new UiSelector().resourceId(safetyPackageName.concat(":id/card_title_end")));
        UiObject Scan_third = uiDevice.findObject(new UiSelector().resourceId(safetyPackageName.concat(":id/btn_remove")));
        UiObject Scan_forth = uiDevice.findObject(new UiSelector().resourceId("android:id/button1"));


        //install testvirus
        showMarket(context);
        install.clickAndWaitForNewWindow();
        accept.clickAndWaitForNewWindow();

        //start Dr.Safety and search virus
        startAPP(context,safetyPackageName);
        Scan_first.clickAndWaitForNewWindow();
        Scan_second.clickAndWaitForNewWindow();
        Scan_third.clickAndWaitForNewWindow();
        Scan_forth.clickAndWaitForNewWindow();

        //check virus uninstalled
        assertFalse(isAppInstalled(context,virusPackageName));

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

    public static void showMarket(Context context) {
        final String appPackageName = "com.androidantivirus.testvirus";
        try {
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("com.android.vending");
            // package name and activity
            ComponentName comp = new ComponentName("com.android.vending", "com.google.android.finsky.activities.LaunchUrlHandlerActivity");
            launchIntent.setComponent(comp);
            launchIntent.setData(Uri.parse("market://details?id="+appPackageName));
            context.startActivity(launchIntent);

        } catch (android.content.ActivityNotFoundException anfe) {
            anfe.printStackTrace();
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }
}
