package application.test_steps;

import utilities.ReusableComponents.GeneralMethod;
import utilities.PropertyReader.propertyReader;
import application.page_objects.*;
import utilities.FileUtils.FilePathUtils;

public abstract class Base_Steps extends GeneralMethod {

    Login_PageObjects loginPageObjects = new Login_PageObjects();
    // Home_PageObjects  homePageObjects = new Home_PageObjects();
    final String filepath = "src\\test\\java\\resources\\testData.properties";
    final String imgName = "testImage.jpg";
    propertyReader propertyReader = new propertyReader(filepath);
    FilePathUtils filePathUtils = new FilePathUtils(imgName);
    
}
