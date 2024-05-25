package com.vvss.FlavorFiesta.example_for_homework.lab5;

import com.vvss.FlavorFiesta.models.User;
import com.vvss.FlavorFiesta.services.UserService;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringIntegrationSerenityRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestsL5 {
    @Steps
    LoginPage loginPageAndSteps;

    @Autowired
    private UserService userService;

    String encodedPassword = "password";

    User testUser;

    @BeforeEach
    void setup(){
        testUser = new User("username", "email", encodedPassword);
        userService.saveUser(testUser);
    }

    @AfterAll
    public void teardown(){
        userService.getAllUsers().forEach(userService::saveUser);
    }

    @Test
    public void shouldLoginSuccessfully() {
        loginPageAndSteps.openLoginPage();
        loginPageAndSteps.enterUsername(testUser.getUsername());
        loginPageAndSteps.enterPassword(testUser.getPassword());
        loginPageAndSteps.clickLoginButton();
        assertTrue(loginPageAndSteps.showLogoutButton());
    }

    @Test
    public void shouldFailToLogin() {
        loginPageAndSteps.openLoginPage();
        loginPageAndSteps.enterUsername("invalidUser");
        loginPageAndSteps.enterPassword("invalidPassword");
        loginPageAndSteps.clickLoginButton();

        String errorMessage = loginPageAndSteps.getErrorMessage();
        assertThat(errorMessage).isEqualTo("Invalid username and password");
    }
}