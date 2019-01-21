package com.example.kaidong_yu.uautomatordemo;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.util.Log;

import org.junit.Test;

import java.util.regex.Pattern;

import static junit.framework.TestCase.assertFalse;

public class ScanVirusTest extends AppTest {
    private static final String TAG = ScanVirusTest.class.getSimpleName();
    private static final String VIRUS_PACKAGE = "com.androidantivirus.testvirus";
    private static final int SCAN_TIMEOUT = 50000;
    private static final int WAIT_TIMEOUT = 3000;

    @Test
    public void  testScanVirus() throws Exception{
        //  install Virus sample
        if (!this.isAppInstalled(VIRUS_PACKAGE)) {
            this.installPackageFromAsset(VIRUS_PACKAGE + ".apk");
        }
        if (!mDevice.hasObject(By.res(Pattern.compile(".*id/tv_clean")))) {
            this.startMainActivityFromHomeScreen();
        }
        //  test scan virus and remove virus
        mDevice.findObject(By.res(Pattern.compile(".*id/tv_clean"))).click();
        UiObject clean = mDevice.findObject(new UiSelector().resourceIdMatches(".*id/card_title_end"));
        //mDevice.wait(Until.hasObject(By.res(Pattern.compile(".*id/card_title_end"))),SCAN_TIMEOUT);
        if(clean.waitForExists(SCAN_TIMEOUT)){
            clean.clickAndWaitForNewWindow();
            mDevice.wait(Until.hasObject(By.res(Pattern.compile(".*id/btn_remove"))),WAIT_TIMEOUT);
            mDevice.findObject(By.res(Pattern.compile(".*id/btn_remove"))).click();
            mDevice.wait(Until.hasObject(By.res(Pattern.compile(".*id/button1"))),WAIT_TIMEOUT);
            mDevice.findObject(By.res(Pattern.compile(".*id/button1"))).click();
        }else{
            Log.i(TAG,"scan cost too much time");
        }
        Thread.sleep(WAIT_TIMEOUT);
        assertFalse("Test scan virus failed",isAppInstalled(VIRUS_PACKAGE));
    }
}
