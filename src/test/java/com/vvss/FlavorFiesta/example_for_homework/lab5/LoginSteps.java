package com.vvss.FlavorFiesta.example_for_homework.lab5;
import net.thucydides.core.annotations.Step;

public class LoginSteps {

    LoginPage loginPage = new LoginPage();

    @Step("Open the login page")
    public void openLoginPage() {
        loginPage.openLoginPage();
    }

    @Step("Enter username {0}")
    public void enterUsername(String username) {
        loginPage.enterUsername(username);
    }

    @Step("Enter password {0}")
    public void enterPassword(String password) {
        loginPage.enterPassword(password);
    }

    @Step("Click the login button")
    public void clickLoginButton() {
        loginPage.clickLoginButton();
    }

    @Step("Check error message")
    public String getErrorMessage() {
        return loginPage.getErrorMessage();
    }
}

