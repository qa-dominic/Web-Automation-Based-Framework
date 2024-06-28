package utilities.ExtentReport;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;

import org.testng.ITestResult;
import utilities.Logger.LoggingUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static utilities.Driver.DriverManager.getDriver;
public class ExtentReporter implements ITestListener {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();
    private static final String SCREENSHOTS_DIRECTORY = "Screenshots";
    private static File src;

    public void onStart(ITestContext context){
        extent = new ExtentReports();
        String projectDirectory = System.getProperty("user.dir");
        String reportsDirectory = projectDirectory + "/Reports";
        String suiteName = context.getSuite().getName();
        String testName = context.getCurrentXmlTest().getName();
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        String reportFileName = currentDate+"/"+suiteName+"/"+testName+".html";
        ExtentSparkReporter spark = new ExtentSparkReporter(reportsDirectory + "/" + reportFileName);
        extent.attachReporter(spark);
    }

    public static synchronized WebDriver getWebDriver() {
        return getDriver();
    }

    public static synchronized ExtentReports getExtentReports() {
        return extent;
    }

    public static synchronized void logPass(String step,String message) {
        logStatusWithScreenshot(extentTestThreadLocal.get(),step, message, "pass");
    }

    public static synchronized void logFail(String step,String message) {
        logStatusWithScreenshot(extentTestThreadLocal.get(),step, message, "fail");

    }
    public static synchronized void logInfo(String step, String message){
        logStatusWithScreenshot(extentTestThreadLocal.get(),step ,message, "info");
    }
    private static synchronized void logStatusWithScreenshot(ExtentTest extentTest,String step ,String message, String logType) {
        String screenshotPath = captureScreenshot(extentTest.getModel().getName());

        switch (logType.toLowerCase()) {
            case "pass":
                extentTest.createNode(step).addScreenCaptureFromPath(screenshotPath).pass(message);
                break;
            case "fail":
                extentTest.createNode(step).addScreenCaptureFromPath(screenshotPath).fail(message);
                break;
            case "info":
                extentTest.createNode(step).addScreenCaptureFromPath(screenshotPath).info(message);
                break;
            default:
                break;
        }
    }
    private static synchronized String captureScreenshot(String testName) {
        try {
            TakesScreenshot screenshotDriver = (TakesScreenshot) getWebDriver();
            File src = screenshotDriver.getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String screenshotPath = getScreenshotDirectoryPath() + File.separator + testName + "_" + timestamp + ".png";
            File screenshotFile = new File(screenshotPath);
            FileUtils.copyFile(src, screenshotFile);
            //LoggingUtils.info("Screenshot captured: " + screenshotFile.getAbsolutePath());
            return screenshotFile.getAbsolutePath();
        } catch (IOException e) {
            LoggingUtils.error(e.getMessage());
        }
        return null;
    }
    private static synchronized String getScreenshotDirectoryPath() {
        String projectDirectory = System.getProperty("user.dir");
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        String screenshotsDirectory = projectDirectory + File.separator + SCREENSHOTS_DIRECTORY + File.separator + currentDate;
        createDirectory(screenshotsDirectory);
        return screenshotsDirectory;
    }
    private static synchronized void createDirectory(String directoryPath) {
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                LoggingUtils.error(e.getMessage());
            }
        }
    }

    @Override
    public synchronized void onTestStart(ITestResult result){
        try{
            ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName(), result.getMethod().getDescription());
            extentTestThreadLocal.set(extentTest);
            LoggingUtils.info("//**::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::://**");
            LoggingUtils.info("\t\t//:::::::::::::Test: "+ result.getName() + " Started ::::::::::://");
            LoggingUtils.info("//**::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::://**");
        }catch (Exception e){
            LoggingUtils.error(e.getMessage());
        }
    }
    @Override
    public synchronized void onFinish(ITestContext context){
        extent.flush();
    }
    
    @Override
    public synchronized void onTestFailure(ITestResult result){
        if(getDriver() != null){
             logFail(result.getName(),result.getThrowable().getMessage());
            //extentTestThreadLocal.get().log(Status.FAIL, result.getName()+ " is Failed " + result.getThrowable().getLocalizedMessage());
            LoggingUtils.info("//**::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::://**");
            LoggingUtils.info("\t\t//:::::::::::::Test: "+ result.getName() + " FAILED ::::::::::://");
            LoggingUtils.info("//**::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::://**");
        }
    }
    @Override
    public synchronized void onTestSkipped(ITestResult result){
        extentTestThreadLocal.get().log(Status.SKIP, result.getName()+ " has been Skipped");
        LoggingUtils.info("//**::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::://**");
        LoggingUtils.info("\t\t//:::::::::::::Test: "+ result.getName() + " Skipped ::::::::::://");
        LoggingUtils.info("//**::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::://**");
    }
    @Override
    public synchronized void onTestSuccess(ITestResult result){
        extentTestThreadLocal.get().log(Status.PASS, result.getName()+ " Passed");
        LoggingUtils.info("//**::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::://**");
        LoggingUtils.info("\t\t//:::::::::::::Test: "+ result.getName() + " Passed ::::::::::://");
        LoggingUtils.info("//**::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::://**");
    }

}