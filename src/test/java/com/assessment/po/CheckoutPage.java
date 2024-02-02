
package com.assessment.po;

import static com.assessment.step_definitions.Hooks.commonWEBHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.assessment.utils.BaseHelper;

public class CheckoutPage extends BaseHelper {
	public WebDriver driver = getBrowser();

	protected By emptyCartButton = By.xpath("//button[text()='Empty Cart']");
	protected By emptyCartButtonConfirmation = By.xpath("//footer//button[contains(text(),'Empty')]");
	protected By emptyCartVerificationMessage = By.xpath("//p[contains(text(),'Your cart is empty.')]");
	
	
	public CheckoutPage(WebDriver browser) {
		// TODO Auto-generated constructor stub
	}
 
	//allows us to empty everything in our cart
	public void emptyCart() throws InterruptedException {
		try {
			driver.findElement(By.cssSelector("[data-testid='cart-button']")).click();
		} catch (Exception e) {
			Thread.sleep(2000);
			WebElement ele = driver.findElement(By.cssSelector("[data-testid='cart-button']"));
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", ele);
		}
		commonWEBHelper.clickOnElement(emptyCartButton);
	}

	//allows us to confirm if we want to empty our cart
	public void confirmEmptyCart() {
		commonWEBHelper.clickOnElement(emptyCartButtonConfirmation);
	}
	
	
	//checks if the cart is actually empty
	public void verifyEmptyCart() {
		commonWEBHelper.isElementPresent(emptyCartVerificationMessage);
	}
}
