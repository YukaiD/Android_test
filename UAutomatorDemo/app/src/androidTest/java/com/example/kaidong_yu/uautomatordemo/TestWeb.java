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

import static junit.framework.TestCase.assertNotNull;


@RunWith(AndroidJUnit4.class)
public class TestWeb {

    UiDevice uiDevice;
    Instrumentation instrumentation;

    @Before
    public void setUp(){
        instrumentation = InstrumentationRegistry.getInstrumentation();
        uiDevice = UiDevice.getInstance(instrumentation);
    }

    @Test
    public void webTest(){

        UiObject chrome = uiDevice.findObject(new UiSelector().text("Chrome"));
        UiObject searchContent = uiDevice.findObject(new UiSelector().text("Search or type web address"));

        try {
            chrome.click();
            sleep(2000);
            searchContent.setText("http://wrs21.winshipway.com/");
            uiDevice.pressKeyCode(KeyEvent.KEYCODE_ENTER);
            sleep(4000);
            assertNotNull(uiDevice.findObject(new UiSelector().text("Dangerous Page")));
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
