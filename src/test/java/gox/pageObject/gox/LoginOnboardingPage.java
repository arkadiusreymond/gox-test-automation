package gox.pageObject.gox;

import gox.pageObject.base.BasePage;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class LoginOnboardingPage extends BasePage {

    public LoginOnboardingPage(AndroidDriver<AndroidElement> androidDriver) {
        super(androidDriver);
    }

    public void isOnLoginOnboardingPage() {
        validateDisplayed("SIGN_IN_TEXTVIEW", 10);
    }

    public void tapOnSignInTextView () {
        tap().element("SIGN_IN_TEXTVIEW");
    }

}
