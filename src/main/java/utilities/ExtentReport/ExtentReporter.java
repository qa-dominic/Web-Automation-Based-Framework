package utilities.ExtentReport;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
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

import static utilities.Driver.DriverManager.getDriver;
public class ExtentReporter implements ITestListener {

    private static final ExtentReports extent = new ExtentReports();
    private static final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();
    private static final String SCREENSHOTS_DIRECTORY = "Screenshots";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public void onStart(ITestContext context){
        String projectDirectory = System.getProperty("user.dir");
        String reportsDirectory = projectDirectory + "/Reports";
        String suiteName = context.getSuite().getName();
        String testName = context.getCurrentXmlTest().getName();
        String currentDate = LocalDate.now().format(DATE_FORMATTER);
        String reportFileName = currentDate+"/"+suiteName+"/"+testName+".html";
        ExtentSparkReporter spark = new ExtentSparkReporter(reportsDirectory + "/" + reportFileName);
        extent.attachReporter(spark);
    }

    public static WebDriver getWebDriver() {
        return getDriver();
    }

    public static ExtentReports getExtentReports() {
        return extent;
    }

    public static void logPass(String step,String message) {
        logStatusWithScreenshot(extentTestThreadLocal.get(),step, message, "pass");
    }

    public static void logFail(String step,String message) {
        logStatusWithScreenshot(extentTestThreadLocal.get(),step, message, "fail");
    }

    public static void logInfo(String step, String message){
        logStatusWithScreenshot(extentTestThreadLocal.get(),step ,message, "info");
    }

    public static void logSkip(String step, String message){
        logStatusWithScreenshot(extentTestThreadLocal.get(),step ,message, "info");
    }

    private static void logStatusWithScreenshot(ExtentTest extentTest,String step ,String message, String logType) {
        ExtentTest test = extentTestThreadLocal.get();
        String screenshotPath = captureScreenshot(extentTest.getModel().getName());
        switch (logType.toLowerCase()) {
            case "pass":
                test.createNode(step).addScreenCaptureFromPath(screenshotPath).pass(message);
                break;
            case "fail":
                test.createNode(step).addScreenCaptureFromPath(screenshotPath).fail(message);
                break;
            case "info":
                test.createNode(step).addScreenCaptureFromPath(screenshotPath).info(message);
                break;
            default:
                test.createNode(step).addScreenCaptureFromPath(screenshotPath).skip(message);
                break;
        }
    }

    private static String captureScreenshot(String testName) {
        try {
            TakesScreenshot screenshotDriver = (TakesScreenshot) getWebDriver();
            File src = screenshotDriver.getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
            String screenshotPath = getScreenshotDirectoryPath() + File.separator + testName + "_" + timestamp + ".png";
            File screenshotFile = new File(screenshotPath);
            FileUtils.copyFile(src, screenshotFile);       
            return screenshotFile.getAbsolutePath();
        } catch (IOException e) {
            LoggingUtils.error("Failed to capture screenshot: " + e.getMessage());
            return null;
        } 
    }

    private static String getScreenshotDirectoryPath() {
        String projectDirectory = System.getProperty("user.dir");
        String currentDate = LocalDate.now().format(DATE_FORMATTER);
        String screenshotsDirectory = projectDirectory + File.separator + SCREENSHOTS_DIRECTORY + File.separator + currentDate;
        createDirectory(screenshotsDirectory);
        return screenshotsDirectory;
    }

    private static void createDirectory(String directoryPath) {
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                LoggingUtils.error("Failed to create directory: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestStart(ITestResult result){
        try{
            ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName(), result.getMethod().getDescription());
            extentTestThreadLocal.set(extentTest);
            logTestBoundary("Started", result.getName());
        }catch (Exception e){
            LoggingUtils.error(e.getMessage());
        }
    }

    @Override
    public void onFinish(ITestContext context){
        extent.flush();
    }
    
    @Override
    public void onTestFailure(ITestResult result){
        if(getDriver() != null){
            logFail(result.getName(), result.getThrowable().getMessage());
            logTestBoundary("FAILED", result.getName());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result){
        extentTestThreadLocal.get().log(Status.SKIP, result.getName() + " has been Skipped");
        logSkip(result.getName(), result.getThrowable().getMessage());
        logTestBoundary("Skipped", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result){
        extentTestThreadLocal.get().log(Status.PASS, result.getName() + " Passed");
        logTestBoundary("Passed", result.getName());
    }

    private void logTestBoundary(String status, String testName) {
        LoggingUtils.info("//**::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::://**");
        LoggingUtils.info("\t\t//:::::::::::::Test: " + testName + " " + status + " ::::::::::://");
        LoggingUtils.info("//**::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::://**");
    }

}