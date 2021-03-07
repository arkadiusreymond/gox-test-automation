package gox.stepDefinitions.gox;

import cucumber.api.java8.En;
import gox.TestInstrument;

public class LoginOnboardingSteps extends TestInstrument implements En {

    public LoginOnboardingSteps() {

        Given("user is on login onboarding page", () -> {
            gox.loginOnboardingPage().isOnLoginOnboardingPage();
        });

        When("user tap on sign in text view", () -> {
            gox.loginOnboardingPage().tapOnSignInTextView();
        });
    }
}