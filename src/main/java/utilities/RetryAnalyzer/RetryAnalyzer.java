package utilities.RetryAnalyzer;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCnt = 1;

    //This method will be called everytime a test fails. It will return TRUE if a test fails and need to be retried, else it returns FALSE
    @Override
    public boolean retry(ITestResult result) {
        if (!result.isSuccess()) {
            System.out.println("RetryFailedTestCases");
            //You could mention maxRetryCnt (Max Retry Count) as per your requirement. Here I took 2, If any failed testcases then it runs two times
            int maxRetryCnt = 3;
            if (retryCnt < maxRetryCnt) {
                System.out.println("Retrying " + result.getName() + " again and the count is " + (retryCnt + 1));
                retryCnt++;
                return true;
            }
        }
        return false;
    }
}