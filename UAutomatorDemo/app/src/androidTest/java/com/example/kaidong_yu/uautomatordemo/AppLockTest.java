package com.example.kaidong_yu.uautomatordemo;

import android.graphics.Point;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

public class AppLockTest extends AppTest {


    @Test
    public void AppLockTest() throws  Exception{
        String passwordPageResourceID = ".*:id/pv_lock_page";
        setAppLock(CHROME_NAME);
        startAPP(CHROME_PACKAGE);
        Thread.sleep(5000);
        mDevice.wait(Until.hasObject(By.clazz("android.widget.TextView")
                .text("Protected by Dr. Safety")),5000);
        boolean isCorrect = mDevice.hasObject(By.clazz("android.widget.TextView")
                .text("Protected by Dr. Safety"));
        assertTrue("AppLock test failed",isCorrect);
        enterPassword(passwordPageResourceID);

    }

    private void setAppLock(String appName) throws Exception{
        initialAppLock();
        UiScrollable settingPermission = new UiScrollable(new UiSelector().resourceIdMatches(".*id/app_list")).setMaxSearchSwipes(30);
        UiObject target = mDevice.findObject(new UiSelector().resourceIdMatches(".*id/desc").text(appName));
        settingPermission.scrollIntoView(target);
        target.getFromParent(new UiSelector().resourceIdMatches(".*id/btn_select").checked(false)).click();
        if (mDevice.hasObject(By.res(Pattern.compile(".*id/btn_lock")))) {
            mDevice.findObject(new UiSelector().resourceIdMatches(".*id/btn_lock")).clickAndWaitForNewWindow();
            enterPassword(".*id/pv_setting"); //set password
            mDevice.waitForWindowUpdate(DR_SAFETY_PACKAGE, 2000);
            enterPassword(".*id/pv_setting"); //confirm password
        }
        //check allow fingerprint
        mDevice.wait(Until.hasObject(By.res(Pattern.compile(".*id/positiveButton"))),2000);
        if(mDevice.hasObject(By.res(Pattern.compile(".*id/positiveButton")).text("NO"))){
            mDevice.findObject(By.res(Pattern.compile(".*id/positiveButton")).text("NO")).click();
        }
        //mDevice.wait(Until.hasObject(By.res(Pattern.compile(".*id/btn_request_all"))),2000);
        //allow usage access on first time
        if(mDevice.hasObject(By.res(Pattern.compile(".*id/btn_request_all")))){
            mDevice.findObject(new UiSelector().resourceIdMatches(".*id/btn_request_all"))
                    .clickAndWaitForNewWindow();
            if(mDevice.hasObject(By.res(Pattern.compile(".*id/title")).text("Dr. Safety"))){
                mDevice.findObject(new UiSelector().resourceIdMatches(".*id/title")
                        .text("Dr. Safety")).clickAndWaitForNewWindow();
                mDevice.findObject(new UiSelector().text("OFF")).clickAndWaitForNewWindow();
            }else if(mDevice.hasObject(By.clazz("android.widget.TextView").text("USAGE DATA ACCESS"))){
                UiScrollable scrollTarget = new UiScrollable(new UiSelector()
                        .resourceIdMatches(".*id/list")).setMaxSearchSwipes(5);
                UiObject target1 = mDevice.findObject(new UiSelector().resourceIdMatches(".*id/title")
                        .text("Dr. Safety"));
                scrollTarget.scrollIntoView(target1);
                target1.clickAndWaitForNewWindow();
                mDevice.findObject(new UiSelector().text("OFF")).clickAndWaitForNewWindow();
            }
        }else{
            mDevice.pressHome();
        }
    }

    private void enterPassword(String resourceId) throws Exception{
        UiObject targetView = mDevice.findObject(new UiSelector().resourceIdMatches(resourceId)) ;
        int leftX = targetView.getBounds().left;
        int leftY = targetView.getBounds().top;
        int rightX = targetView.getBounds().right;
        int rightY = targetView.getBounds().bottom;
        int stepX = (rightX-leftX)/3;
        int stepY = (rightY-leftY)/3;
        Point p = new Point();
        p.x = (leftX+(rightX-leftX)/6);
        p.y = (leftY+(rightY-leftY)/6);
        mDevice.swipe(new Point[]{p,new Point(p.x+stepX,p.y),new Point(p.x+stepX*2,p.y),new Point(p.x+stepX*2,p.y+stepY),new Point(p.x+stepX*2,p.y+stepY*2)},6);
    }

    private void initialAppLock() throws Exception {
        if (!mDevice.hasObject(By.res(Pattern.compile(".*id/desc")).text("App Lock"))) {
            this.startMainActivityFromHomeScreen();
            mDevice.findObject(new UiSelector().className("android.widget.ImageButton").index(0)).clickAndWaitForNewWindow();
        }
        mDevice.findObject(new UiSelector().resourceIdMatches(".*id/desc").text("App Lock")).clickAndWaitForNewWindow();
        //check enter password
        if(mDevice.hasObject(By.res(Pattern.compile(".*id/tv_title_unlock_settingpage")))){
            enterPassword(".*id/pv_unlock");
        }
        //check permission
        if (mDevice.hasObject(By.res(Pattern.compile(".*id/btn_request_permission")))) {
            mDevice.findObject(new UiSelector().resourceIdMatches(".*id/btn_request_permission")).clickAndWaitForNewWindow();
        }
        //initial App Lock
        UiScrollable scrItem = new UiScrollable(new UiSelector()
                .className("android.support.v7.widget.RecyclerView"));
        UiObject initialApp = mDevice.findObject(new UiSelector().resourceIdMatches(".*id/btn_select")
                .checked(true));
        do {
            scrItem.scrollIntoView(initialApp);
            if (initialApp.exists()){
                initialApp.click();
            }
        }while(scrItem.scrollIntoView(initialApp));

    }
}
