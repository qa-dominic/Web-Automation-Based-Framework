package utilities.ReusableComponents;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.Random;
import java.util.Set;
import java.util.List;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.ExtentReport.ExtentReporter;
import utilities.Logger.LoggingUtils;
import utilities.yamlReader.yamlReader;
import org.testng.Assert;

import static utilities.Driver.DriverManager.getDriver;

public class GeneralMethod extends ExtentReporter{
    protected final WebDriver driver = getDriver();
    private final WebDriverWait wait;
    public final yamlReader reader = new yamlReader();
    private JavascriptExecutor js;
    protected final Actions actions = new Actions(driver);

    public GeneralMethod(){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }
    
    public void click(WebElement locator, String elementName){
        try {
            if (isDisplayed(locator)) {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
                LoggingUtils.info("Clicked on element: " + elementName);
                ExtentReporter.logInfo("Clicked on element: " + elementName, "");
                actions.click(element).perform();          
            }
        } catch (StaleElementReferenceException e){
            LoggingUtils.info("Stale Clicked on element: " + elementName + ":::: due to " + e);
            ExtentReporter.logInfo("Stale Clicked on element: " + elementName, "" + e);
            actions.click(locator).perform();
        }catch (TimeoutException | NoSuchElementException e) {
            ExtentReporter.logFail("Failed to click element: " + elementName, "Caused: " + e);
            LoggingUtils.error("Failed to click element: " + elementName + "Caused" + e);
            throw new AssertionError("Failed to click element: " + elementName, e);
        } catch (Exception e) {
            ExtentReporter.logFail("Failed to click element: " + elementName, "Caused: " + e);
            LoggingUtils.error("Failed to click element: " + elementName + "Caused"  + e);
            throw new AssertionError("Failed to click element: " + elementName);
        }
    }

    public void type(WebElement locator, String elementName, String text){
        try {
            if(isDisplayed(locator)) {
                WebElement element = wait.until(ExpectedConditions.visibilityOf(locator));
                LoggingUtils.info("Typed into field: " + elementName + ", Value: "+ text);
                ExtentReporter.logInfo("Typed into field: " + elementName , "Typed Value: "+ text);
                element.clear();
                actions.sendKeys(element, text)
                .perform();
            }
        } catch (NoSuchElementException e) {
        LoggingUtils.error("Failed to type into field: "+ elementName + ", Value: "+ text);
        ExtentReporter.logFail("Failed to type into field: "+ elementName , " Typed Value:: "+ text);
        throw new AssertionError("Failed to type into field: "+ elementName + ", Value: "+ text);
        }
    }

    public void typeEnter(WebElement locator, String elementName, String text){
        try {
            if(isDisplayed(locator)) {
                WebElement element = wait.until(ExpectedConditions.visibilityOf(locator));
                LoggingUtils.info("Typed into field: " + elementName + ", Value: "+ text);
                ExtentReporter.logInfo("Typed into field: " + elementName , "Typed Value: "+ text);
                element.sendKeys(text + Keys.ENTER);           
            }
        } catch (NoSuchElementException e) {
            LoggingUtils.error("Failed to type into field: "+ elementName + ", Value: "+ text);
            ExtentReporter.logFail("Failed to type into field: "+ elementName , "Typed Value: "+ text);
            throw new AssertionError("Failed to type into field: "+ elementName + ", Value: "+ text);
        }
    }

    public boolean isVisible(WebElement locator, String elementName){
        try{
            wait.until(ExpectedConditions.visibilityOf(locator));
            LoggingUtils.info("Element: " + elementName + ", is visible");
            return true;
        }catch (NoSuchElementException e){
            ExtentReporter.logFail("Element: " + elementName + "not visible", "Caused: ");
            LoggingUtils.error("Element: " + elementName + "not visible");
            throw new AssertionError("Element: " + elementName + " not visible" );
        } catch (Exception e){
            LoggingUtils.error(e);
            ExtentReporter.logFail("err", String.valueOf(e));
            throw new AssertionError(e );
        }
    }

