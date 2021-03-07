package gox;

import org.junit.runner.RunWith;

@RunWith(io.cucumber.junit.Cucumber.class)
@io.cucumber.junit.CucumberOptions(
        features = {
                "classpath:gox/features",
        },
        stepNotifications = true,
        plugin = {
                "pretty",
                "rerun:rerun/failed_scenarios.txt",
                "com.epam.reportportal.cucumber.ScenarioReporter",
                "json:target/cucumber-report.json",
        })
public class CucumberTestRunner {
}