package com.example.kaidong_yu.uautomatordemo;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
//import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
//import android.support.test.uiautomator.UiObjectNotFoundException;
//import android.support.test.uiautomator.UiSelector;
//import android.view.KeyEvent;

import org.junit.*;
import org.junit.runner.RunWith;


import static android.os.SystemClock.sleep;


@RunWith(AndroidJUnit4.class)
public class Hello {

    // Instrumentation可以在主程序启动之前，创建模拟的Context；发送UI事件给应用程序；
    // 检查程序当前运行状态；控制Android如何加载应用程序，控制应用程序和控件的生命周期;
    // 可以直接调用控件的方法，对控件的属性进行查看和修改
    private Instrumentation mInstrumentation;

    // 代表着Android设备
    private UiDevice mUiDevice;

    // 测试用例执行前，用于一些处理一些初始化工作
    @Before
    public void setUp() {
        mInstrumentation = InstrumentationRegistry.getInstrumentation();
        mUiDevice = UiDevice.getInstance(mInstrumentation);
    }

    // 一个测试用例
    @Test
    public void testAdd() {
        // 获取屏幕上计算器的数字"9"的控件，"com.android.calculator2:id/digit_9"为通过uiautomatoviewer工具获取的控件id
        UiObject2 digit9 = mUiDevice.findObject(By.res("com.google.android.calculator:id/digit_9"));
        // 获取屏幕上计算器的数字"8"的控件
        UiObject2 digit8 = mUiDevice.findObject(By.res("com.google.android.calculator:id/digit_8"));
        // 获取屏幕上计算器的"*"控件
        UiObject2 opMul = mUiDevice.findObject(By.res("com.google.android.calculator:id/op_mul"));
        // 获取屏幕上计算器的"="的控件
        UiObject2 opEq = mUiDevice.findObject(By.res("com.google.android.calculator:id/eq"));
        // 获取屏幕上计算器的结果显示控件
        UiObject2 result = mUiDevice.findObject(By.res("com.google.android.calculator:id/result"));

        // 自动依序执行：
        // 1.点击计算器"9"控件
        // 2.点击计算器"*"控件
        // 3.点击计算器"8"控件
        // 4.点击计算器"="控件
        digit9.click();
        sleep(2000);
        opMul.click();
        sleep(2000);
        digit8.click();
        sleep(2000);
        opEq.click();

        // 获取计算结果控件的值
        String resultValue = result.getText();

        // 进行断言判断，判断结果是否和预期一致
        Assert.assertEquals(72, Integer.parseInt(resultValue));
    }

    // 测试用例执行完后执行
    @After
    public void tearDown() {
    }

}
