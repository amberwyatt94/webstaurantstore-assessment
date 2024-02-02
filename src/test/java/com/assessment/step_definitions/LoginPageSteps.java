package com.assessment.step_definitions;

import com.assessment.po.HomePage;
import com.assessment.po.LoginPage;
import com.assessment.utils.PropertyReader;
import io.cucumber.java.en.Given;

import static com.assessment.step_definitions.Hooks.commonWEBHelper;

public class LoginPageSteps {
	LoginPage loginPage = new LoginPage(commonWEBHelper.getBrowser());
	HomePage homePage = new HomePage(commonWEBHelper.getBrowser());

	@Given("I navigate to the Home Page for Webstaurant")
	public void i_navigate_to_home_page() {
		if (PropertyReader.readApplicationConfigFile("env").equals("qa")) {
			loginPage.openURL(PropertyReader.readQaConfigFile("url"));
		} else if (PropertyReader.readApplicationConfigFile("env").equals("dev1")) {
			loginPage.openURL(PropertyReader.readDev1ConfigFile("url"));
		} else if (PropertyReader.readApplicationConfigFile("env").equals("dev2")) {
			loginPage.openURL(PropertyReader.readDev2ConfigFile("url"));
		} else if (PropertyReader.readApplicationConfigFile("env").equals("prod")) {
			loginPage.openURL(PropertyReader.readProdConfigFile("url"));
		} else if (PropertyReader.readApplicationConfigFile("env").equals("staging")) {
			loginPage.openURL(PropertyReader.readstagingConfigFile("url"));
		}
	}

}