package com.assessment.step_definitions;

import io.cucumber.java.en.And;
import static com.assessment.step_definitions.Hooks.commonWEBHelper;
import com.assessment.po.HomePage;

public class HomePageSteps {
	HomePage homePage = new HomePage(commonWEBHelper.getBrowser());
	
	@And("I click on the search field")
	public void i_click_on_search_field() {
		homePage.clickSearchField();
	}

	@And("I enter in the search keywords {string}")
	public void i_enter_the_search_keywords(String keywords) {
		homePage.enterSearchCriteria(keywords);
	}
	
	@And("I click the search button")
	public void i_click_the_search_button() {
		homePage.clickSearchButton();
	}
}
