package com.assessment.step_definitions;

import static com.assessment.step_definitions.Hooks.commonWEBHelper;
import java.util.Set;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class Common_web_step_definitions {
	
	@Then("I should see {string} page")
	public void i_should_see_page(String title) {
		commonWEBHelper.verifyTitle(title);
	}

	@Then("I close the browser")
	public void i_close_the_browser() {
		commonWEBHelper.closeBrowser();
	}
	
	@Then("I logout of the application")
	public void i_log_out_of_the_application() {
		commonWEBHelper.logout();
	}
	
	
	
	@Then("I close the page opened and switch to the only active window")
	public void i_close_the_page_opened_and_switch_to_the_only_active_window() {
		commonWEBHelper.getBrowser().close();
		for (String winHandle : commonWEBHelper.getBrowser().getWindowHandles()) {
			commonWEBHelper.getBrowser().switchTo().window(winHandle);
		}
	}
	
	@And("I close the current tab and switch driver to another tab")
	public void i_close_current_tab_and_switch_driver_to_another_tab() {
		Set<String> windowHandles=commonWEBHelper.getBrowser().getWindowHandles();
		for(String winHandle:windowHandles) {
			commonWEBHelper.getBrowser().switchTo().window(winHandle);
		}
		commonWEBHelper.getBrowser().close();
		windowHandles=commonWEBHelper.getBrowser().getWindowHandles();
		for(String winHandle:windowHandles) {
			commonWEBHelper.getBrowser().switchTo().window(winHandle);
		}
	}

	@Then("I open the new browser window")
	public void i_open_the_new_browser_window() {
		commonWEBHelper.openNewBrowserWindow();
	}
	
	@Then("I open the browser")
	public void i_open_the_browser() {
		commonWEBHelper.openBrowser("chrome");
	}
	
	@Then("I open the browser with clean state")
	public void i_open_the_browser_with_clean_state() {
		commonWEBHelper.clearCookies();
		while (commonWEBHelper.getBrowser().manage().getCookies().size() > 0) {
			commonWEBHelper.sleep(2);
		}
	}
	
	@Then("I clear the browser cookies")
	public void i_clear_the_browser_cookies() {
		while (commonWEBHelper.getBrowser().manage().getCookies().size() > 0) {
			commonWEBHelper.sleep(2);
		}
	}
	
	@Then("I select {string} method")
	public void i_select_method(String method) {
		commonWEBHelper.selectMethod(method);
	}

	@Then("I switch to another open tab")
	public void i_switch_to_another_browser_tab() {
		Set<String> windowHandles=commonWEBHelper.getBrowser().getWindowHandles();
		for(String winHandle:windowHandles) {
			commonWEBHelper.getBrowser().switchTo().window(winHandle);
		}
	}
	
	@Then("I wait for {int} seconds")
	public void waitFor(int seconds) {
		commonWEBHelper.sleep(seconds);
	}
	
	

	//commonWEBHelper.logout();
}