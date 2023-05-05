package dtu.mennekser.softwarehuset.whiteBoxTests;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "whiteBoxCases",
        plugin = {"summary", "html:target/cucumber/wikipedia.html"},
        monochrome = true,
        snippets = SnippetType.CAMELCASE,
        glue = {"dtu.mennekser.softwarehuset.whiteBoxTests"})

public class WhiteBoxTest {
}
