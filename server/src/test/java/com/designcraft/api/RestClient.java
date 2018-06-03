package com.designcraft.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class RestClient {
	public RestClient() {
		
	}
	
	public String request(String url, String method, String body) {
		return "ok";
	}

	public static void main(String[] args) throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost("http://localhost:8080/server/SWArchi2018_3/designcraft/1.0.0/user");
		String body = "{  \"email\": \"string\",  \"password\": \"string\",  \"address\": \"string\",  \"creditcard\": {    \"number\": \"string\",    \"expirationdate\": \"string\",    \"validationcode\": \"string\"  }}";
		StringEntity entity = new StringEntity(body, ContentType.create("application/json", Consts.UTF_8));
		entity.setChunked(true);
		httppost.setEntity(entity);
		CloseableHttpResponse response = httpclient.execute(httppost);
		HttpEntity responseEntity = response.getEntity();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
		String line = null;
		while((line = br.readLine()) != null) {
			System.out.println(line);
		}
	}

}
