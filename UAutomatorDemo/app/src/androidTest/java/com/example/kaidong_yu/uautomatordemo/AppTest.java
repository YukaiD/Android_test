package com.example.kaidong_yu.uautomatordemo;
//package Dr.Safety

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
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
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.util.Log;

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

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class AppTest {
    private static final String TAG = AppTest.class.getSimpleName();
    public static final String CHROME_NAME = "Chrome";
    public static final String DR_SAFETY_PACKAGE = "com.trendmicro.freetmms.gmobi";
    public static final String CHROME_PACKAGE ="com.android.chrome";
    private static final int WAIT_TIMEOUT = 2000;
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final int INSTALL_TIMEOUT = 300000;
    public UiDevice mDevice;

    @BeforeClass
    public static void setUpClass() throws Exception {
        Configurator configurator = Configurator.getInstance();
        configurator.setWaitForSelectorTimeout(3000);
//        enableAllowUnknownSources();
    }

    @Before
    public void setUp() throws Exception {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        this.startMainActivityFromHomeScreen();
        this.welcomePagesIfAny();
    }

    @Test
    public void LaunchDrSafety() throws Exception{
        if (!this.isAppInstalled(DR_SAFETY_PACKAGE)) {
            this.installPackageFromAsset(DR_SAFETY_PACKAGE + ".apk");
        }
        if (!mDevice.getCurrentPackageName().equals(DR_SAFETY_PACKAGE)) {
            this.startMainActivityFromHomeScreen();
        }
//        UiScrollable scroll = new UiScrollable(new  UiSelector().resourceIdMatches(".*id/vp_whats_new")).setMaxSearchSwipes(5);
//        scroll.setAsHorizontalList();
//        UiObject startNowButton = mDevice.findObject(new UiSelector().resourceIdMatches(".*id/btn_what_new"));
//        scroll.scrollIntoView(startNowButton);
//        startNowButton.click();
//        mDevice.wait(Until.hasObject(By.text("Tap here to scan your device")),3000);
//        mDevice.pressBack();
//        mDevice.wait(Until.hasObject(By.text("Tap here to boost your device")),3000);
//        mDevice.pressBack();
        assertTrue("Dr.Safety installation failed ",isAppInstalled(DR_SAFETY_PACKAGE));
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
        //ignore feedback
        UiObject feedback = mDevice.findObject(new UiSelector().resourceIdMatches(".*id/btn_later"));
        if(feedback.waitForExists(WAIT_TIMEOUT)){
           feedback.clickAndWaitForNewWindow();
        }
    }

    public void welcomePagesIfAny() throws Exception {
        do{
            //navigates the welcome pages
            UiScrollable scroll = new UiScrollable(new  UiSelector().resourceIdMatches(".*id/vp_whats_new"));
            if(scroll.waitForExists(WAIT_TIMEOUT)){
                scroll.setMaxSearchSwipes(5);
                scroll.setAsHorizontalList();
                UiObject startNowButton = mDevice.findObject(new UiSelector().resourceIdMatches(".*id/btn_what_new"));
                scroll.scrollIntoView(startNowButton);
                startNowButton.clickAndWaitForNewWindow();
                //ignore the guide
                mDevice.wait(Until.hasObject(By.text("Tap here to scan your device")),WAIT_TIMEOUT);
                mDevice.pressBack();
                mDevice.wait(Until.hasObject(By.text("Tap here to boost your device")),WAIT_TIMEOUT);
                mDevice.pressBack();
                mDevice.waitForIdle();
            }else {
                Log.i(TAG,"Welcome pages viewed.");
                break;
            }
        } while (true);
    }


    public static void enableAllowUnknownSources() throws Exception {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Context context = InstrumentationRegistry.getContext();
        boolean isNonPlayAppAllowed = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS) == 1;
        if (!isNonPlayAppAllowed) {
            Intent intent = new Intent().setAction(android.provider.Settings.ACTION_SECURITY_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            UiScrollable settingsItem = new UiScrollable(new UiSelector().scrollable(true));
            UiObject item = settingsItem.getChildByText(new UiSelector()
                    .className("android.widget.LinearLayout"), "Unknown sources");
            item.click();
            BySelector bs = By.clazz("android.widget.Button").text(Pattern.compile("((\\bOK\\b)|(\\bAllow\\b))"));
            device.wait(Until.hasObject(bs), LAUNCH_TIMEOUT);
            UiObject2 o = device.findObject(bs);
            if (o != null) {
                o.click();
            }
        }
    }


    /**
     * Uses package manager to find the package name of the device launcher. Usually this package
     * is "com.android.launcher" but can be different at times. This is a generic solution which
     * works on all platforms.`
     */
    public String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }

    public boolean isAppInstalled(String packageName){
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if(packageInfo.packageName.equals(packageName))
                return true;
        }
        return false;
    }

    public void installPackageFromAsset(String assetFileName) throws Exception {
        Context context = InstrumentationRegistry.getContext();
        File outputDir = Environment.getExternalStorageDirectory();
        if (Build.VERSION.SDK_INT > 19) {
            outputDir = context.getExternalFilesDir(null);
        }
        String outputFilePath = new File(outputDir, assetFileName).getPath();
        this.copyAssetToFile(assetFileName, outputFilePath);
        this.installPackage(outputFilePath);
    }

    public void installPackage(final String apkPath) throws Exception {
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

    public void copyAssetToFile(String assetFileName, String outputFilePath) throws Exception {
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

    public void uninstallAPP(String packageName) throws Exception{
        Context context = InstrumentationRegistry.getContext();
        Uri packageURI = Uri.parse("package:"+packageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        context.startActivity(uninstallIntent);
        mDevice.wait(Until.hasObject(By.res(Pattern.compile(".*id/button1"))),2000);
        mDevice.findObject(new UiSelector().resourceIdMatches(".*id/button1")).click();
        assertFalse(packageName+" is successful uninstalled",isAppInstalled(packageName));
    }

    public void startAPP(String appPackageName) throws Exception{
        final PackageManager packageManager = InstrumentationRegistry.getContext().getPackageManager();
        final Intent intent = InstrumentationRegistry.getContext().getPackageManager()
                .getLaunchIntentForPackage(appPackageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        InstrumentationRegistry.getContext().startActivity(intent);
        mDevice.wait(Until.hasObject(By.pkg(appPackageName).depth(0)), LAUNCH_TIMEOUT);
        mDevice.waitForIdle();

//        Intent intent = packageManager.getLaunchIntentForPackage(appPackageName);
//        InstrumentationRegistry.getContext().startActivity(intent);

    }


}
