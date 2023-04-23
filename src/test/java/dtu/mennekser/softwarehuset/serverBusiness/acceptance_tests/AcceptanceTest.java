package dtu.mennekser.softwarehuset.serverBusiness.acceptance_tests;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;

@RunWith(Cucumber.class)
@CucumberOptions(features = "use_cases",
        plugin = {"summary", "html:target/cucumber/wikipedia.html"},
        monochrome = true,
        snippets = SnippetType.CAMELCASE,
        glue = {"dtu.mennekser.softwarehuset.serverBusiness.acceptance_tests", "dtu.mennekser.softwarehuset.serverInterface.acceptance_tests"})

public class AcceptanceTest {

}