    /**
     * boolean return type for conditions
     *
     * @param locator
     * @return
     * @throws NoSuchElementException
     */
    public boolean isDisplayed(WebElement locator) {
        try {
            wait.until(ExpectedConditions.visibilityOf(locator));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        } finally {

        }
    }
    public String getText(WebElement locator){
        String val = null;
        try{
            WebElement element = wait.until(ExpectedConditions.visibilityOf(locator));
            val = element.getText();
        }catch(Exception e){
            ExtentReporter.logFail("Cannot get text for element" + e.getMessage(), "Caused: "+ e);
            LoggingUtils.error("Cannot get text for element" + e.getMessage());
            throw new AssertionError("Cannot get text for element" + e.getMessage());
        }
       return val;

    }

    public void assertEqual(String actual, String expected){
        try{
            Assert.assertEquals(actual, expected);
            LoggingUtils.info(actual +  " and " + expected + " are matched");
            ExtentReporter.logInfo("Assertion: "+actual +  " and " + expected + " are matched" , "asserted values " + actual + " and " + expected);
        }catch(Exception e){
            LoggingUtils.error("Assertion error: "+ e.getMessage());
            ExtentReporter.logFail("Assertion error: "+ e.getMessage(), "Caused: "+ e);
            throw new AssertionError("Assertion error: "+ e.getMessage());
        }
    }

    public void waitImplicitly(int seconds){
        try{
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
            LoggingUtils.info("Waiting implicitly for: " + seconds + " seconds");
        }catch(Exception e){
            LoggingUtils.error("waitImplicitly error: "+ e.getMessage());
            throw new AssertionError("Assertion error: "+ e.getMessage());
        }
    }
    public void waitSleep(int miliSeconds){
        try{
            Thread.sleep(miliSeconds);
            LoggingUtils.info("Waiting for: " + miliSeconds + " miliSeconds");
        }catch(Exception e){
            LoggingUtils.error("wait error: "+ e.getMessage());
            throw new AssertionError("Assertion error: "+ e.getMessage());
        }
    }

    public void switchToNextTab() {
        String currentWindowHandle = getDriver().getWindowHandle();
        Set<String> windowHandles = getDriver().getWindowHandles();

        if (windowHandles.size() <= 1) {
            LoggingUtils.info("No next tab/window to switch to or close");
            ExtentReporter.logInfo("No Tab to Switch", "No Tab to Switch");
            return;
        }

        for (String windowHandle : windowHandles) {
            if (!windowHandle.equals(currentWindowHandle)) {
                getDriver().switchTo().window(windowHandle);
                LoggingUtils.info("Switch Tab " + currentWindowHandle);
                ExtentReporter.logInfo("Switch Next Tab" , "Window ID: "+currentWindowHandle);
                break;
            }
        }
    }
    public void switchToNextTabClose() {
        String currentWindowHandle = getDriver().getWindowHandle();
        Set<String> windowHandles = getDriver().getWindowHandles();

        // Check if there are multiple windows/tabs open
        if (windowHandles.size() <= 1) {
            LoggingUtils.info("No next tab/window to switch to or close");
            ExtentReporter.logInfo("No Tab to Switch", "No Tab to Switch");
            return;
        }

        boolean foundNextWindow = false;

        for (String windowHandle : windowHandles) {
            if (!windowHandle.equals(currentWindowHandle)) {
                try {
                    getDriver().switchTo().window(windowHandle);
                    getDriver().close();
                    LoggingUtils.info("Closed window: " + windowHandle);
                    ExtentReporter.logInfo("Closed window: " + windowHandle, "");
                    foundNextWindow = true;
                } catch (NoSuchWindowException e) {
                    LoggingUtils.error("Failed to switch to/close window: " + windowHandle);
                    ExtentReporter.logFail("Failed to switch to/close window: " + windowHandle, "");
                }
                break;
            }
        }

        if (!foundNextWindow) {
            LoggingUtils.error("No next tab/window found to switch to or close");
            ExtentReporter.logFail("No next tab/window found to switch to or close", "");
            throw new AssertionError("Assertion error: ");
        }

        // Switch driver focus back to the original window/tab
        getDriver().switchTo().window(currentWindowHandle);
    }

