package application.test_steps;
// package mlshopbuilder.testSteps;

// import org.checkerframework.checker.units.qual.t;
// import org.openqa.selenium.By;
// import org.openqa.selenium.NoSuchElementException;
// import org.openqa.selenium.WebElement;
// import org.openqa.selenium.support.ui.ExpectedConditions;
// import org.openqa.selenium.support.ui.WebDriverWait;

// import java.time.Duration;

// public class Login_Steps extends Base_Steps{
//     String loginUrl = "https://mlshoppreprod.mlhuillier.com/authlogin";
//     String registerUrl = "https://mlshoppreprod.mlhuillier.com/register";

//    public void login(){
//     try{
//         navigateLoginPage();
//         waitSleep(2000);
//         assertEqual(driver.getCurrentUrl(), loginUrl);
//         typeEnter(loginPageObjects.mobileNumber_field(), "Mobile Number Field", reader.getRandomUser());
//         waitSleep(5000);
//         if(isVisible(loginPageObjects.otpMessage(), getText(loginPageObjects.otpMessage()))){
//             inputOTP();
//             click(loginPageObjects.okay_Btn(), "Okay Button");
//             waitSleep(1000);
//             assertEqual(driver.getCurrentUrl(), System.getProperty("targetUrl"));
//             passTest("LOGIN SUCESS", "");
//         }else{
//             failTest("FAILED TO LOGIN", "");
//         }
//     }catch(Exception e){
//         failTest("FAILED TO LOGIN", "Due to :" + e);
//     }
      
//    }
//    public void inputOTP(){
//        for(int i = 1; i <= 6 ; i++){
//         typeActiveElement(String.valueOf(i));
//        }
//    }

//    public void navigateLoginPage(){
//     click(home_PageObjects.userIcon(), "User Icon"); 
//    }
// }
