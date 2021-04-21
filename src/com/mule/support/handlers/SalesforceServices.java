package com.mule.support.handlers;

import org.apache.commons.codec.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SalesforceServices {

	private static final String clientId = "3MVG9cHH2bfKACZZ1Nre4OcjFL3Lc3S2270O0oDP01XLAe_3_vPSVxwORiTKtnAxQj5BdBlN3LWdvII3.8Bxc";
	private static final String clientSecret = "FE7B3F8244AA28E79C36046B72B3357038CB97D5E907F9E2339DCC74B8710AB1";
	private static final String password = "Crowley2020";
	private static final String username = "eugeniamariotti@mulesoft.com";
	private static final String grantType = "password";
	private static final String casePath = "/services/data/v42.0/sobjects/Case";
	private static final String attachmentPath = "/services/data/v42.0/sobjects/Attachment";
	private static final String tokenPath = "/services/oauth2/token";
	private static final String authToken = "ntoAxGIYD9NY4adQWyCYN6zWk";

	private static final String baseLoginUrl = "https://login.salesforce.com";

	public static String createSupportCase(String projectName, String message, List<File> attachments)  {
		String salesforceData = getAuthToken();
		System.out.println(salesforceData);
		JSONObject salesforceJSON = new JSONObject(salesforceData);

		String instanceUrl = salesforceJSON.getString("instance_url");
		String accessToken = salesforceJSON.getString("access_token");

		// Create a new case
		String newCaseResult = createCase(instanceUrl, accessToken, projectName, message);
		JSONObject newCaseJSON = new JSONObject(newCaseResult);

		if (!newCaseJSON.getBoolean("success")) {
			System.out.println("Error Creating Case: " + salesforceData);
			return salesforceData;
		}

		System.out.println("NEW Case created");
		System.out.println(newCaseJSON);
		String caseId = newCaseJSON.getString("id");

		//Attach files
		attachFilesToCase(instanceUrl, accessToken, caseId, attachments);

		return caseId;
	}


	private static String createCase(String instanceUrl, String accessToken, String projectName, String message) {
		String caseJSON = "{\"Subject\": \"Need Help With  " + projectName + "\", \"Description\": \"" + message + "\" , \"Origin\": \"Web\"}";
		return postJSONToURL(instanceUrl + casePath, caseJSON, accessToken);
	}


	private static List<String> attachFilesToCase(String instanceUrl, String accessToken, String caseId, List<File> attachments) {
		List<String> results = new ArrayList<>();
		for (File attachment : attachments) {
			String base64EncodedFileContent = "";
			try {
				base64EncodedFileContent = Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(attachment));
			} catch (IOException exception) {
				System.out.println("Failed to attach " + attachment.getName());
				System.out.println(exception);
				continue;
			}

			String attachmentJSON = "{\"ParentId\": \"" + caseId + "\", \"Name\": \"" + attachment.getName() + "\", \"Body\": \"" + base64EncodedFileContent + "\" }";
			String result = postJSONToURL(instanceUrl + attachmentPath, attachmentJSON, accessToken);
			System.out.println(result);

			results.add(result);
		}
		return results;
	}

	private static String postJSONToURL(String endpoint, String json, String accessToken) {
		CloseableHttpClient client = null;

		try {
			client = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(endpoint);

			StringEntity jsonEntity = new StringEntity(json);
			httpPost.setEntity(jsonEntity);

			httpPost.setHeader("Accept", "application/json");
			httpPost.addHeader("Content-Type","application/json");
			httpPost.addHeader("Authorization","Bearer " + accessToken);

			CloseableHttpResponse response = client.execute(httpPost);

			HttpEntity responseEntity = response.getEntity();
			Header encodingHeader = responseEntity.getContentEncoding();
			Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8 : Charsets.toCharset(encodingHeader.getValue());

			return EntityUtils.toString(responseEntity, encoding);
		} catch (Exception exception) {
			if (client != null) {
				try {
					client.close();
				} catch (Exception exception1) {
					System.out.println(exception1.getMessage());
				}
			}
			System.out.println(exception.getMessage());
		}

		return "";
	}

	private static String getAuthToken() {
		CloseableHttpClient client = null;
		try {
			client = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(baseLoginUrl + tokenPath);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("grant_type", grantType));
			params.add(new BasicNameValuePair("client_id", clientId));
			params.add(new BasicNameValuePair("client_secret", clientSecret));
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("password", password));
			httpPost.setEntity(new UrlEncodedFormEntity(params));

			httpPost.addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
			httpPost.addHeader("Authorization","Bearer " + authToken);

			CloseableHttpResponse response = client.execute(httpPost);

			HttpEntity entity = response.getEntity();
			Header encodingHeader = entity.getContentEncoding();

			Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8 : Charsets.toCharset(encodingHeader.getValue());

			return EntityUtils.toString(entity, encoding);

		} catch (Exception exception) {
			if (client != null) {
				try {
					client.close();
				} catch (Exception exception1) {
					System.out.println(exception1.getMessage());
				}
			}
			System.out.println(exception.getMessage());
		}

		return "";
	}

}