    public void switchToPreviousTab() {
        String currentWindowHandle = getWebDriver().getWindowHandle();
        Set<String> windowHandles = getWebDriver().getWindowHandles();
        String previousWindowHandle = null;
        getWebDriver().close();
        for (String windowHandle : windowHandles) {
            if (!windowHandle.equals(currentWindowHandle)) {
                previousWindowHandle = windowHandle;
            }
        }

        if (previousWindowHandle != null) {
            getWebDriver().switchTo().window(previousWindowHandle);
            LoggingUtils.info("Switch to " + previousWindowHandle);
            ExtentReporter.logInfo("Switch Previous Tab " ,"Previous Tab ID: "+ currentWindowHandle);
        } else {
            throw new IllegalStateException("No previous tab found");
            
        }
    }
    public void scrollToBottomOfPageWEB() {
        try{
            js = (JavascriptExecutor) getWebDriver();
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        }catch (Exception e){
            LoggingUtils.info("Error: "+e);
            throw new AssertionError("Assertion error: "+ e.getMessage());
        }

    }

    public  void scrollToTopOfPageWEB() {
        try{
            js= (JavascriptExecutor) getWebDriver();
            js.executeScript("window.scrollBy(0,-250)", "");
        }catch (Exception e ){
            LoggingUtils.info("Error: "+e);
            throw new AssertionError("Assertion error: "+ e.getMessage());
        }
    }
    public void scrollToSpecificScrollPosition(int scrollPosition) {
        try{
            js = (JavascriptExecutor) getWebDriver();
            js.executeScript("window.scrollTo(0, arguments[0])", scrollPosition);
            LoggingUtils.info("Scrolling into position: "+ scrollPosition);
        }catch (Exception e){
            LoggingUtils.info("Error "+ e);
            throw new AssertionError("Assertion error: "+ e.getMessage());
        }

    }
    public void scrollToElement(WebElement element) {
        try{
            actions.scrollToElement(element).perform();
            LoggingUtils.info("Scrolling into element: "+ element.getText());
            ExtentReporter.logInfo("Scrolling into element: ", "");
        }catch (Exception e){
            LoggingUtils.info("Error "+ e);
            throw new AssertionError("Assertion error: "+ e.getMessage());
        }
    }
    public List<WebElement> staleException_Click(WebElement locator) {
        List<WebElement> outcome = null;
        int repeat = 0;
        while (repeat <= 6) {
            try {
                List<WebElement> ele = getDriver().findElements((By) locator);
                break;
            } catch (StaleElementReferenceException e) {
                e.printStackTrace();
            }
            repeat++;
        }
        return outcome;
    }
    public void uploadFile(WebElement locator, String filename){
        try{
            ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('hidden');", locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.display = 'block';", locator);
            locator.sendKeys(filename);
            LoggingUtils.info("Uploading file: " + filename);
            ExtentReporter.logInfo("Uploading file: " + filename, "");
        }catch(Exception e){
            ExtentReporter.logFail("Error in Upload File: ", ""+ e);
            throw new AssertionError("Assertion error: "+ e.getMessage());
        }
    }
    public void failTest(String testName, String message){
        ExtentReporter.logFail(testName, message);
        Assert.fail(testName +": "+ message);
    }

    public void passTest(String testName, String message){
        ExtentReporter.logPass(testName, message);
        LoggingUtils.info(testName +": "+ message);
    }
    public void arrowKeyDown(int option){
        try{
            LoggingUtils.info("Arrow key down option: " + option);
            Actions action = new Actions(driver);
            for(int i=0; i< option; i++){
                LoggingUtils.info("Performing Arrow Key Down" + i);
                action.keyDown(Keys.SHIFT).sendKeys(Keys.ENTER)
                .perform();
            }
        }catch(Exception e){
            throw new AssertionError("Assertion error: "+ e.getMessage());
        }
    }
    public void arrowKeyDownv2(int option){
        Actions action = new Actions(driver);
        try{
            LoggingUtils.info("Arrow key down option: " + option);
            for(int i=0; i< option; i++){
                action.keyDown(Keys.SHIFT)
                        .perform();
            }
        }catch(Exception e){
            throw new AssertionError("Assertion error: "+ e.getMessage());
        }
        finally {
            LoggingUtils.info("Entering Selected Value ");
            action.sendKeys(Keys.ENTER).perform();
        }
    }
    public void arrowKeyUp(int option){
        try{
            LoggingUtils.info("Arrow key up option: " + option);
            Actions action = new Actions(driver);
            for(int i=0; i< option; i++){
                action.keyUp(Keys.SHIFT).sendKeys(Keys.ENTER)
                .perform();
            }
        }catch(Exception e){
            throw new AssertionError("Assertion error: "+ e.getMessage());
        }
    }
    public void navigateBack(){
        try{
            driver.navigate().back();
            LoggingUtils.info("Navigating back");
            ExtentReporter.logInfo("Navigating Back" , "Previous Page");
        }catch (Exception e){
            throw new AssertionError("Cannot get value for element" + e.getMessage());
        }
    }
    public void selectByVisibleText(WebElement locator, String value){
        Select select = new Select(locator);
        try{
            wait.until(ExpectedConditions.visibilityOf(locator));
            select.selectByVisibleText(value);
            LoggingUtils.info("Selected Value: " + value);
            ExtentReporter.logInfo("Selected Value: "+ value + " from " + locator , "");
        }catch (Exception e){
            throw new AssertionError("No Selected Value" + e.getMessage());
        }
    }
    public void selectByValue(WebElement locator, String value){
        Select select = new Select(locator);
        try{
            wait.until(ExpectedConditions.visibilityOf(locator));
            select.selectByValue(value);
            LoggingUtils.info("Selected Value: " + value);
            ExtentReporter.logInfo("Selected Value: "+ value + " from " + locator , "");
        }catch (Exception e){
            throw new AssertionError("No Selected Value" + e.getMessage());
        }
    }
    public void selectByIndex(WebElement locator, int index){
        Select select = new Select(locator);
        try{
            wait.until(ExpectedConditions.visibilityOf(locator));
            select.selectByIndex(index);
            LoggingUtils.info("Selected Index: " + index);
            ExtentReporter.logInfo("Selected Index: "+ index + " from " + locator , "");
        }catch (Exception e){
            throw new AssertionError("No Selected Index" + e.getMessage());
        }
    }
    public static int getRandomNumber() {
        Random random = new Random();
        return random.nextInt(5) + 1; //random 1 to 5
    }
    public static int getThreeDigitRandomNumber() {
        Random random = new Random();
        return random.nextInt(152) + 1;
    }
    public static int getFiveDigitsRandomNumber() {
        Random random = new Random();
        return random.nextInt(999999) + 99999;
    }
    public String getValue(WebElement locator){
        String val = null;
        try{
            WebElement element = wait.until(ExpectedConditions.visibilityOf(locator));
            val = element.getAttribute("value");
        }catch(Exception e){
            ExtentReporter.logFail("Cannot get text for element" + e.getMessage(), "Caused: "+ e);
            LoggingUtils.error("Cannot get text for element" + e.getMessage());
            throw new AssertionError("Cannot get text for element" + e.getMessage());
        }
        return val;
    }

    public void assertNotEqual(String actual, String expected){
        try{
            Assert.assertNotEquals(actual, expected);
            LoggingUtils.info(actual +  " and " + expected + " are not matched");
            ExtentReporter.logInfo("Assertion: "+actual +  " and " + expected + " are not matched" , "asserted values " + actual + " and " + expected);
        }catch(Exception e){
            LoggingUtils.error("Assertion error: "+ e.getMessage());
            ExtentReporter.logFail("Assertion error: "+ e.getMessage(), "Caused: "+ e);
            throw new AssertionError("Assertion error: "+ e.getMessage());
        }
    }
    public void typeActiveElement(String text){
        try{
            actions.
            sendKeys(text).
            perform();
        }finally{
            LoggingUtils.info("Entering text: " + text);
            ExtentReporter.logInfo("Entering text: " + text, "");
        }
    }
    public void assertEqual(double actual, double expected, double delta){
        try{
            Assert.assertEquals(actual, expected, delta);
            LoggingUtils.info(actual +  " and " + expected + " are matched");
            ExtentReporter.logInfo("Assertion: "+actual +  " and " + expected + " are matched" , "asserted values " + actual + " and " + expected);
        }catch(Exception e){
            LoggingUtils.error("Assertion error: "+ e.getMessage());
            ExtentReporter.logFail("Assertion error: "+ e.getMessage(), "Caused: "+ e);
            throw new AssertionError("Assertion error: "+ e.getMessage());
        }
    }
    public void switchFrame(WebElement element) {
        try{
            driver.switchTo().frame(element);
            LoggingUtils.info("Switch frame: "+ element.getText());
            ExtentReporter.logInfo("Switch frame: ", "");
        }catch (Exception e){
            LoggingUtils.info("Error "+ e);
            throw new AssertionError("Assertion error: "+ e.getMessage());
        }
    }
}
