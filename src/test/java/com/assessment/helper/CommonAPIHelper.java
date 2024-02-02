package com.assessment.helper;

import io.restassured.http.Method;

import java.util.*;

import com.assessment.utils.BaseHelper;
import com.assessment.utils.FileHelper;

public class CommonAPIHelper extends BaseHelper {
	public void initializeRequest(String baseUrl, String username, String password) {
		initializeRestRequest(baseUrl);
		addBasicAuthorization(username, password);
	}

	public void initializeRequest(String baseUrl) {
		initializeRestRequest(baseUrl);
	}

	public void addHeaderToTheRequest(Map<String, String> headers) {
		addRequestHeader(headers);
	}

	public void addParameterToTheRequest(Map<String, String> headers) {
		addRequestParameters(headers);
	}

	public void generateRequestPayload() {
		generatedJSONPayload();
	}

	public void submitTheRequest(String method, String endURL) {
		if (method.equalsIgnoreCase("GET"))
			submitRequest(Method.GET, endURL);
		else if (method.equalsIgnoreCase("POST"))
			submitRequest(Method.POST, endURL);
	}

	public void verifyStatusCode(int statusCode) {
		assertStatusCode(statusCode);
	}

	public boolean validateJSONWithSchema(String JSONFileName, String SchemaFileName) {
		return validateJSONSchema(FileHelper.readFile(JSONFileName), FileHelper.readFile(SchemaFileName));
	}

	public String getValueFromJsonPath(String jsonPath) {
		return getSingleValueFromResponse(jsonPath);
	}

	public String getValueFromRequestJsonPath(String completeJson, String jsonPath) {
		return getSingleValueFromJson(completeJson, jsonPath);
	}

	public List<String> parseJsonByGivenKey(String completeJson, String key) {
		List<String> value = new ArrayList<String>();
		List<String> data = parseJsonInKeyValues(completeJson);
		Collections.sort(data);
		if (key != null) {
			String[] keyArray = key.split("\\.");
			String first = keyArray[0];
			String last = keyArray[keyArray.length - 1];

			data.stream().filter(
					X -> X.startsWith("$." + first) && X.split("\\.")[X.split("\\.").length - 1].startsWith(last))
					.forEach(X -> value.add(X));
		} else
			return data;
		return value;
	}
}
