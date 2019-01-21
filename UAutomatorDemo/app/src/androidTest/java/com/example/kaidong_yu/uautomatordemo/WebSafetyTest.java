package com.example.kaidong_yu.uautomatordemo;

import android.graphics.Point;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.view.KeyEvent;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

public class WebSafetyTest extends AppTest {
    private static final int WAIT_TIME = 2000;

    @Test
    public void testWebProtect() throws Exception {
        String virusURL = "http://wrs21.winshipway.com/";
        String virusStatus = "lbl_status";
        String virusBlockedInfo = "Website Blocked: Dangerous Page with Advanced Threats";
        String passwordPageResourceID = ".*:id/pv_lock_page";
        setWebProtect();
        /*test chrome web surfing*/
        startAPP(CHROME_PACKAGE);
        //check lock password
        if (mDevice.hasObject(By.clazz("android.widget.TextView")
                .text("Protected by Dr. Safety"))){
            enterPassword(passwordPageResourceID);
        }
        UiObject url = mDevice.findObject(new UiSelector().resourceIdMatches(".*id/search_box_text"));
        if(url.waitForExists(WAIT_TIME)){
            enterURL(".*id/search_box_text","Search or type web address",virusURL);
        }else if(mDevice.hasObject(By.res(Pattern.compile(".*id/url_bar")))){
            enterURL(".*id/url_bar",null,virusURL);
        }
        Boolean result = mDevice.hasObject(By.res(Pattern.compile(virusStatus)))||mDevice.hasObject(By.text(virusBlockedInfo));
        assertTrue("test Chrome web surfing failed.",result);

        /*test Privacy Browser web surfing*/
        if (!mDevice.hasObject(By.res(Pattern.compile(".*id/desc")).text("Privacy Browser"))) {
            this.startMainActivityFromHomeScreen();
            mDevice.findObject(new UiSelector().className("android.widget.ImageButton")
                    .index(0)).clickAndWaitForNewWindow();
        }
        mDevice.findObject(new UiSelector().resourceIdMatches(".*id/desc").text("Privacy Browser")).clickAndWaitForNewWindow();
        enterURL(".*id/main_omnibox_input",null,virusURL);
        Boolean result1 = mDevice.hasObject(By.res(Pattern.compile(virusStatus)))||mDevice.hasObject(By.text(virusBlockedInfo));
        assertTrue("test Privacy Browser web surfing failed.",result1);
    }

    //
    private void enterURL(String resourceID,String text,String webUrl) throws Exception{
        UiObject url = mDevice.findObject(new UiSelector().resourceIdMatches(resourceID));
        if(text!=null){
            url = mDevice.findObject(new UiSelector().resourceIdMatches(resourceID).text(text));
        }
        url.clearTextField();
        url.setText(webUrl);
        mDevice.pressKeyCode(KeyEvent.KEYCODE_ENTER);
        mDevice.waitForWindowUpdate(mDevice.getCurrentPackageName(),10000);
    }

    private void setWebProtect() throws Exception {
        if (!mDevice.hasObject(By.res(Pattern.compile(".*id/desc")).text("Safe Web Surfing"))) {
            this.startMainActivityFromHomeScreen();
            mDevice.findObject(new UiSelector().className("android.widget.ImageButton")
                    .index(0)).clickAndWaitForNewWindow();
            mDevice.findObject(new UiSelector().resourceIdMatches(".*id/desc")
                    .text("Safe Web Surfing")).clickAndWaitForNewWindow();
        }
        //
        if (mDevice.hasObject(By.res(Pattern.compile(".*id/vp_promo")).scrollable(true))) {
            mDevice.findObject(new UiSelector().resourceIdMatches(".*id/close_promo")).clickAndWaitForNewWindow();
        }
        mDevice.findObject(new UiSelector().resourceIdMatches(".*id/setting_item_check_switch")
                .text("OFF")).clickAndWaitForNewWindow();
        if (mDevice.hasObject(By.res(Pattern.compile(".*id/button1")).text("OK"))) {
            mDevice.findObject(new UiSelector().resourceIdMatches(".*id/button1").text("OK")).clickAndWaitForNewWindow();
        }
        if (mDevice.hasObject(By.res(Pattern.compile(".*id/btn_request_all")))) {
            mDevice.findObject(new UiSelector().resourceIdMatches(".*id/btn_request_all")).clickAndWaitForNewWindow();
            UiScrollable settingPermission = new UiScrollable(new UiSelector().resourceIdMatches(".*id/list")).setMaxSearchSwipes(5);
            UiObject target = mDevice.findObject(new UiSelector().resourceId("android:id/title").text("Dr. Safety"));
            settingPermission.scrollIntoView(target);
            target.click();
            mDevice.findObject(new UiSelector().resourceIdMatches(".*id/switch_widget").text("OFF")).click();
            mDevice.findObject(new UiSelector().resourceIdMatches(".*id/button1").text("OK")).click();
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

}
