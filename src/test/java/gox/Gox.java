package gox;


import gox.pageObject.gox.LoginOnboardingPage;
import gox.pageObject.gox.LoginPage;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class Gox {

    private final AndroidDriver<AndroidElement> androidDriver;

    public Gox(AndroidDriver<AndroidElement> androidDriver) {
        this.androidDriver = androidDriver;
    }

    public LoginOnboardingPage loginOnboardingPage() {
        return new LoginOnboardingPage(androidDriver);
    }

    public LoginPage loginPage() {
        return new LoginPage(androidDriver);
    }

}
