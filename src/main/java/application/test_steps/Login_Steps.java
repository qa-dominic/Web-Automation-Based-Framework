package application.test_steps;

public class Login_Steps extends Base_Steps{
   
    public void clickLoginToKpx(){
        click(loginPageObjects.loginToKpx_Button2(), "Login to Kpx Button");
        System.out.println("I have clicked login to kpx button asdqweqwewqasd");
        waitSleep(1200);
        driver.navigate().refresh();
        waitSleep(1800);
        click(loginPageObjects.LOGINGOOGLE_button2(), "Google Login Btn");
        passTest("clickLoginToKpx", "This test has completed successfully");
    }
}