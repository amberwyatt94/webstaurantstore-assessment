package com.assessment.step_definitions;

import static com.assessment.step_definitions.Hooks.commonWEBHelper;

import com.assessment.po.CheckoutPage;
import com.assessment.po.HomePage;
import com.assessment.po.LoginPage;
import com.assessment.po.ResultsPage;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class CheckoutPageSteps {

	LoginPage loginPage = new LoginPage(commonWEBHelper.getBrowser());
	HomePage homePage = new HomePage(commonWEBHelper.getBrowser());
	ResultsPage resultspage = new ResultsPage(commonWEBHelper.getBrowser());
	CheckoutPage checkoutpage = new CheckoutPage(commonWEBHelper.getBrowser());
	
	@Then("I remove the item from my cart")
	public void i_remove_the_item_from_my_cart() throws InterruptedException {
	 checkoutpage.emptyCart();
	}
	@Then("I confirm that I want to empty my cart")
	public void i_confirm_that_i_want_to_empty_my_cart() {
	   checkoutpage.confirmEmptyCart();
	}
	
	@And("I confirm that my cart is empty")
	public void i_confirm_that_my_cart_is_empty() {
		checkoutpage.verifyEmptyCart();
	}
	
}