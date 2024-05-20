package com.vvss.FlavorFiesta.example_for_homework.lab5;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.annotations.findby.FindBy;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.web.server.LocalServerPort;

public class LoginPage extends PageObject {
    private String loginPageURL;

    @Override
    public WebDriver getDriver() {
        return new ChromeDriver();
    }
    public void setLoginPage(String page){
        loginPageURL = page;
    }

    public void openLoginPage() {
        getDriver().get(loginPageURL);
    }
    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "loginButton")
    private WebElement loginButton;

    @FindBy(id = "errorMessage")
    private WebElement errorMessage;

    public void enterUsername(String username) {
        usernameField.sendKeys(username);
    }

    public void enterPassword(String password) {
        passwordField.sendKeys(password);
    }

    public void clickLoginButton() {
        loginButton.click();
    }

    public String getErrorMessage() {
        return errorMessage.getText();
    }
}
