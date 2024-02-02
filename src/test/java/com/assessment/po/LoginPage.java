package com.assessment.po;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import com.assessment.utils.BaseHelper;

public class LoginPage extends BaseHelper {
	public WebDriver driver = getBrowser();
	
	public LoginPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
	}

	
	
}
