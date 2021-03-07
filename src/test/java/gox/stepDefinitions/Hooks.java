package gox.stepDefinitions;

import java.util.HashMap;
import gox.TestInstrument;
import gox.util.DataUtil;
import gox.util.LogUtil;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java8.En;

public class Hooks extends TestInstrument implements En {
    private static final HashMap<String, Boolean> FEATURE_MAP = new HashMap<String, Boolean>();
    private static String lastScenarioStatus = "failed";

    public Hooks() {

        Before(0, () -> {
            setUp();
            Runtime.getRuntime().addShutdownHook(new Thread(this::tearDown));
        });

        Before(1, (Scenario scenario) -> {
            if (isFeatureNameNotExist(scenario)) {
                if (getStateOfFeature()) {
                    LogUtil.error("reset session");
                    resetSession();
                }else {
                    replaceValueState(false, true);
                }
            } else {
                if(getLastScenarioStatus().equals("failed") && isSessionNull()){
                    LogUtil.info("Previous scenario is failed and session is disconnected");
                    resetSession();
                }else {
                    LogUtil.info("Continue with existing session: " + getSessionId());
                }
            }
            LogUtil.info("Running scenario : " + scenario.getName());
            LogUtil.info("----------------Start of Scenario----------------");
        });

        After(0, (Scenario scenario) -> {
            LogUtil.info("Scenario '"+scenario.getName()+"' with status "+scenario.getStatus().lowerCaseName());
            LogUtil.info("-----------------End of Scenario-----------------");
            setLastScenarioStatus(scenario.getStatus().lowerCaseName());
        });

        After(1, this::afterScenario);
    }

    private Boolean isFeatureNameNotExist(Scenario scenario) {
        String[] dir = scenario.getId().split("/");
        String featureName = dir[dir.length - 1].split(":")[0];
        FEATURE_MAP.put("default.feature", true);
        
        if (FEATURE_MAP.get(featureName) == null ) {
            LogUtil.info("Run this feature at firstime: " + featureName);
            FEATURE_MAP.put(featureName, true);
            return true;
        } else {
            LogUtil.info("Run this feature not at firstime: " + featureName);
            return false;
        }
    }

    private void resetSession() {
        removeSession();
        initiateSession();
        DataUtil.setDataAfterReset();
    }

    private void setLastScenarioStatus(String status){
        lastScenarioStatus = status;
    }

    private String getLastScenarioStatus(){
        return lastScenarioStatus;
    }

    @Before
    public static void beforeStep(Scenario mScenario) {
        scenario = mScenario;
    }
}
