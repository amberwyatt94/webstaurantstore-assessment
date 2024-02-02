package com.assessment.utils;

import static com.assessment.step_definitions.Hooks.commonWEBHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.http.Method;

public class BaseHelper {
	// region VARIABLES
	private Map<String, Object> finalJSONKeyValues;
	private static WebDriver driver;
	private static WebDriverWait webDriverWait;
	
	// To initialize the rest request with base URL
	protected void initializeRestRequest(String baseURL) {
		RestAssured.baseURI = baseURL;
		GlobalVariables.httpRequest = RestAssured.given();
	}

	// To add authorization to the request
	protected void addBasicAuthorization(String username, String password) {
		GlobalVariables.httpRequest.auth().preemptive().basic(username, password);
	}

	// To add authorization to the request
	protected void addOauth2Authorization(String accessToken) {
		GlobalVariables.httpRequest.auth().preemptive().oauth2(accessToken);
	}

	// To Add header in the request
	protected void addRequestHeader(Map<String, String> headers) {
		GlobalVariables.httpRequest.headers(headers);
	}

	// To update the added headers in the request
	protected void updateRequestHeader(String headerKey, String value) {
		GlobalVariables.httpRequest.headers(headerKey, value);
	}

	// To add the parameters in the request
	protected void addRequestParameters(Map<String, String> parameters) {
		GlobalVariables.httpRequest.params(parameters);
	}

	// To update the value of given node by jsonPath in the JSON request body
	protected void updateAttributeInJSONRequestBody(String jsonString, String jsonPath, String newValue) {
		DocumentContext json = JsonPath.parse(jsonString);
		GlobalVariables.requestPayload = json.set(jsonPath, newValue).jsonString();
	}

	// To set the request body
	protected void generatedJSONPayload() {
		GlobalVariables.httpRequest.body(GlobalVariables.requestPayload);
	}

	// To submit the request
	protected void submitRequest(Method method, String uri) {
		GlobalVariables.httpResponse = null;
		String filePath;
		String fileName = uri.split("/")[uri.split("/").length - 1];
		String folderName = GlobalVariables.outputFilesPath + File.separator + fileName + File.separator
				+ new SimpleDateFormat("YYYYMMdd_hhmmss").format(new Date()).toString();
		FileHelper.createFolder(folderName);
		if (method != Method.GET) {
			filePath = folderName + File.separator + fileName + "_Request.txt";
			FileHelper.createFile(filePath, GlobalVariables.requestPayload);
			GlobalVariables.newlyCreatedRequestPayloadFile = filePath;
		}
		try {
			System.out.println(
					"\n\n******************************************************************************************************************************************************************\n\n");

			GlobalVariables.httpRequest.log().all();
			GlobalVariables.httpResponse = GlobalVariables.httpRequest.request(method, uri);
			System.out.println("\n\nRESPONSE BODY : " + GlobalVariables.httpResponse.getBody().asString());

			System.out.println(
					"\n\n******************************************************************************************************************************************************************\n\n");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		Assert.assertEquals(GlobalVariables.httpResponse.statusCode(), 200);
		filePath = folderName + File.separator + fileName + "_Response.txt";
		FileHelper.createFile(filePath, GlobalVariables.httpResponse.getBody().asString());
		GlobalVariables.newlyCreatedResponsePayloadFile = filePath;
	}

	// To get Single key value of the given jsonPath from the Response
	protected String getSingleValueFromResponse(String jsonPath) {
		String nodeValue = null;
		io.restassured.path.json.JsonPath jsonPathValue = GlobalVariables.httpResponse.jsonPath();
		nodeValue = jsonPathValue.getString(jsonPath);

		return nodeValue;
	}

	// To get Single key value of the given jsonPath from the Request
	protected String getSingleValueFromJson(String completeJson, String jsonPath) {
		String nodeValue = null;
		Object jsonNode = JsonPath.parse(completeJson).read("$." + jsonPath, Object.class);

		nodeValue = jsonNode.toString();
		return nodeValue;
	}

	// To get/return the response as String
	protected String returnResponseAsString() {
		return GlobalVariables.httpResponse.asString();
	}

	// To Assert the status code
	protected void assertStatusCode(int statusCode) {
		Assert.assertEquals(statusCode, GlobalVariables.httpResponse.statusCode());
	}

	// To assert the key value in the response
	protected void assertKeyValueInResponse(String jsonPath, String value) {
		Assert.assertEquals(value, getSingleValueFromResponse(jsonPath));
	}

	// To validate JSON with Schema
	protected boolean validateJSONSchema(String jsonString, String jsonSchemaString) {
		boolean result = false;
		ObjectMapper objectMapper = new ObjectMapper();
		JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);

		try {
			JsonNode json = objectMapper.readTree(jsonString);

			JsonSchema jsonSchema = schemaFactory.getSchema(jsonSchemaString);

			Set<ValidationMessage> validationRessult = jsonSchema.validate(json);

			if (validationRessult.isEmpty()) {
				System.out.println("No JSON Schema Validation Errors");
				result = true;
			} else {
				System.out.println("JSON Schema has following Validation Errors");
				validationRessult.forEach(vm -> System.out.println(vm.getMessage()));
				result = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				throw e;
			} catch (JsonProcessingException ex) {
				ex.printStackTrace();
			}
			result = false;
		}
		return result;
	}

