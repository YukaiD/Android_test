package com.example.kaidong_yu.uautomatordemo;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.view.KeyEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;



@RunWith(AndroidJUnit4.class)
public class TestChrome {

    UiDevice uiDevice;
    Instrumentation instrumentation;

    @Before
    public void setUp(){
        instrumentation = InstrumentationRegistry.getInstrumentation();
        uiDevice = UiDevice.getInstance(instrumentation);
    }

    @Test
    public void launchChrome(){
        UiObject chrome = uiDevice.findObject(new UiSelector().text("Chrome"));
        UiObject searchContent = uiDevice.findObject(new UiSelector().text("Search or type web address"));

        try {
            chrome.click();
            sleep(2000);
            searchContent.setText("www.baidu.com");
            uiDevice.pressKeyCode(KeyEvent.KEYCODE_ENTER);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void sleep(int mint){
        try{
            Thread.sleep(mint);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

}
