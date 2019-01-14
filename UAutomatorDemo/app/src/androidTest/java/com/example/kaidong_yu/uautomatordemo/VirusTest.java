package com.example.kaidong_yu.uautomatordemo;
//package com.trendmicro.tmmsa;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.Configurator;
import android.support.test.uiautomator.Direction;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Scroller;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.regex.Pattern;

import static android.os.SystemClock.sleep;
import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class VirusTest {
    private static final String TAG = VirusTest.class.getSimpleName();
    private static final String CHROME_NAME = "Chrome";
    private static final String DR_SAFETY_PACKAGE = "com.trendmicro.freetmms.gmobi";
    private static final String VIRUS_PACKAGE = "com.androidantivirus.testvirus";
    private static final String CHROME_PACKAGE ="com.android.chrome";
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final int INSTALL_TIMEOUT = 300000;
    private UiDevice mDevice;

    private static final String LocationPermission = "android.permission.ACCESS_FINE_LOCATION";
    private static final String PhoneStatePermission = "android.permission.READ_PHONE_STATE";
    private static final String StorageAccessPermission = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String CameraAccessPermission = "android.permission.CAMERA";
    private static final String StopAppPermission = "android.permission.KILL_BACKGROUND_PROCESSES";
//    @BeforeClass
//    public static void setUpClass() throws Exception {
//        Configurator configurator = Configurator.getInstance();
//        configurator.setWaitForSelectorTimeout(3000);
//        enableAllowUnknownSources();
//    }
//
//    public static void enableAllowUnknownSources() throws Exception {
//        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
//        Context context = InstrumentationRegistry.getContext();
//        boolean isNonPlayAppAllowed = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS) == 1;
//        if (!isNonPlayAppAllowed) {
//            Intent intent = new Intent().setAction(android.provider.Settings.ACTION_SECURITY_SETTINGS)
//                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//            UiScrollable settingsItem = new UiScrollable(new UiSelector().scrollable(true));
//            UiObject item = settingsItem.getChildByText(new UiSelector()
//                    .className("android.widget.LinearLayout"), "Unknown sources");
//            item.click();
//            BySelector bs = By.clazz("android.widget.Button").text(Pattern.compile("((\\bOK\\b)|(\\bAllow\\b))"));
//            device.wait(Until.hasObject(bs), LAUNCH_TIMEOUT);
//            UiObject2 o = device.findObject(bs);
//            if (o != null) {
//                o.click();
//            }
//        }
//    }

    @Before
    public void setUp() throws Exception {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
       // this.startMainActivityFromHomeScreen();
       // this.navigateTutorialPagesIfAny();
    }

    public void startMainActivityFromHomeScreen() throws Exception {
        // Start from the home screen
        mDevice.pressHome();
        // Wait for launcher
        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);
        // Launch the app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(DR_SAFETY_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);
        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(DR_SAFETY_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
        mDevice.waitForIdle();
        if(mDevice.hasObject(By.res(Pattern.compile(".*id/btn_later")))){
            mDevice.findObject(new UiSelector().resourceIdMatches(".*id/btn_later")).clickAndWaitForNewWindow();
        }
    }

    public void navigateTutorialPagesIfAny() throws Exception {
        do {
            // navigates the welcome pages
            //UiObject2 nextPageButton = mDevice.findObject(By.res(DR_SAFETY_PACKAGE, "roundBtn"));
            UiObject nextPageButton = mDevice.findObject(new UiSelector().resourceIdMatches(".*id/roundBtn"));
            if (nextPageButton.waitForExists(2000)) {
                nextPageButton.click();
                mDevice.waitForIdle();
            }
            else {
                Log.i(TAG, "Welcome pages viewed.");
                break;
            }
        } while (true);
        // Dismiss tip to show the button to add apps
        //UiObject2 closeButton = mDevice.findObject(By.res(Pattern.compile(".*id/tutorial_im_btn1")));
        UiObject closeButton = mDevice.findObject(new UiSelector().resourceIdMatches(".*id/tutorial_im_btn1"));
        if (closeButton.waitForExists(2000)) {
            Log.i(TAG, "Tip to add apps found. Closing it.");
            closeButton.click();
            mDevice.waitForIdle();
        }
        // Dismiss tutorial page to add popular apps
        //UiObject2 closeButton2 = mDevice.findObject(By.res(Pattern.compile(".*id/btn_tuto_btn3")));
        UiObject closeButton2 = mDevice.findObject(new UiSelector().resourceIdMatches(".*id/btn_tuto_btn3"));
        if (closeButton2.waitForExists(2000)) {
            Log.i(TAG, "Tutorial to add popular apps found. Closing it.");
            closeButton2.click();
            mDevice.waitForIdle();
        }
    }


    public void installVirus() throws Exception{
        String virusName = "Test Virus";
        if (!this.isAppInstalled(VIRUS_PACKAGE)) {
            this.installPackageFromAsset(VIRUS_PACKAGE + ".apk");
        }
        boolean result = isAppInstalled(VIRUS_PACKAGE);
        assertTrue("Virus is not installed successfully.", result);
    }

    @Test
    public void installAndOpenDrSafety() throws Exception{
        String packageName = "com.trendmicro.freetmms.gmobi";
//        if (!this.isAppInstalled(packageName)) {
//            this.installPackageFromAsset(packageName + ".apk");
//        }
        if (!mDevice.getCurrentPackageName().equals(DR_SAFETY_PACKAGE)) {
            this.startMainActivityFromHomeScreen();
        }
        UiScrollable scroll = new UiScrollable(new  UiSelector().resourceIdMatches(".*id/vp_whats_new")).setMaxSearchSwipes(5);
        scroll.setAsHorizontalList();
        UiObject startNowButton = mDevice.findObject(new UiSelector().resourceIdMatches(".*id/btn_what_new"));
        scroll.scrollIntoView(startNowButton);
        startNowButton.click();
        mDevice.wait(Until.hasObject(By.text("Tap here to scan your device")),3000);
        mDevice.pressBack();
        mDevice.wait(Until.hasObject(By.text("Tap here to boost your device")),3000);
        mDevice.pressBack();
        openFullPermission();
    }

    @Test
    public void  testScanAndRemoveVirus() throws Exception{
        String virusPackageName = "com.androidantivirus.testvirus";
        installVirus();
        if (!mDevice.getCurrentPackageName().equals(DR_SAFETY_PACKAGE)) {
            this.startMainActivityFromHomeScreen();
        }
        //      test scan virus and remove virus
        mDevice.findObject(By.res(Pattern.compile(".*id/tv_clean"))).click();
        mDevice.wait(Until.hasObject(By.res(Pattern.compile(".*id/card_title_end"))),15000);
        mDevice.findObject(By.res(Pattern.compile(".*id/card_title_end"))).click();
        mDevice.wait(Until.hasObject(By.res(Pattern.compile(".*id/btn_remove"))),3000);
        mDevice.findObject(By.res(Pattern.compile(".*id/btn_remove"))).click();
        mDevice.wait(Until.hasObject(By.res(Pattern.compile(".*id/button1"))),3000);
        mDevice.findObject(By.res(Pattern.compile(".*id/button1"))).click();
        assertFalse("Test scan virus failed",isAppInstalled(virusPackageName));
    }

    @Test
    public void openFullPermission() throws Exception{
        String packageName = "com.trendmicro.freetmms.gmobi";
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        if (!mDevice.hasObject(By.text("Allow Permissions"))) {
            this.startMainActivityFromHomeScreen();
            mDevice.findObject(new UiSelector().className("android.widget.ImageButton").index(0)).click();
            mDevice.findObject(new UiSelector().resourceIdMatches(".*id/permission_desc")
                    .text("Low Protection")).clickAndWaitForNewWindow();
        }
//        if(mDevice.hasObject(By.res(Pattern.compile(".*id/permission_name")).text("Allow Auto-Start"))){
//            openAllowAutoStart();
//        }
        openAllowOverlay();
        boolean AllowAppPermission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission(LocationPermission , DR_SAFETY_PACKAGE));
        if(!AllowAppPermission){
            openAllowAppPermissions();
        }
        openAllowAccessibility();
        openAllowNotificationAccess();
        openAllowUsageAccess();
    }


    private void uninstallAPP(String packageName) throws Exception{
        Context context = InstrumentationRegistry.getContext();
        Uri packageURI = Uri.parse("package:"+packageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        context.startActivity(uninstallIntent);
        mDevice.wait(Until.hasObject(By.res(Pattern.compile(".*id/button1"))),2000);
        mDevice.findObject(new UiSelector().resourceIdMatches(".*id/button1")).click();
        assertFalse(packageName+" is successful uninstalled",isAppInstalled(packageName));
    }


    public void setAppLock(String appName) throws Exception{
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

    @Test
    public void testAppLock() throws  Exception{
        setAppLock(CHROME_NAME);
        String passwordPageResourceID = ".*:id/pv_lock_page";
        startAPP(CHROME_PACKAGE);
        mDevice.wait(Until.hasObject(By.clazz("android.widget.TextView")
                .text("Protected by Dr. Safety")),2000);
        boolean isCorrect = mDevice.hasObject(By.clazz("android.widget.TextView")
                .text("Protected by Dr. Safety"));
        assertTrue("AppLock test failed",isCorrect);
        enterPassword(passwordPageResourceID);
        mDevice.waitForIdle();

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
            initialApp.click();
        }while(scrItem.scrollIntoView(initialApp));

    }

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

    @Test
    public void testWebProtect() throws Exception {
        String virusURL = "http://wrs21.winshipway.com/";
        String virusStatus = "lbl_status";
        String virusBlockedInfo = "Website Blocked: Dangerous Page with Advanced Threats";
        String passwordPageResourceID = ".*:id/pv_lock_page";
        setWebProtect();
        /*test chrome web surfing*/
        startAPP(CHROME_PACKAGE);
        if (mDevice.hasObject(By.clazz("android.widget.TextView")
                .text("Protected by Dr. Safety"))){
            enterPassword(passwordPageResourceID);
        }
        if(mDevice.hasObject(By.res(Pattern.compile(".*id/search_box_text")))){
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

    private void setWebProtect() throws Exception{
        if (!mDevice.hasObject(By.res(Pattern.compile(".*id/desc")).text("Safe Web Surfing"))) {
            this.startMainActivityFromHomeScreen();
            mDevice.findObject(new UiSelector().className("android.widget.ImageButton")
                    .index(0)).clickAndWaitForNewWindow();
            mDevice.findObject(new UiSelector().resourceIdMatches(".*id/desc")
                    .text("Safe Web Surfing")).clickAndWaitForNewWindow();
        }
        if(mDevice.hasObject(By.res(Pattern.compile(".*id/vp_promo")).scrollable(true))){
            mDevice.findObject(new UiSelector().resourceIdMatches(".*id/close_promo")).clickAndWaitForNewWindow();
        }
        mDevice.findObject(new UiSelector().resourceIdMatches(".*id/setting_item_check_switch")
                .text("OFF").index(0)).clickAndWaitForNewWindow();
        if (mDevice.hasObject(By.res(Pattern.compile(".*id/button1")).text("OK"))){
            mDevice.findObject(new UiSelector().resourceIdMatches(".*id/button1").text("OK")).clickAndWaitForNewWindow();
        }
        if(mDevice.hasObject(By.res(Pattern.compile(".*id/btn_request_all")))){
            mDevice.findObject(new UiSelector().resourceIdMatches(".*id/btn_request_all")).clickAndWaitForNewWindow();
            UiScrollable settingPermission = new UiScrollable(new UiSelector().resourceIdMatches(".*id/list")).setMaxSearchSwipes(5);
            UiObject target = mDevice.findObject(new UiSelector().resourceId("android:id/title").text("Dr. Safety"));
            settingPermission.scrollIntoView(target);
            target.click();
            mDevice.findObject(new UiSelector().resourceIdMatches(".*id/switch_widget").text("OFF")).click();
            mDevice.findObject(new UiSelector().resourceIdMatches(".*id/button1").text("OK")).click();
        }


    }

    private void openAllowAutoStart() throws Exception{
        /*start open Allow Auto-Start*/
        mDevice.findObject(new UiSelector().resourceIdMatches(".*id/permission_name").text("Allow Auto-Start"))
                .clickAndWaitForNewWindow();
        if (mDevice.hasObject(By.res(Pattern.compile(".*id/action_bar")))){
            mDevice.pressBack();
        }
    }

    private void openAllowOverlay() throws Exception{
        /*start opening Allow Overlay permission */
        //mDevice.findObject(new UiSelector().className("android.widget.RelativeLayout").index(1)).click();
        mDevice.findObject(new UiSelector().resourceIdMatches(".*id/permission_name").text("Allow Overlay"))
                .clickAndWaitForNewWindow();
        if (mDevice.hasObject(By.res(Pattern.compile(".*id/switch_widget")).text("OFF"))){
            mDevice.findObject(new UiSelector().resourceIdMatches(".*id/switch_widget").text("OFF")).click();
        }
    }

    private void openAllowAppPermissions() throws Exception{
        /*start opening Allow App permissions*/
        mDevice.findObject(new UiSelector().resourceIdMatches(".*id/permission_name")
                .text("Allow App permissions")).clickAndWaitForNewWindow();
//        mDevice.wait(Until.hasObject(By.res(Pattern.compile(".*id/permission_allow_button"))),2000);
        mDevice.findObject(new UiSelector().resourceIdMatches(".*id/permission_allow_button")
                .text("ALLOW")).clickAndWaitForNewWindow();
        mDevice.findObject(new UiSelector().resourceIdMatches(".*id/permission_allow_button")
                .text("ALLOW")).clickAndWaitForNewWindow();
        mDevice.findObject(new UiSelector().resourceIdMatches(".*id/permission_allow_button")
                .text("ALLOW")).clickAndWaitForNewWindow();
        mDevice.findObject(new UiSelector().resourceIdMatches(".*id/permission_allow_button")
                .text("ALLOW")).clickAndWaitForNewWindow();
    }

    private void openAllowAccessibility() throws Exception {
        /*start open Allow Accessibility*/
        mDevice.waitForIdle();
        mDevice.findObject(new UiSelector().resourceIdMatches(".*id/permission_name")
                .text("Allow Accessibility")).clickAndWaitForNewWindow();
        if (mDevice.hasObject(By.res(Pattern.compile(".*id/list")).scrollable(true))) {
            UiScrollable settingPermission = new UiScrollable(new UiSelector().resourceIdMatches(".*id/list"))
                    .setMaxSearchSwipes(5);
            UiObject target = mDevice.findObject(new UiSelector().resourceIdMatches(".*android:id/title")
                    .text("Dr. Safety"));
            settingPermission.scrollIntoView(target);
            target.clickAndWaitForNewWindow();
            mDevice.findObject(new UiSelector().resourceIdMatches(".*id/switch_widget").text("OFF")).clickAndWaitForNewWindow();
            mDevice.findObject(new UiSelector().resourceIdMatches(".*id/button1").text("OK")).clickAndWaitForNewWindow();
        }
    }

    private void openAllowNotificationAccess() throws Exception{
        /*start open Allow Notification Access*/
        mDevice.findObject(new UiSelector().resourceIdMatches(".*id/permission_name").text("Allow Notification Access")).clickAndWaitForNewWindow();
        if (mDevice.hasObject(By.res(Pattern.compile(".*id/title")).text("Dr. Safety"))){
            mDevice.findObject(new UiSelector().resourceIdMatches(".*id/title").text("Dr. Safety")).clickAndWaitForNewWindow();
            mDevice.findObject(new UiSelector().resourceIdMatches(".*id/button1").text("ALLOW")).clickAndWaitForNewWindow();
        }
    }

    private void openAllowUsageAccess() throws  Exception{
        /*start open Allow Usage Access*/
        mDevice.findObject(new UiSelector().resourceIdMatches(".*id/permission_name")
                .text("Allow Usage Access")).clickAndWaitForNewWindow();
        if (mDevice.hasObject(By.res(Pattern.compile(".*id/title")).text("Dr. Safety"))){
            mDevice.findObject(new UiSelector().resourceIdMatches(".*id/title")
                    .text("Dr. Safety")).clickAndWaitForNewWindow();
            mDevice.findObject(new UiSelector().className("android.widget.Switch").text("OFF")).clickAndWaitForNewWindow();
        }else if (mDevice.hasObject(By.res(Pattern.compile(".*id/list")).scrollable(true))){
            UiScrollable scrollTarget = new UiScrollable(new UiSelector().resourceIdMatches(".*id/list"))
                    .setMaxSearchSwipes(5);
            UiObject target1 = mDevice.findObject(new UiSelector().resourceIdMatches(".*id/title")
                    .text("Dr. Safety"));
            scrollTarget.scrollIntoView(target1);
            target1.clickAndWaitForNewWindow();
            mDevice.findObject(new UiSelector().className("android.widget.Switch").text("OFF")).clickAndWaitForNewWindow();
        }
       // mDevice.findObject(new UiSelector().resourceIdMatches(".*id/switch_widget").text("OFF")).click();
    }

    private void startAPP(String appPackageName) {
        final PackageManager packageManager = InstrumentationRegistry.getContext().getPackageManager();
        try {
            Intent intent = packageManager.getLaunchIntentForPackage(appPackageName);
            InstrumentationRegistry.getContext().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Uses package manager to find the package name of the device launcher. Usually this package
     * is "com.android.launcher" but can be different at times. This is a generic solution which
     * works on all platforms.`
     */
    private String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }

    private boolean isAppInstalled(String packageName){
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if(packageInfo.packageName.equals(packageName))
                return true;
        }
        return false;
    }

    private void installPackageFromAsset(String assetFileName) throws Exception {
        Context context = InstrumentationRegistry.getContext();
        File outputDir = Environment.getExternalStorageDirectory();
        if (Build.VERSION.SDK_INT > 19) {
            outputDir = context.getExternalFilesDir(null);
        }
        String outputFilePath = new File(outputDir, assetFileName).getPath();
        this.copyAssetToFile(assetFileName, outputFilePath);
        this.installPackage(outputFilePath);
    }

    private void installPackage(final String apkPath) throws Exception {
        Context context = InstrumentationRegistry.getContext();
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, 0);
        if (this.isAppInstalled(info.packageName)) {
            Log.w(TAG, String.format("%s already installed.", info.packageName));
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(apkPath)),
                "application/vnd.android.package-archive");
        String packageInstaller = "com.google.android.packageinstaller";
        if (Build.VERSION.SDK_INT < 23) {
            packageInstaller = "com.android.packageinstaller";
        }
        intent.setPackage(packageInstaller);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent); // start package installer
        UiObject okButton = mDevice.findObject(new UiSelector().resourceIdMatches(".*id/ok_button"));
        do {
            okButton.click();
            Thread.sleep(1000);
        } while (okButton.exists()); // tap the next button multiple times if possible
        BySelector bs = By.res(Pattern.compile(".*id/done_button"));
        mDevice.wait(Until.hasObject(bs), INSTALL_TIMEOUT);
        mDevice.findObject(bs).click();
    }

    private void copyAssetToFile(String assetFileName, String outputFilePath) throws Exception {
        Context context = InstrumentationRegistry.getContext();
        AssetManager assetManager = context.getResources().getAssets();
        File outputFile = new File(outputFilePath);
        if (outputFile.exists()) {
            outputFile.delete();
        }
        InputStream in = assetManager.open(assetFileName);
        OutputStream out = new FileOutputStream(outputFilePath);
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        in.close();
        out.flush();
        out.close();
    }

}

