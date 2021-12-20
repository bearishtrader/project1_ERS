package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import pom.LoginPOM;

public class LoginSDF {
    WebDriver driver;
    LoginPOM loginPom;
    String domain = "http://localhost:9000/";

    @Before
    public void setup() throws InterruptedException {
        //System.setProperty("webdriver.chrome.driver", "C:\\tools\\selenium\\chromedriver.exe");
        System.setProperty("webdriver.gecko.driver", "C:\\tools\\selenium\\geckodriver.exe");
        //this.driver = new ChromeDriver();
        this.driver = new FirefoxDriver();
        Thread.sleep(5000);
        driver.get(domain);

        loginPom = new LoginPOM(driver);
    }

    @After
    public void teardown(){
        driver.quit();
    }

    @Given("Employee is on login page")
    public void employee_is_on_login_page() {
        Assertions.assertEquals(domain, driver.getCurrentUrl());
    }
    @When("Employee enters email username and password")
    public void employee_enters_email_username_and_password() {
        // Write code here that turns the phrase above into concrete actions
        Assertions.assertTrue(loginPom.login("jsmith@javadev.com", "jsmith", "1234"));
    }
    @Then("Employee is now on Employee Expense Reimbursements dashboard")
    public void employee_is_now_on_employee_expense_reimbursements_dashboard() {
        String expectedResult =domain+"employee-dashboard/";
        Assertions.assertEquals(expectedResult, driver.getCurrentUrl());
    }
}
