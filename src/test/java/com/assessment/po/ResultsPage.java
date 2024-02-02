package com.assessment.po;

import static com.assessment.step_definitions.Hooks.commonWEBHelper;


import java.util.List;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.assessment.utils.BaseHelper;

public class ResultsPage extends BaseHelper {
	public WebDriver driver = getBrowser();
	
	public ResultsPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
	}
	
	protected By pagnationButton = By.xpath("//li[@class='inline-block leading-4 align-top rounded-r-md']");
	String searchTerm = "Table";//used to verify if each result is valid
	List<WebElement> results = driver.findElements(By.xpath("//a[contains(@data-testid,'itemDescription')]"));
	protected By addToCartButton = By.xpath("//input[@name='addToCartButton']");



	//allows us to navigate through the search results
	public void verifyNextPageSearchResults() {
		try {
			commonWEBHelper.clickOnElement(pagnationButton);
		} catch(StaleElementReferenceException e) {
			pagnationButton = By.xpath("//li[@class='inline-block leading-4 align-top rounded-r-md']");

		}

		commonWEBHelper.clickOnElement(pagnationButton);

		for (int i = 0; i < results.size(); i++) {
			List<WebElement> results = driver.findElements(By.xpath("//span[contains(@data-testid,'itemDescription')]"));

			Assert.assertTrue(results.get(i).getText().contains(searchTerm));
			
		}
		int resultsCount = results.size();
		System.out.println(resultsCount);

	}
	
//allows us to verify if table is in each search result
	public void verifySearchResults()
	{
		By locatorPagination=By.xpath("//a[contains(@aria-label,'go to page')]/../following-sibling::li[1]");
		int allPagesCount=Integer.parseInt(driver.findElement(locatorPagination).getText());
		
		// below code checks for the world table on every page except page 7
		for(int page=1;page<=allPagesCount;page++) {
			if(page!=7) {
			By locatorEachPage=By.xpath("(//a[contains(@aria-label,'page "+page+"')])[1]");
			driver.findElement(locatorEachPage).click();
			By locator=By.xpath("//span[contains(@data-testid,'itemDescription')]");
			
			new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
			List<WebElement> results = driver.findElements(locator);
			for (int j = 0; j < results.size(); j++) {
				Assert.assertTrue("NOT MATCHING:: instead the product name contains "+results.get(j).getText(),results.get(j).getText().contains(searchTerm));
			}
			
			//Gives us the count of each result by page
			int resultsCount = results.size();
			System.out.println("Count of results on page "+page+" :: "+resultsCount);
			}
		}
		
	}		
	
  //allows us to add the last of items found to the cart
   public void addLastItemFoundToCart() {

	   new WebDriverWait(driver,30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//input[@data-testid='itemAddCart'])[last()]"))).click();
 

	}

	
}
