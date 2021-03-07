package gox.stepDefinitions.gox;

import cucumber.api.java8.En;
import gox.TestInstrument;

public class LoginPageSteps extends TestInstrument implements En {

    public LoginPageSteps() {

        Given("user is on login page", () -> {
            gox.loginPage().isOnLoginPage();
        });

        When("user login with username \"([^\"]*)\" and password \"([^\"]*)\"", (String username, String password) -> {
            gox.loginPage().login(username, password);
        });
    }
}