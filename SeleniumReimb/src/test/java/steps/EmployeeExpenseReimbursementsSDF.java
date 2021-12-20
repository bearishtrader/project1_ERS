package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import pom.EmployeeExpenseReimbursementsPOM;
import pom.LoginPOM;

public class EmployeeExpenseReimbursementsSDF {
    WebDriver driver;
    EmployeeExpenseReimbursementsPOM reimbPom;
    String domain = "http://localhost:9000/";
    WebElement reimbNewlyCreated;
    @Before
    public void setup() throws InterruptedException {
        //System.setProperty("webdriver.chrome.driver", "C:\\tools\\selenium\\chromedriver.exe");
        System.setProperty("webdriver.gecko.driver", "C:\\tools\\selenium\\geckodriver.exe");
        //this.driver = new ChromeDriver();
        this.driver = new FirefoxDriver();
        Thread.sleep(7000);
        driver.get(domain);
        reimbPom = new EmployeeExpenseReimbursementsPOM(driver);
    }

    @After
    public void teardown(){
        driver.quit();
    }

    @Given("Employee has logged in and is on the Employee Expense Reimbursements dashboard")
    public void employee_has_logged_in_and_is_on_the_employee_expense_reimbursements_dashboard() {
        Assertions.assertTrue(reimbPom.login("jsmith@javadev.com", "jsmith", "1234"));
    }
    @When("Employee completes and submits Create Expense Reimbursement Request form")
    public void employee_completes_and_submits_create_expense_reimbursement_request_form() {
        reimbNewlyCreated = this.reimbPom.createExpenseReimbursementRequest("Super 8 Motel #1", 375.97, 1/* LODGING*/);
    }

    @Then("A new expense reimbursement request will appear in list")
    public void a_new_expense_reimbursement_request_will_appear_in_list() {
        Assertions.assertNotNull(reimbNewlyCreated);
    }

    /*@Then("The description will match what was input")
    public void the_description_will_match_what_was_input() {
        // Write code here that turns the phrase above into concrete actions
        Assertions.assertEquals("Super 8 Motel #1", reimbNewlyCreated.findElement(By.className("reimb-description")).getText());
    }

    @Then("The amount will match what was input")
    public void the_amount_will_match_what_was_input() {
        Assertions.assertEquals("$"+String.valueOf(375.97), reimbNewlyCreated.findElement(By.className("reimb-amount")).getText());
    }

    @Then("The type will match what was input")
    public void the_type_will_match_what_was_input() {
        Assertions.assertEquals("LODGING", reimbNewlyCreated.findElement(By.className("reimb-type")).getText());
    }

    @Then("The status will be Pending")
    public void the_status_will_be_pending() {
        Assertions.assertEquals("Pending", reimbNewlyCreated.findElement(By.className("reimb-status")).getText());
    }
*/
}
