package com.vvss.FlavorFiesta.example_for_homework.lab5;

import com.vvss.FlavorFiesta.models.User;
import com.vvss.FlavorFiesta.services.UserService;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.thucydides.core.annotations.Steps;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@RunWith(SpringIntegrationSerenityRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestsL5 {
    @Steps
    LoginSteps loginSteps;
    private UserService userService;
    @LocalServerPort
    private int port;
    private String baseURL;
    private String loginPageURL;
    private LoginPage loginPage;

    @BeforeEach
    public void setup(){
        baseURL = "http://localhost:" + port;
        loginPageURL = baseURL + "/login";
        loginPage = new LoginPage();
        loginPage.setLoginPage(loginPageURL);
        loginSteps = new LoginSteps();
        userService = new UserService();
        User user = new User("username", "email", "password");
        userService.saveUser(user);
    }

    @Test
    public void shouldLoginSuccessfully() {
        loginSteps.openLoginPage();
        loginSteps.enterUsername("validUser");
        loginSteps.enterPassword("validPassword");
        loginSteps.clickLoginButton();
    }

    @Test
    public void shouldFailToLogin() {
        loginSteps.openLoginPage();
        loginSteps.enterUsername("invalidUser");
        loginSteps.enterPassword("validPassword");
        loginSteps.clickLoginButton();

        String errorMessage = loginSteps.getErrorMessage();
        assertThat(errorMessage).isEqualTo("Invalid username");
    }
}
