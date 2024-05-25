package com.vvss.FlavorFiesta.example_for_homework.lab5;

import lombok.Setter;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.annotations.findby.FindBy;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@RunWith(SpringIntegrationSerenityRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@DefaultUrl("http://localhost:9000/login")
public class LoginPage extends PageObject {
    @LocalServerPort
    private int port;
    private String baseURL;

    @Setter
    private String loginPageURL;

    WebDriver driver;

    @Override
    public WebDriver getDriver() {
        if (this.driver == null) {
            this.driver = new ChromeDriver();
        }
        return this.driver;
    }

    @Step("Open the login page")
    public void openLoginPage() {
        baseURL = "http://localhost:" + port;
        getDriver().get(baseURL + "/login");
    }

    @FindBy(id = "username")
    WebElement usernameField = driver.findElement(By.id("username"));
    // getDriver() => null
    // also this.driver => null
    // without driver.findElement(By.id("username")) => this.usernameField is null

    @FindBy(id = "password")
    WebElement passwordField;

    @FindBy(id = "loginButton")
    WebElement loginButton;

    @FindBy(id = "errorMessage")
    WebElement errorMessage;

    @FindBy(id = "logoutButton")
    WebElementFacade logoutButton;

    @Step("Enter username {0}")
    public void enterUsername(String username) {
        usernameField.sendKeys(username);
    }

    @Step("Enter password {0}")
    public void enterPassword(String password) {
        passwordField.sendKeys(password);
    }

    @Step("Click the login button")
    public void clickLoginButton() {
        loginButton.click();
    }

    @Step("Check error message")
    public String getErrorMessage() {
        return errorMessage.getText();
    }

    @Step("Check for logoutButton")
    public boolean showLogoutButton(){
        return logoutButton.isVisible();
    }
}