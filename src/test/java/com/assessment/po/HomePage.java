package com.assessment.po;

import static com.assessment.step_definitions.Hooks.commonWEBHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.assessment.utils.BaseHelper;

public class HomePage extends BaseHelper {
	
	public WebDriver driver = getBrowser();
	protected By pageTitleMsg = By.cssSelector(".page-title__heading>span");
	protected By searchField = By.xpath("//input[@id='searchval'][1]");
	protected By searchButton = By.xpath("//button[@value='Search'][1]");
	
	public HomePage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void clickSearchField(){
		commonWEBHelper.clickOnElement(searchField);
	}

	public void enterSearchCriteria(String keywords) {
		commonWEBHelper.enterValueInField(searchField,keywords);
	}
	
	public void clickSearchButton(){
		commonWEBHelper.clickOnElement(searchButton);
	}
	public void clickTabWithName(String tab) {
		By menuTab=By.cssSelector(".ncf-navigation>a[title*='"+tab+"']");
		commonWEBHelper.clickOnElement(menuTab);
		
		By menuTabSelected=By.cssSelector("a[title*='"+tab+"'].is-active");
		commonWEBHelper.waitForElementToBeVisible(menuTabSelected);
	}

}