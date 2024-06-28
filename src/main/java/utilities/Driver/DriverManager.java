package utilities.Driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import utilities.Logger.LoggingUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Set;

public class DriverManager {
    //thread local for web driver
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<> ();
    //private static final boolean useRemoteWebDriver = Boolean.getBoolean("remoteDriver");
    public static void createDriver(final DriverType browser){
        switch (browser){
            case CHROME:
                setupChrome();
                break;
            case REMOTE_CHROME:
                setupRemoteChrome();
                break;
            default:
                LoggingUtils.error("Invalid Browser...");
                break;
        }
        setupBrowserTimeouts();
    }
    public static void setupChrome(){
        LoggingUtils.info("Setting up chrome driver...");
        HashMap<String, Object> chromePreferences = new HashMap<>();
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless=new");
        options.addArguments("enable-automation");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("--disable-gpu");
        options.addArguments("--start-maximized");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-notifications");
        options.addArguments("--incognito");
        options.addArguments("use-fake-ui-for-media-stream");

        options.setExperimentalOption("prefs", chromePreferences);
        // Create desired capabilities
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
        // Set ChromeOptions as an argument while creating the WebDriver
        WebDriverManager.chromedriver().clearDriverCache().capabilities(desiredCapabilities).setup();

        setDriver(new ChromeDriver(options));
        LoggingUtils.info("Chrome Driver created successfully");
    }

    public static void setupRemoteChrome(){
        try {
            LoggingUtils.info ("Setting up Remote Chrome Driver....");
            final ChromeOptions options = new ChromeOptions ();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--start-maximized");
            options.addArguments("--ignore-certificate-errors");
            //options.setPlatformName("Windows 11");

            // Set the OS capabilities

//            options.addArguments("--incognito");
            String seleniumGridUrl = System.getProperty("gridURL");
            if (seleniumGridUrl == null || seleniumGridUrl.trim().isEmpty()) {
                throw new IllegalStateException("Selenium Grid URL is not set. Please set the 'seleniumGridURL' system property.");
            }
            setDriver(new RemoteWebDriver(new URL(seleniumGridUrl), options));
            LoggingUtils.info("Remote Chrome Driver created successfully!");
            } catch (MalformedURLException | IllegalStateException e) {
                LoggingUtils.error("Error setting up remote Chrome." + e);
            }
    }
    //method for quitting driver
public static void closeWebBrowser() {
    //waitTime(5000);
    WebDriver driver = getDriver();
    if (driver != null) {
        String currentWindowHandle = driver.getWindowHandle();
        Set<String> windowHandles = driver.getWindowHandles();
        // Close all windows except the current one
        for (String windowHandle : windowHandles) {
            if (!windowHandle.equals(currentWindowHandle)) {
                driver.switchTo().window(windowHandle);
                LoggingUtils.info("Switch to window " + windowHandle);
                driver.close();
                LoggingUtils.info("Driver closed");
            }
        }
        // Switch back to the current window and close it
        driver.switchTo().window(currentWindowHandle);
        driver.quit();
    }
}

    //method for setting up driver
    private static void setDriver (WebDriver driver){
        DriverManager.DRIVER.set(driver);
    }

    //method for getting driver
    public static WebDriver getDriver () {
        return DriverManager.DRIVER.get ();
    }

    //setup for timeouts
    private static void setupBrowserTimeouts(){
        LoggingUtils.info("Setting browser timeouts...");
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        getDriver().manage().timeouts().pageLoadTimeout (Duration.ofSeconds (20));
        getDriver().manage().timeouts().scriptTimeout (Duration.ofSeconds (10));
    }

    private DriverManager(){}
}