	protected List<String> parseJsonInKeyValues(String completeJson) {
		finalJSONKeyValues = new HashMap<String, Object>();
		// parseJsonByKeys(completeJson,null);
		JsonParser(completeJson);
		return getPathList();
	}

	private void parseJsonByKeys(String completeJson, String key) {
		JSONArray jArray = null;
		JSONArray keysArray = null;
		JSONObject jsonObject = null;
		if (completeJson.startsWith("[")) {
			jArray = new JSONArray(completeJson);

			for (int i = 0; i < jArray.length(); i++) {
				try {
					jsonObject = new JSONObject(jArray.get(i).toString());
					keysArray = jsonObject.names();
					captureKeyValue(jsonObject, keysArray);
				} catch (Exception e) {
//                    String value = jArray.get(i).toString().toLowerCase();
//                    if (!finalJSONKeyValues.containsKey(value))
//                        finalJSONKeyValues.put(value, key);
				}
			}
		} else {
			jsonObject = new JSONObject(completeJson);

			keysArray = jsonObject.names();
			captureKeyValue(jsonObject, keysArray);
		}

	}

	private void captureKeyValue(JSONObject jsonObject, JSONArray keysArray) {
		if (keysArray != null && !(keysArray.length() < 1)) {
			for (int i = 0; i < keysArray.length(); i++) {
				String key = keysArray.getString(i);
				String value = jsonObject.get(key).toString();
				if (value.startsWith("{") || value.startsWith("["))
					parseJsonByKeys(value, key);
				else
					value = value.toLowerCase();
				if (!finalJSONKeyValues.containsKey(value))
					finalJSONKeyValues.put(value, key);
			}
		}
	}

	private List<String> pathList;
	private String json;

	public void JsonParser(String json) {
		this.json = json;
		this.pathList = new ArrayList<String>();
		setJsonPaths(json);
	}

	public List<String> getPathList() {
		return this.pathList;
	}

	private void setJsonPaths(String json) {
		this.pathList = new ArrayList<String>();
		JSONObject object = new JSONObject(json);
		String jsonPath = "$";
		if (json != JSONObject.NULL) {
			readObject(object, jsonPath);
		}
	}

	private void readObject(JSONObject object, String jsonPath) {
		Iterator<String> keysItr = object.keys();
		String parentPath = jsonPath;
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);
			jsonPath = parentPath + "." + key;

