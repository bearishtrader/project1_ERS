package pom;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;

public class LoginPOM {
    WebDriver driver;
    Wait<WebDriver> wait;

    WebElement emailElem;
    WebElement usernameElem;
    WebElement passwordElem;
    WebElement loginButtonElem;

    public LoginPOM(WebDriver driver) {
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
}
