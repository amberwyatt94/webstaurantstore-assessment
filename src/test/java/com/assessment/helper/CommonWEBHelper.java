package com.assessment.helper;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.assessment.utils.BaseHelper;
import com.assessment.utils.GlobalVariables;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class CommonWEBHelper extends BaseHelper {
	protected By logout = By.xpath("//a[text()='Log out']");

	protected By getMethodLocator(String method) {
		return By.cssSelector("label[for='verb" + method.toLowerCase(Locale.ROOT) + "']");
	}

	public void openBrowser(String browserName) {
		initiateBrowser(browserName);
	}

	public void openURL(String url) {
		super.openURL(url);
	}

	public void closeBrowser() {
		super.closeBrowser();
	}

	public void openNewBrowserWindow() {
		super.openNewWindow();
	}

	public void quitBrowser() {
		super.quitBrowser();
	}

	public void navigateToURL(String url) {
		goToURL(url);
	}

	public void enterValueInField(By field, String value) {
		super.sendKeys(field, value);
	}
	
	public void enterIntValueInField(By field, int value) {
		super.sendKeys(field, value);
	}

	public void enterValueInFieldAndSubmit(By field, String value) {
		super.sendKeysWithSubmit(field, value);
	}

	public String getRandomStringOfLengthK(int N) {
		return super.getStringOfLength(N);
	}

	public void scrollDownThePage() {
		JavascriptExecutor js = (JavascriptExecutor) getBrowser();
		js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
	}

	public void scrollUpThePage() {
		JavascriptExecutor js = (JavascriptExecutor) getBrowser();
		js.executeScript("window.scrollBy(document.body.scrollHeight,0)");
	}

	public void verifyTitle(String title) {
		super.waitForTitle(title);
	}

	public void selectMethod(String method) {
		super.clickOnElement(getMethodLocator(method));
	}

	public boolean isPageContainsValue(String value) {
		boolean result = pageContainsValue(value);
		System.out.println(result + " -> Web Page contains : " + value);
		return result;
	}

	public void click_JS(By leagueBtn) {
		super.click_JS(leagueBtn);
	}

	public void hoverOverAnItem(By item) {
		super.hoverOverAnItem(item);
	}

	public void reloadPage() {
		getBrowser().navigate().refresh();
	}

	public void enterValue_JS(By by, String value) {
		WebElement ele = getBrowser().findElement(by);

		JavascriptExecutor jse = (JavascriptExecutor) getBrowser();
		String script = "arguments[0].value='" + value + "';";
		jse.executeScript(script, ele);
	}

	public List<WebElement> getElementInList(By option) {
		return getBrowser().findElements(option);
	}

	public void clearCookies() {
		getBrowser().manage().deleteAllCookies();
	}

	public void elementListContainsItemWithText(By selectedProjects, String project) {
		List<WebElement> list = getBrowser().findElements(selectedProjects);
		List<String> itemsList = new ArrayList<String>();
		for (WebElement ele : list) {
			itemsList.add(ele.getText());
		}
		Assert.assertTrue(itemsList.contains(project.trim()));
	}

	public List<String> getTextOfAllElementsInList(By locator) {
		List<WebElement> list = getBrowser().findElements(locator);
		List<String> itemsList = new ArrayList<String>();
		for (WebElement ele : list) {
			itemsList.add(ele.getText());
		}
		return itemsList;
	}

	public void switchDriverToOpenedTab() {
		getWait().until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {
				return getBrowser().getWindowHandles().size() == 2;
			}
		});
		for (String winHandle : getBrowser().getWindowHandles()) {
			System.out.println(winHandle);
			getBrowser().switchTo().window(winHandle);
		}
	}

	public String getPercentValuesInRoundFigure(String input) {
		long output = round(Double.parseDouble(input.replace("%", "")), 0);
		return output + "%";
	}

	public static long round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return ((int) tmp / factor);
	}

	public Map<String, String> getSubTreeInJsonAtNode(String className, String jsonFile) {
		Map<String, String> subTree = new HashMap<String, String>();
		ObjectMapper objectMapper = new ObjectMapper();
		File from = new File(GlobalVariables.resourceDir + jsonFile);
		try {
			JsonNode root = objectMapper.readTree(from);
			JsonNode objNode = (JsonNode) root.get(className).get(0);
			subTree = objectMapper.convertValue(objNode, new TypeReference<Map<String, String>>() {
			});

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return subTree;
	}

	public String getHexValue(String rgba) {
		String arr[] = rgba.substring(rgba.indexOf("(") + 1, rgba.indexOf(")")).replaceAll(",", "").split(" ");
		int array[] = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			array[i] = Integer.parseInt(arr[i]);
		}
		String hexValue = String.format("#%02x%02x%02x", array[0], array[1], array[2]);
		return hexValue;
	}

	public void logout() {
	//	clickOnElement(logout);
		waitForElemenetToBeClickable(logout);
		//scrolltoElement(logout);
		click_JS(logout);

	}

	public static int getIntegerOfLength(int k) {
		String alphabet = "0123456789";
		String uid = "";
		Random random = new Random();
		for (int i = 0; i < k; i++) {
			char c = alphabet.charAt(random.nextInt(10));
			uid += c;
		}
		return Integer.parseInt(uid);
	}
}