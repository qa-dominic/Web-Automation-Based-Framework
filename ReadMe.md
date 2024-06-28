### Prerequisites 
Before using the Automation Framework, make sure you have the following dependencies installed:

#### *Java
#### *Selenium
#### *Maven
#### *Test NG
#### *Extent Reporter
#### *Sl4j Logger


### Creating Test Scripts
To create a test script, follow these steps:

1. Obtain the necessary locators (id, cssSelector, className, xpath, etc,.) and place them in the src/main/java/application/page_objects package. Here's an example of how to create a locator:

        public WebElement googleContainer2() {
        return getDriver().findElement(By.cssSelector("[class='haAclf']"));
        }

        public WebElement googleContainer2() {
        return getDriver().findElement(By.xpath("//div[@class='haAclf']"));
        }

        public WebElement googleContainer2() {
        return getDriver().findElement(By.id("idName"));
        }
      
2. After creating the class for locators, proceed to create the test steps found at src/main/java/application/test_steps. This is where you define the steps required to complete a test case.

3. Once the test steps are created, create a BaseTest class. In this class, call the test steps so that they can be used across multiple test classes.

4. Finally, create your test classes and extend them from the BaseTest class. This will allow you to utilize the predefined test steps in your test cases.


### Test Execution
#### To execute your test scripts, run the Maven command mvn test in your project directory. This will trigger the Test NG framework to execute all the test cases defined in your project.
#### After executing the script, navigate to the Reports directory and view the results.


## Git commands
#### To pull changes from the repository
        git pull --rebase

#### To push changes from the repository
        git add .
        git commit -m "Your commit message"
        git push

#### To check out a branch
        git checkout <branch-name>

#### To pull changes from a branch
        git pull origin <branch-name>

#### To push changes to a branch
        git push origin <branch-name>