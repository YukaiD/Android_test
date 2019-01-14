package com.example.kaidong_yu.uautomatordemo;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;
import android.widget.Toast;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


@RunWith(AndroidJUnit4.class)
public class TestInstall {


    private Instrumentation mInstrumentation;
    private UiDevice mUiDevice;
    Context context;

    // 测试用例执行前，用于一些处理一些初始化工作
    @Before
    public void setUp() {
        mInstrumentation = InstrumentationRegistry.getInstrumentation();
        mUiDevice = UiDevice.getInstance(mInstrumentation);
    }

    // 一个测试用例
    @Test
    public void testInstall() throws IOException {
        String apkPath = "C:\\Users\\kaidong_yu\\Downloads\\DrSafety-release.apk";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(
                Uri.fromFile(new File(apkPath)),
                "application/vnd.android.package-archive"
        );
        context.startActivity(intent);
//        Runtime.getRuntime().exec("C:\\Users\\kaidong_yu\\AppData\\Android\\SDK\\platform-tools\\adb install " + apkPath);
//
//        Runtime.getRuntime().exec("adb install " + apkPath);



    }
    // 测试用例执行完后执行
    @After
    public void tearDown() {
    }



}