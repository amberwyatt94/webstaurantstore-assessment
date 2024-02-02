package com.assessment.testrunner;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.assessment.utils.ReportUtils;
import com.aventstack.extentreports.service.ExtentService;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = { "src/test/resources/features"}, glue = {
		"com.assessment.step_definitions"},plugin = { "pretty", "json:target/cucumber-json/cucumber.json","com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:" }, 
				tags = "@task", dryRun = false, publish = false)

public class TestRunner {
	@BeforeClass

    public static void beforeClass() throws FileNotFoundException, IOException, XmlPullParserException {

        ReportUtils.getSystemInfo().forEach((k, v) -> ExtentService.getInstance().setSystemInfo(k, v));

    }
}
