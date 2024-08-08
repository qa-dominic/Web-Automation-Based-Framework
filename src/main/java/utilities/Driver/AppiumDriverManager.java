package utilities.Driver;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.Logger.LoggingUtils;
import utilities.ApkPath.Apk;
import utilities.PropertyReader.propertyReader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


public class AppiumDriverManager {
    private static AppiumDriverManager instance;
    private AppiumDriverLocalService service;
    private AndroidDriver driver;
    private WebDriverWait wait;
    private final propertyReader propertyReader = new propertyReader("config.properties");

    private AppiumDriverManager() {

    }

    public static AppiumDriverManager getInstance() {
        if (instance == null) {
            synchronized (AppiumDriverManager.class) {
                if (instance == null) {
                    instance = new AppiumDriverManager();
                }
            }
        }
        return instance;
    }

    public void setupServer() {
        if (service == null || !service.isRunning()) {
            service = new AppiumServiceBuilder()
                    .usingDriverExecutable(new File(propertyReader.getProperty("node.path")))
                    .withAppiumJS(new File(propertyReader.getProperty("appium.path")))
                    .withIPAddress(propertyReader.getProperty("ip.address"))
                    .usingPort(Integer.parseInt(propertyReader.getProperty("port")))
                    .build();
            service.start();
            LoggingUtils.info("Appium server started on " + service.getUrl());
        }
    }

    public void initializeDriver() {
        if (driver == null) {
            setupServer();
            UiAutomator2Options options = new UiAutomator2Options()
                    .setDeviceName(propertyReader.getProperty("device.name"))
                    .setApp(Apk.API_DEMOS_APK.toString())
                    .setAutomationName("uiautomator2")
                    .setNoReset(false)
                    .eventTimings();

            try {
                driver = new AndroidDriver(new URL(service.getUrl().toString()), options);
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(
                        Integer.parseInt(propertyReader.getProperty("implicit.wait.duration"))));
                wait = new WebDriverWait(driver, Duration.ofSeconds(
                        Integer.parseInt(propertyReader.getProperty("explicit.wait.duration"))));
                LoggingUtils.info("AndroidDriver initialized successfully");
            } catch (MalformedURLException e) {
                LoggingUtils.error("Failed to initialize AndroidDriver: " + e.getMessage());
                throw new RuntimeException("Failed to initialize AndroidDriver", e);
            }
        }
    }

    public void stopServer() {
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

    public void startActivity() {
        Map<String, String> args = new HashMap<>();
        args.put("component", String.format("%s/%s",
                propertyReader.getProperty("app.id"), propertyReader.getProperty("app.activity")));
        LoggingUtils.info("Starting Activity: " + propertyReader.getProperty("app.id") + "/" + propertyReader.getProperty("app.activity"));
        driver.executeScript("mobile: startActivity", args);
    }

    public void clearApp() {
        LoggingUtils.info("Clearing app: " + propertyReader.getProperty("app.id"));
        driver.executeScript("mobile: clearApp", ImmutableMap.of("appId", propertyReader.getProperty("app.id")));
    }

    public void resetApp() {
        clearApp();
        startActivity();
    }

    public AndroidDriver getAndroidDriver() {
        if (driver == null) {
            initializeDriver();
        }
        return driver;
    }

    public WebDriverWait getWait() {
        if (wait == null) {
            initializeDriver();
        }
        return wait;
    }
}