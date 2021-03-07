package gox.pageObject.gox;

import gox.pageObject.base.BasePage;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class LoginPage extends BasePage {

    public LoginPage(AndroidDriver<AndroidElement> androidDriver) {
        super(androidDriver);
    }

    public void isOnLoginPage() {
        validateDisplayed("USERNAME_EDIT_TEXT", 10);
        validateDisplayed("PASSWORD_EDIT_TEXT", 10);
    }

    public void typeOnUsernameEditText(String username) {
        type().element("USERNAME_EDIT_TEXT", username);
    }

    public void typeOnPasswordEditText(String password) {
        type().element("PASSWORD_EDIT_TEXT", password);
    }

    public void tapOnButtonLogin() {
        tap().element("LOGIN_BUTTON");
    }

    public void login(String username, String password) throws InterruptedException {
        typeOnUsernameEditText(username);
        typeOnPasswordEditText(password);
        tapOnButtonLogin();
        Thread.sleep(5000);
    }

}