			if (value instanceof JSONArray) {
				readArray((JSONArray) value, jsonPath);
			} else if (value instanceof JSONObject) {
				readObject((JSONObject) value, jsonPath);
			} else { // is a value
				this.pathList.add(jsonPath);
			}
		}
	}

	private void readArray(JSONArray array, String jsonPath) {
		String parentPath = jsonPath;
		for (int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			jsonPath = parentPath + "[" + i + "]";

			if (value instanceof JSONArray) {
				readArray((JSONArray) value, jsonPath);
			} else if (value instanceof JSONObject) {
				readObject((JSONObject) value, jsonPath);
			} else { // is a value
				this.pathList.add(jsonPath);
			}
		}
	}

	// region WEB AUTOMATION METHODS

	public WebDriver initiateBrowser(String browser ) {
		String os = System.getProperty("os.name");
		System.out.println("AAAAAAA: "+os);
		switch (browser.toLowerCase()) {
		case "chrome":
			if(os.contains("Windows")) {
				WebDriverManager.chromedriver().setup();
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--disable-site-isolation-trials");
				//WebDriverManager.chromedriver().forceDownload().setup();
				driver = new ChromeDriver(options);
				break;
			}else {
				WebDriverManager.chromedriver().setup();
				ChromeOptions options = new ChromeOptions();
				options.addArguments("start-maximized"); // open Browser in maximized mode
				options.addArguments("disable-infobars"); // disabling infobars
				options.addArguments("--disable-extensions"); // disabling extensions
				options.addArguments("--disable-gpu"); // applicable to windows os only
				options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
				options.addArguments("--no-sandbox"); // Bypass OS security model
				options.addArguments("--headless");
				options.addArguments("--window-size=1920,1080");
				DesiredCapabilities capabilities = DesiredCapabilities.chrome();
				capabilities.setJavascriptEnabled(true);
				capabilities.setCapability("acceptSslCerts", true);
				capabilities.setCapability("acceptInsecureCerts", true);
				capabilities.setCapability("ignore-certificate-errors", true);
				options.merge(capabilities);
				driver = new ChromeDriver(options);
				break;
			}
		case "safari":
			WebDriverManager.safaridriver().setup();
			driver = new SafariDriver();
			break;
		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			break;
		}
		driver.manage().window().maximize();
		return driver;
	}

	public WebDriver getBrowser() {
		return driver;
	}
	
	public WebElement getElement(By by) {
		return driver.findElement(by);
	}

	public WebDriverWait getWait() {
		webDriverWait = new WebDriverWait(getBrowser(), 30);
		return webDriverWait;
	}

	public WebElement waitForElementToBeVisible(By by) {
		getWait().until(ExpectedConditions.visibilityOfElementLocated(by));
		return driver.findElement(by);
	}
	public WebElement waitForElementToBeClickable(By by) {
		getWait().until(ExpectedConditions.elementToBeClickable(by));
		return driver.findElement(by);
	}

	public void waitForPageToLoad() {
        new WebDriverWait(driver, 60).until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }
	
	public void waitForElementToContainsMatchingText(By by, String text) {
		new WebDriverWait(getBrowser(), 15).until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {
				String message = getBrowser().findElement(by).getText();
				System.out.println("Flash Msg displayed is:: " + message);
				return message.contains(text);
			}
		});
	}

	public WebElement waitForElemenetToBeClickable(By by) {
		getWait().until(ExpectedConditions.elementToBeClickable(by));
		return driver.findElement(by);
	}
	
	public WebElement waitForElemenetToBeSelected(By by) {
		getWait().until(ExpectedConditions.elementToBeSelected(by));
		return driver.findElement(by);
	}
	
	public WebElement waitForElemenetToBePresent(By by) {
		getWait().until(ExpectedConditions.presenceOfElementLocated(by));
		return driver.findElement(by);
	}

	public WebElement waitForElementToBeHide(By by) {
		try {
			getWait().until(ExpectedConditions.invisibilityOfElementLocated(by));
			return driver.findElement(by);
		} catch (Exception e) {
			return null;
		}
	}
	
	public String getCurrentURL() {
		try {

			return driver.getCurrentUrl();
		} catch (Exception e) {
			return null;
		}
	}

	public WebElement waitForElementToBeEnabled(By by) {
		WebElement el = waitForElementToBeVisible(by);
		getWait().until((ExpectedCondition<Boolean>) driver -> el.isEnabled());
		return el;
	}

	public boolean isElementPresent(By by) {
		try {
			WebElement el = waitForElementToBeVisible(by);
			return el != null;
		} catch (Exception e) {
			return false;
		}

	}

	public void clickOnElement(By by) {
		WebElement el = waitForElemenetToBeClickable(by);
		el.click();
	}

	public void selectValueFromDropdown(By by, String value) {
		WebElement el = waitForElementToBeEnabled(by);
		new Select(el).selectByVisibleText(value);
	}
	
	public void selectbyValueFromDropdown(By by, String value) {
		WebElement el = waitForElementToBeEnabled(by);
		new Select(el).selectByValue(value);
		
	}
	public void selectbyIndexFromDropdown(By by, int index) {
		WebElement el = waitForElementToBeEnabled(by);
		new Select(el).selectByIndex(index);
		
	}
	
	public String getSelectedValueFromDropDown(By by) {
		WebElement el = waitForElementToBeEnabled(by);
		return (new Select(el).getFirstSelectedOption().getText());
	}

	public void sendKeys(By by, String text) {
		WebElement el = waitForElemenetToBeClickable(by);
		el.clear();
		el.sendKeys(text);
	}

	/*public void sendKeysNumber(By by, int text) {
		WebElement el = waitForElemenetToBeClickable(by);
		el.clear();*/
		//el.sendKeys(text);
	//}
	
	public void sendKeysWithSubmit(By by, String text) {
		WebElement el = waitForElemenetToBeClickable(by);
		el.clear();
		el.sendKeys(text);
		el.sendKeys(Keys.ENTER);
	}

	public void sendKeys(By by, Keys key) {
		WebElement el = waitForElemenetToBeClickable(by);
		el.sendKeys(key);
	}

	public String getText(By by) {
		WebElement el = waitForElemenetToBeClickable(by);
		return el.getText();
	}

	public void quitBrowser() {
		if (driver != null)
			driver.quit();
		driver = null;
	}

	public void closeBrowser() {
			driver.close();
	}
	
	public void openNewWindow() {
//		driver.switchTo().new
		    		   	}
	public void openAndSwitchtoNewWindow()
	{
		Set<String>windowhandles=driver.getWindowHandles();
	    for(String windowHandle:windowhandles) {
	    	driver.switchTo().window(windowHandle);
	    	}
	}
	public void goToURL(String url) {
		getBrowser().navigate().to(url);
	}

	public void waitForTitle(String title) {
		getWait().until(ExpectedConditions.titleContains(title));
	}

	public void setCheckBoxValue(By by, boolean value) {
		WebElement el = waitForElemenetToBeClickable(by);
		if (el.isSelected() && !value)
			el.click();
		if (!el.isSelected() && value)
			el.click();
			}

	public void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (Exception e) {

		}
	}

	public boolean pageContainsValue(String value) {
		return getBrowser().getPageSource().toLowerCase().contains(value.toLowerCase());
	}

	public void openURL(String url) {
		getBrowser().get(url);
		waitUntilPageLoads();
	}
	
	public void waitUntilPageLoads() {
		new WebDriverWait(getBrowser(), 60).until(
			      webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
	}
	
	public void click_JS(By leagueBtn) {
		WebElement el = getBrowser().findElement(leagueBtn);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", el);
	}

	public static String getRandomNumberOfLength(int length) {
        StringBuilder randomNumber = new StringBuilder();
        Random random = new Random();        
        // Ensure the first digit is not zero
        int firstDigit = random.nextInt(9) + 1;
        randomNumber.append(firstDigit);
        // Generate the remaining digits
        for (int i = 1; i < length; i++) {
            int digit = random.nextInt(10);
            randomNumber.append(digit);
        }
        return randomNumber.toString();
    }
	
	public static String getStringOfLength(int k) {
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		String uid = "";
		Random random = new Random();
		for (int i = 0; i < k; i++) {
			char c = alphabet.charAt(random.nextInt(26));
			uid += c;
		}
		return uid;
	}

	

	public void hoverOverAnItem(By item) {
		Actions actions = new Actions(getBrowser());
		WebElement myElement = new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(item));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", myElement);
		actions.moveToElement(commonWEBHelper.getElement(item)).build().perform();
	}

	public void scrolltoElement(By item) {
			Actions actions = new Actions(getBrowser());
			WebElement myElement = new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(item));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", myElement);
	}
	public void sendKeys(By field, int value) {
		// TODO Auto-generated method stub
		
	}
	
	public void enter_actions(By item,String value) throws InterruptedException {
		Actions actions = new Actions(getBrowser());
		WebElement myElement = new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(item));
		
		for(char c:value.toCharArray()) {
			Thread.sleep(100);
		actions.sendKeys(myElement, Character.toString(c)).perform();}
	}

	// This is to Click on the displayed webelement when we catch multiple
	// webelements with xpath we need to skip hidden webelements.
	public void clickonEnableXpath(By item) {
		List<WebElement> elements = driver.findElements(item);
		for (WebElement element : elements) {
			if (element.isDisplayed()) {
				element.click();
			}
		}
	}
	public boolean isElementClickable(By by) {
		try {
			WebElement el = waitForElementToBeClickable(by);
			return el != null;
		} catch (Exception e) {
			return false;
		}

	}
	
	//where you want to open new tab , 3rd 3-1  , 0,1,2
//public void launchApplicationInNewTab(int tabNum,String url)
	public void launchApplicationInNewTab()
	{
	ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
		((JavascriptExecutor) driver).executeScript("window.open('', '_blank');");
		tabs = new ArrayList<>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(2));
		
	
			
}
	public void switchApplicationTabs()
	{
	Set<String> s=driver.getWindowHandles();
	Iterator<String> b=s.iterator();
	String SFApplication=b.next();
	System.out.println(SFApplication);
	String SFApplication_childTab=b.next();
	System.out.println(SFApplication_childTab);
	String MyGivingApplication=b.next();
	System.out.println(MyGivingApplication);
	System.out.println(driver.getTitle());
	
	driver.switchTo().window(SFApplication_childTab);
	System.out.println(driver.getTitle());
	
	
}	
	
}	
	