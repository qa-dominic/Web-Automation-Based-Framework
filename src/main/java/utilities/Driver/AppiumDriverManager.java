package utilities.Driver;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.Logger.LoggingUtils;
import utilities.ApkPath.Apk;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class AppiumDriverManager {
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final int PORT = 4723;
    private static final String APP_ID = "com.mlhuillier.mlwallet";
    private static final String APP_ACTIVITY = "MainActivity";
    private static final String NODE_PATH = "C:\\Program Files\\nodejs\\node.exe";
    private static final String APPIUM_PATH = "C:\\Users\\MONC20248261\\AppData\\Roaming\\npm\\node_modules\\appium";
    private static final int IMPLICIT_WAIT_DURATION = 10;
    private static final int EXPLICIT_WAIT_DURATION = 30;

    private static AppiumDriverLocalService service;
    private static AndroidDriver driver;
    private static WebDriverWait wait;

    private AppiumDriverManager() {
        // Private constructor to prevent instantiation
    }

    public static void setupServer() {
        if (service == null || !service.isRunning()) {
            service = new AppiumServiceBuilder()
                    .usingDriverExecutable(new File(NODE_PATH))
                    .withAppiumJS(new File(APPIUM_PATH))
                    .withIPAddress(IP_ADDRESS)
                    .usingPort(PORT)
                    .build();
            service.start();
            LoggingUtils.info("Appium server started on " + service.getUrl());
        }
    }

    public static void initializeDriver() {
        if (driver == null) {
            setupServer();
            UiAutomator2Options options = new UiAutomator2Options()
                    .setDeviceName("emulator-5554")
                    .setApp(Apk.API_DEMOS_APK.toString())
                    .setAutomationName("uiautomator2")
                    .setNoReset(false)
                    .eventTimings();

            try {
                driver = new AndroidDriver(new URL(service.getUrl().toString()), options);
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT_DURATION));
                wait = new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT_DURATION));
                LoggingUtils.info("AndroidDriver initialized successfully");
            } catch (MalformedURLException e) {
                LoggingUtils.error("Failed to initialize AndroidDriver: " + e.getMessage());
                throw new RuntimeException("Failed to initialize AndroidDriver", e);
            }
        }
    }

    public static void stopServer() {
        if (driver != null) {
            driver.quit();
            driver = null;
            wait = null;
        }
        if (service != null && service.isRunning()) {
            service.stop();
            LoggingUtils.info("Appium server stopped");
        }
    }

    public static void startActivity() {
        Map<String, String> args = new HashMap<>();
        args.put("component", String.format("%s/%s", APP_ID, APP_ACTIVITY));
        LoggingUtils.info("Starting Activity: " + APP_ID + "/" + APP_ACTIVITY);
        driver.executeScript("mobile: startActivity", args);
    }

    public static void clearApp() {
        LoggingUtils.info("Clearing app: " + APP_ID);
        driver.executeScript("mobile: clearApp", ImmutableMap.of("appId", APP_ID));
    }

    public static void resetApp() {
        clearApp();
        startActivity();
    }

    public static AndroidDriver getAndroidDriver() {
        if (driver == null) {
            initializeDriver();
        }
        return driver;
    }

    public static WebDriverWait getWait() {
        if (wait == null) {
            initializeDriver();
        }
        return wait;
    }
}