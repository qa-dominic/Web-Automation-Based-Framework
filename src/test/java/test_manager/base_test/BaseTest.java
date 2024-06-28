package test_manager.base_test;

import org.testng.annotations.*;

import application.test_steps.*;

import static utilities.Driver.DriverManager.*;
import utilities.Driver.DriverType;
import utilities.Logger.LoggingUtils;

public class BaseTest {
    // protected Login_Steps loginSteps;
    // protected Home_Steps homeSteps;
    // protected Shipping_Steps shippingSteps;
    // protected Cart_Steps cartSteps;


    @Parameters("browser")
    @BeforeClass (alwaysRun = true)
    public void setUp(final String browser) throws InterruptedException {
        if (browser == null || browser.isEmpty()) {
            throw new IllegalArgumentException("Browser parameter cannot be null or empty");
        }
        LoggingUtils.info("base test browser type: "+browser.toUpperCase());
//        initializeDriver(DriverType.valueOf("REMOTE_CHROME"));
        initializeDriver(DriverType.valueOf(browser.toUpperCase()));
        getDriver().manage().deleteAllCookies();
        Thread.sleep(3000);
        getDriver().get(System.getProperty("targetUrl"));
        LoggingUtils.info("Redirecting back to home");
        // loginSteps = new Login_Steps();
        // homeSteps = new Home_Steps();
        // shippingSteps = new Shipping_Steps();
        // cartSteps = new Cart_Steps();
    }
    private void initializeDriver(DriverType driverType) {
        createDriver(driverType);
    }


    @BeforeMethod(alwaysRun = true)
    public void setUpTests(){
        getDriver().get(System.getProperty("targetUrl"));
    }

    @AfterMethod(alwaysRun = true)
    public void clean(){
        getDriver().manage().deleteAllCookies();
    }
    @AfterClass(alwaysRun = true)
    public void tearDown () {
        closeWebBrowser();
    }

}