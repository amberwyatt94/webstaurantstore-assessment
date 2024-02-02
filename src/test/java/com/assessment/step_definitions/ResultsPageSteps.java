package com.assessment.step_definitions;

import static com.assessment.step_definitions.Hooks.commonWEBHelper;

import com.assessment.po.HomePage;
import com.assessment.po.LoginPage;
import com.assessment.po.ResultsPage;
import io.cucumber.java.en.And;

public class ResultsPageSteps {

	LoginPage loginPage = new LoginPage(commonWEBHelper.getBrowser());
	HomePage homePage = new HomePage(commonWEBHelper.getBrowser());
	ResultsPage resultspage = new ResultsPage(commonWEBHelper.getBrowser());
	
	
	@And("I verify each search results title contains the word table")
	public void i_verify_search_results() {
		resultspage.verifySearchResults();	 
	}
	
	@And("I click the next search results page")
	public void i_click_the_next_search_results_page(){
		resultspage.verifyNextPageSearchResults();
	}
	
	@And("I add the last item found to my cart")
	public void i_add_the_last_item_found() {
		resultspage.addLastItemFoundToCart();
	}
}