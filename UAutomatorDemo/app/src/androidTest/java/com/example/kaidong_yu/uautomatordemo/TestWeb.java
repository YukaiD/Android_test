package com.example.kaidong_yu.uautomatordemo;

import android.app.Instrumentation;
import android.content.Context;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.Direction;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.view.KeyEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.os.SystemClock.sleep;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;


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
    public void webTest() {
        String virusURL = "http://wrs21.winshipway.com/";
        String browserName = "Chrome";
        String searchAdr = "com.android.chrome:id/search_box_text";
        String urlAdr = "com.android.chrome:id/url_bar";
        String virusStatus = "lbl_status";
        UiObject chrome = uiDevice.findObject(new UiSelector().text(browserName));
        UiObject searchContent = uiDevice.findObject(new UiSelector().resourceId(searchAdr));
        UiObject target = uiDevice.findObject(new UiSelector().resourceId(virusStatus));
        UiObject url = uiDevice.findObject(new UiSelector().resourceId(urlAdr));

        try {
            chrome.clickAndWaitForNewWindow();
            searchContent.clearTextField();
            searchContent.setText(virusURL);
            uiDevice.pressEnter();
            uiDevice.pressMenu();
            UiObject reflash = uiDevice.findObject(new UiSelector().resourceId("com.android.chrome:id/button_five"));
            reflash.clickAndWaitForNewWindow();
            sleep(2000);
            if (!target.exists()){
            url.clickAndWaitForNewWindow();
            uiDevice.pressEnter();
            }
            sleep(2000);
            assertTrue(uiDevice.hasObject(By.res(virusStatus)));
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

}
