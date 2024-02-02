
package com.assessment.step_definitions;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.assessment.helper.CommonAPIHelper;
import com.assessment.helper.CommonWEBHelper;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;

public class Hooks {

	protected static CommonAPIHelper commonAPIHelper;
	public static CommonWEBHelper commonWEBHelper;

	// Will execute once throughout the execution
	@BeforeAll
	public static void before_all() {
		commonAPIHelper = new CommonAPIHelper();
		commonWEBHelper = new CommonWEBHelper();
	}

	@Before
	public static void beforeScenario() {
		commonAPIHelper.initiateBrowser("chrome");
		commonWEBHelper.getBrowser().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		commonWEBHelper.getBrowser().manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
	}

	@AfterStep
	public void afterEachStep(Scenario scenario) throws IOException {
		commonWEBHelper.waitForPageToLoad();
	}

	@After
	public void afterEachScenario(Scenario scenario) throws IOException {
		if (scenario.isFailed()) {
			scenario.attach(getByteScreenshot(), "image/png", scenario.getName());
		
		}
		
	//	commonWEBHelper.getBrowser().close();
		
	}
	
	@AfterAll
	public static void after_all() throws IOException {
		//commonWEBHelper.getBrowser().quit();
	}

	private static byte[] getByteScreenshot() {
		return ((TakesScreenshot) commonWEBHelper.getBrowser()).getScreenshotAs(OutputType.BYTES);
	}

}
