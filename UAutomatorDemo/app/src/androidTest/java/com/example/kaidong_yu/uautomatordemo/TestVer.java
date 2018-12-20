package com.example.kaidong_yu.uautomatordemo;

import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.os.SystemClock.sleep;
import static junit.framework.TestCase.assertFalse;


@RunWith(AndroidJUnit4.class)
public class TestVer {

    UiDevice uiDevice;

    // Instrumentation可以在主程序启动之前，创建模拟的Context；发送UI事件给应用程序；
    // 检查程序当前运行状态；控制Android如何加载应用程序，控制应用程序和控件的生命周期;
    // 可以直接调用控件的方法，对控件的属性进行查看和修改
    Instrumentation instrumentation;


    Context context;

    // 测试用例执行前，用于一些处理一些初始化工作
    @Before
    public void setUp(){
        instrumentation = InstrumentationRegistry.getInstrumentation();
        context = InstrumentationRegistry.getTargetContext();
        uiDevice = UiDevice.getInstance(instrumentation);
    }

    @Test
    public void testScan(){
        UiObject2 Scan_first = uiDevice.findObject(By.res("com.trendmicro.freetmms.gmobi:id/tv_clean"));
        //UiObject Scan = uiDevice.findObject(new UiSelector().text("Risky"));
        Scan_first.click();
        sleep(10000);
        UiObject2 Scan_second = uiDevice.findObject(By.res("com.trendmicro.freetmms.gmobi:id/card_title_end"));
        Scan_second.click();
        sleep(3000);
        UiObject2 Scan_third = uiDevice.findObject(By.res("com.trendmicro.freetmms.gmobi:id/btn_remove"));
        Scan_third.click();
        sleep(2000);
        UiObject2 Scan_forth = uiDevice.findObject(By.res("android:id/le_bottomsheet_default_confirm"));
        Scan_forth.click();

        sleep(2000);
        assertFalse(isAppInstalled(context,"com.androidantivirus.testvirus"));
        //assertTrue(isAppInstalled(context,"com.androidantivirus.testvirus"));

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
