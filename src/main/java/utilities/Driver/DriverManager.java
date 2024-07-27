package utilities.Driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import utilities.Logger.LoggingUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Set;

public class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> WAIT = new ThreadLocal<>();
    private static final String SELENIUM_GRID_URL_PROPERTY = "gridURL";
    private static final Duration IMPLICIT_WAIT = Duration.ofSeconds(10);
    private static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(20);
    private static final Duration SCRIPT_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration EXPLICIT_WAIT_TIMEOUT = Duration.ofSeconds(15);;

    private DriverManager(){}

    public static void createDriver(final DriverType browser){
        WebDriver driver;
        switch (browser){
            case CHROME:
                driver = setupChrome();
                break;
            case REMOTE_CHROME:
                driver = setupRemoteChrome();
            break;
            case FIREFOX:
                driver = setupFirefox();
                break;
            default:
                LoggingUtils.error("Invalid Browser...");
                throw new IllegalArgumentException("Invalid browser type: " + browser);
        }
        if (driver == null) {
            throw new RuntimeException("Failed to initialize WebDriver");
        }
        setupBrowserTimeouts(driver);
        DRIVER.set(driver);
        WAIT.set(new WebDriverWait(driver, EXPLICIT_WAIT_TIMEOUT));
    }

    private static WebDriver setupChrome() {
        LoggingUtils.info("Setting up chrome driver...");
        try {
            WebDriverManager.chromedriver().clearDriverCache().setup();
            return new ChromeDriver(getChromeOptions());
        } catch (Exception e) {
            LoggingUtils.error("Error setting up Chrome: " + e.getMessage());
            throw new RuntimeException("Failed to set up Chrome driver", e);
        }
    }

    private static WebDriver setupRemoteChrome() {
        LoggingUtils.info("Setting up Remote Chrome Driver...");
        try {
            String seleniumGridUrl = System.getProperty(SELENIUM_GRID_URL_PROPERTY);
            if (seleniumGridUrl == null || seleniumGridUrl.trim().isEmpty()) {
                throw new IllegalStateException("Selenium Grid URL is not set. Please set the '" + SELENIUM_GRID_URL_PROPERTY + "' system property.");
            }
            return new RemoteWebDriver(new URL(seleniumGridUrl), getChromeOptions());
        } catch (MalformedURLException e) {
            LoggingUtils.error("Error setting up remote Chrome: " + e.getMessage());
            throw new RuntimeException("Failed to set up remote Chrome driver", e);
        }
    }
     private static WebDriver setupFirefox() {
        LoggingUtils.info("Setting up Firefox driver...");
        try {
            WebDriverManager.firefoxdriver().setup();
            return new FirefoxDriver(getFirefoxOptions());
        } catch (Exception e) {
            LoggingUtils.error("Error setting up Firefox: " + e.getMessage());
            throw new RuntimeException("Failed to set up Firefox driver", e);
        }
    }

    private static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.addArguments(
            "enable-automation",
            "--no-sandbox",
            "--disable-extensions",
            "--disable-infobars",
            "--disable-dev-shm-usage",
            "--disable-browser-side-navigation",
            "--disable-gpu",
            "--start-maximized",
            "--ignore-certificate-errors",
            "--disable-notifications",
            "--incognito",
            "use-fake-ui-for-media-stream"
        );
        return options;
    }

    private static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        // Add Firefox-specific arguments here
        return options;
    }

    //method for quitting driver
    public static void closeWebBrowser() {
        WebDriver driver = getDriver();
        if (driver != null) {
            String currentWindowHandle = driver.getWindowHandle();
            Set<String> windowHandles = driver.getWindowHandles();
            for (String windowHandle : windowHandles) {
                if (!windowHandle.equals(currentWindowHandle)) {
                    driver.switchTo().window(windowHandle);
                    LoggingUtils.info("Switch to window " + windowHandle);
                    driver.close();
                    LoggingUtils.info("Driver closed");
                }
            }
            driver.switchTo().window(currentWindowHandle);
            driver.quit();
            LoggingUtils.info("All windows closed and driver quit");
            DRIVER.remove();
            WAIT.remove();
        }
}

    private static void setDriver(WebDriver driver) {
        DRIVER.set(driver);
    }
   
    public static WebDriver getDriver () {
        return DRIVER.get ();
    }
    public static WebDriverWait getWait() {
        return WAIT.get();
    }

    //setup for timeouts
      private static void setupBrowserTimeouts(WebDriver driver) {
        LoggingUtils.info("Setting browser timeouts...");
        driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT);
        driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT);
        driver.manage().timeouts().scriptTimeout(SCRIPT_TIMEOUT);
    }

    
}