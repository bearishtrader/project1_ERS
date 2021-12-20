package pom;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class EmployeeExpenseReimbursementsPOM {
    WebDriver driver;
    Wait<WebDriver> wait;

    WebElement emailElem;
    WebElement usernameElem;
    WebElement passwordElem;
    WebElement loginButtonElem;

    WebElement descriptionElem;
    WebElement amountElem;
    WebElement typeDropdownElem;
    WebElement submitBtnElem;

    By reimbListSelector = By.className("reimb-item");

    public EmployeeExpenseReimbursementsPOM(WebDriver driver) {
        this.driver = driver;
        this.wait = new FluentWait<>(this.driver)
                .withTimeout(Duration.ofSeconds(60))
                .pollingEvery(Duration.ofMillis(500));
    }

    public Boolean login(String email, String username, String password) {
        Boolean success = false;
        try {
            this.wait.until(ExpectedConditions.titleContains("Expense Reimbursement System: Login"));
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        this.emailElem = this.driver.findElement(By.id("email-input"));
        this.usernameElem = this.driver.findElement(By.id("username-input"));
        this.passwordElem = this.driver.findElement(By.id("password-input"));
        this.loginButtonElem = this.driver.findElement(By.id("login-btn"));

        this.emailElem.sendKeys(email );
        this.usernameElem.sendKeys(username );
        this.passwordElem.sendKeys(password );
        this.loginButtonElem.click();
        try {
            this.wait.until(ExpectedConditions.urlContains("employee-dashboard"));
            success = true;
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return success;
    }

    // Return the WebElement of the newly created expense reimbursement request by the Employee
    public WebElement createExpenseReimbursementRequest(String description, Double amount, Integer type) {
        try {
            this.wait.until(ExpectedConditions.urlContains("employee-dashboard"));
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        descriptionElem = driver.findElement(By.id("reimb-description"));
        amountElem = driver.findElement(By.id("reimb-amount"));
        typeDropdownElem = driver.findElement(By.id("reimb-type-id"));
        submitBtnElem = driver.findElement(By.id("reimb-submit"));
        // Get reimbursement requests before creation of our new request
        List<WebElement> reimbBeforeCreation = new ArrayList<>();
        try {
            this.wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(reimbListSelector, 0));
            reimbBeforeCreation = driver.findElements(reimbListSelector);
        } catch (TimeoutException e){
            System.out.println("There are currently 0 expense reimbursement requests for this employee.");
        }

        descriptionElem.sendKeys(description + Keys.TAB);
        amountElem.sendKeys(String.valueOf(amount) + Keys.TAB);
        Select typeDropdownSelect = new Select(typeDropdownElem);
        typeDropdownSelect.selectByIndex(type);
        submitBtnElem.click();

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(reimbListSelector, reimbBeforeCreation.size()));
        List<WebElement> reimbAfterCreation = driver.findElements(reimbListSelector);

        return reimbAfterCreation.get(0);
    }
}
