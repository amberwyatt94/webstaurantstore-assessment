package com.assessment.utils;

import java.io.File;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class GlobalVariables
{
	public static RequestSpecification httpRequest;
	public static Response httpResponse;
	public static String requestPayload;
	public static String guid;
	public static String title;
	public static String inputJsonFile;
    public static String baseProjectPath = File.separator+System.getProperty("user.dir");
    public static String outputFilesPath = File.separator+System.getProperty("user.dir")+ File.separator+"OutputFiles"+File.separator;
    public static String inputFilesPath = baseProjectPath+ File.separator+"src"+ File.separator+"test"+ File.separator+"resources"+ File.separator+ "inputJSONFiles" + File.separator;
    public static String dev1ConfigFilePath = baseProjectPath+ File.separator+"src"+ File.separator+"test"+ File.separator+"java"+ File.separator+"config"+ File.separator+"dev1.properties";
    public static String dev2ConfigFilePath = baseProjectPath+ File.separator+"src"+ File.separator+"test"+ File.separator+"java"+ File.separator+"config"+ File.separator+"dev2.properties";
    public static String prodConfigFilePath = baseProjectPath+ File.separator+"src"+ File.separator+"test"+ File.separator+"java"+ File.separator+"config"+ File.separator+"prod.properties";
    public static String legacysiteConfigFilePath = baseProjectPath+ File.separator+"src"+ File.separator+"test"+ File.separator+"java"+ File.separator+"config"+ File.separator+"legacysite.properties";
    public static String qaConfigFilePath = baseProjectPath+ File.separator+"src"+ File.separator+"test"+ File.separator+"java"+ File.separator+"config"+ File.separator+"qa.properties";
    public static String stagingConfigFilePath = baseProjectPath+ File.separator+"src"+ File.separator+"test"+ File.separator+"java"+ File.separator+"config"+ File.separator+"staging.properties";   
    public static String applicationConfigPath = baseProjectPath+ File.separator+"src"+ File.separator+"test"+ File.separator+"java"+ File.separator+"config"+ File.separator+"Application.properties";
    public static String resourceDir = baseProjectPath+ File.separator+"src"+ File.separator+"test"+ File.separator+"resources"+ File.separator;
    public static String newlyCreatedRequestPayloadFile;
    public static String newlyCreatedResponsePayloadFile;
}
