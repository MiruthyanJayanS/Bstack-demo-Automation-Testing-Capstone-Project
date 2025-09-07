package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
  features = "src/test/resources/features",
  glue = {"stepdefinitions"},
  plugin = {
    "pretty",
    "json:target/cucumber-reports/cucumber.json",
    "html:target/cucumber-reports/cucumber.html",
  },
  tags = "@smoke",
  monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests { }
