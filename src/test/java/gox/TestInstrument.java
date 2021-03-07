package gox;

import gox.util.*;
import id.aldochristiaan.salad.Salad;
import id.aldochristiaan.salad.util.Driver;
import id.aldochristiaan.salad.util.LogLevel;
import cucumber.api.Scenario;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class TestInstrument {

    private static final HashMap<String, Boolean> STATE = new HashMap<String, Boolean>();
    public static Properties capabilitiesProperties = getCapabilitiesProperties();
    public static Scenario scenario;
    protected static Gox gox;
    protected static AndroidDriver<AndroidElement> androidDriver;
    private static String scenarioName;
    private static boolean isRunning = false;
    private static boolean stfStarted = false;
    private static String stfRemoteDeviceUrl;
    private LogLevel appiumLogLevel;
    public static final Dotenv dotenv = Dotenv.load();

    private static Salad salad;

    private static Properties getCapabilitiesProperties() {
        Properties capabilitiesProperties = new Properties();
        try {
            FileInputStream properties = new FileInputStream("capabilities.properties");
            capabilitiesProperties.load(properties);
            properties.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return capabilitiesProperties;
    }

    private static void updateCapabilitiesFromSystemProp() {
        String value;
        for (Map.Entry<Object, Object> e : capabilitiesProperties.entrySet()) {
            value = (String) e.getValue();

            if (value.contains("{")) {
                value = value.replace('{', ' ');
                value = value.replace('}', ' ');
                value = System.getProperty(value.trim());
                capabilitiesProperties.setProperty((String) e.getKey(), value);
            }
        }
        LogUtil.info(capabilitiesProperties.toString());
    }

    public void setUp() {
        String elementPropertiesDirectory;
        updateCapabilitiesFromSystemProp();
        LogUtil.info(capabilitiesProperties.toString());
        elementPropertiesDirectory = "src/test/resources/gox/elements/gox/";

        if (!isRunning) {
            salad = getSaladInstance(capabilitiesProperties, elementPropertiesDirectory);
            LogUtil.info("capabilities : " + capabilitiesProperties);

            salad.start();
            isRunning = true;

            initiateSession();
            STATE.put("state", false);
        } else {
            LogUtil.info("Appium server is already started!");
            if (salad.getAndroidDriver() == null) initiateSession();
        }
    }

    private Salad getSaladInstance(Properties capabilitiesProperties,String elementPropertiesDirectory) {
        Salad salad;
        String appiumHost = "appiumLocalHost";
        if(capabilitiesProperties.containsKey(appiumHost)) {
            LogUtil.info("Running test using Appium Local Server");
            String host = String.format("http://%s", System.getProperty(appiumHost));
            salad = new Salad(capabilitiesProperties, elementPropertiesDirectory, Driver.ESPRESSO, host);
        } else {
            LogUtil.info("Running test using Appium Service Builder");
            setAppiumLogLevel();
            salad = new Salad(capabilitiesProperties, elementPropertiesDirectory, Driver.ESPRESSO, appiumLogLevel);
        }
        return salad;
    }

    private void setAppiumLogLevel() {
        try {
            appiumLogLevel = LogLevel.valueOf(System.getProperty("logLevel"));
        } catch (Exception ex){
            appiumLogLevel = LogLevel.ERROR;
        }
    }

    public void afterScenario(Scenario scenario) {
        scenarioName = scenario.getName();

        if (scenario.isFailed()) {
            try {
                File srcFile = ((TakesScreenshot) androidDriver).getScreenshotAs(OutputType.FILE);
                byte[] screenshot = ((TakesScreenshot) androidDriver).getScreenshotAs(OutputType.BYTES);
                File imageFile = new File(dotenv.get("SCREENSHOT_PATH") + "/" + scenarioName + ".png");
                scenario.embed(screenshot, "image/png");
                FileUtils.copyFile(Objects.requireNonNull(srcFile), imageFile);
                LogUtil.info("Screenshot taken");
                //commenting this for debug purpose
                //sendImageToTelegram(prepareImageFile(imageFile));
            } catch (Exception e) {
                LogUtil.error("Exception while taking screenshot", e);
            } finally {
                LogUtil.info("Reseting app");
                androidDriver.resetApp();
                DataUtil.setDataAfterReset();
            }
        }
        DataUtil.setDataAfterScenario();
    }

    public void tearDown() {
        salad.stop(Driver.ESPRESSO);
    }

    public void initiateSession() {
        salad.initiateSession(Driver.ESPRESSO);
        androidDriver = salad.getAndroidDriver();
        gox = new Gox(androidDriver);
    }

    public String getSessionId() {
        return androidDriver.getSessionId().toString();
    }
    
    public void removeSession() {
        salad.removeSession(Driver.ESPRESSO);
    }

    protected Boolean getStateOfFeature() {
        return STATE.get("state");
    }

    protected void replaceValueState(Boolean oldvalue, Boolean newvalue) {
        STATE.replace("state", oldvalue, newvalue);
    }

    public Boolean isSessionNull(){
        boolean sessNull;
        try {
            if(!getSessionId().equals("null") || !getSessionId().equals("")){
                sessNull = false;
            }else sessNull = true;
        }catch (NullPointerException e){
            sessNull = true;
        }
        LogUtil.info("isSessionNull : "+sessNull);
        return sessNull;
    }
}
