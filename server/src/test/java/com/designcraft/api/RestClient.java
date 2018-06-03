package com.designcraft.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.designcraft.api.common.Pair;

public class RestClient {
	private final static String URL_BASE = "http://localhost:8080/server/SWArchi2018_3/designcraft/1.0.0";

	public RestClient() {
	}
	
	public CloseableHttpResponse request(String url, List<Pair> headers, String method, String body) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		url = URL_BASE + url;
		HttpUriRequest request = null;
		
		switch (method) {
		case "PUT":
		case "POST":
			request = makeEntityEnclosingRequest(method, url, body);
			break;
		case "GET":
			request = new HttpGet(url);
			break;
		case "DELETE":
			request = new HttpDelete(url);
			break;
		default:
			throw new UnsupportedOperationException("Unsupport Http Method: " + method);
		}
		
		if (headers != null) {
			for (Pair header : headers) {
				request.setHeader(header.getKey(), header.getValue());
			}
		}
		
		CloseableHttpResponse response = httpClient.execute(request);
		
		return response;
	}

	private HttpEntityEnclosingRequestBase makeEntityEnclosingRequest(String method, String url, String body) {
		HttpEntityEnclosingRequestBase request = null;
		switch(method) {
		case "PUT":
			request = new HttpPut(url);
			break;
		case "POST":
			request = new HttpPost(url);
			break;
		}
		
		StringEntity entity = new StringEntity(body, ContentType.create("application/json", Consts.UTF_8));
		entity.setChunked(true);
		request.setEntity(entity);
		return request;
	}

	public static void main(String[] args) throws ClientProtocolException, IOException {
		RestClient client = new RestClient();
		String body = "{  \"email\": \"string\",  \"password\": \"string\",  \"address\": \"string\",  \"creditcard\": {    \"number\": \"string\",    \"expirationdate\": \"string\",    \"validationcode\": \"string\"  }}";
		List<Pair> headers = new ArrayList<Pair>();
		headers.add(new Pair("x-phone-number", "1112222"));
		headers.add(new Pair("x-password", "test123#$"));
		CloseableHttpResponse response = client.request("/user", headers, "post", body);
		HttpEntity responseEntity = response.getEntity();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
		String line = null;
		while((line = br.readLine()) != null) {
			System.out.println(line);
		}
	}

}
