package com.sdastest.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.sdastest.projects.website.crm.stepdefinitions",
                "com.sdastest.projects.website.cms.stepdefinitions",
                "com.sdastest.projects.website.hrm.stepdefinitions",
                "com.sdastest.hooks"
        },
        plugin = {
                "com.sdastest.hooks.CucumberListener",
                "pretty",
                "html:target/cucumber-reports/cucumber-reports.html",
                "json:target/cucumber-reports/cucumber-reports.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true,
        tags = "@Regression or @Smoke"
)

public class TestRunnerAllFeatureByTag extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
