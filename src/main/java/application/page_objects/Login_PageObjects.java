package application.page_objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import static utilities.Driver.DriverManager.getDriver;

import java.util.List;

public class Login_PageObjects {

    
    public WebElement login_Btn(){
        return getDriver().findElement(By.cssSelector("[type='submit']"));
    }
    public WebElement mobileNumber_field(){
        return getDriver().findElement(By.id("OTP"));
    }
    public WebElement verifyMobile_Text(){
        return getDriver().findElement(By.xpath("//h3[normalize-space()='Please verify your mobile number']"));
    }

    public WebElement loginToKpx_Button(){
        return getDriver().findElement(By.className("btn d-block w-100 text-light"));
    }

    public WebElement loginToKpx_Button2(){
        return getDriver().findElement(By.cssSelector("[method='post']"));
    }
    public WebElement LOGINGOOGLE_button2(){
        return getDriver().findElement(By.cssSelector("[role='button']"));
    }
}