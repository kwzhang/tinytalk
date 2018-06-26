package com.designcraft.api.common;

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

public class RestClient {
	private final static String URL_BASE = "http://localhost:8080/designcraft/SWArchi2018_3/designcraft/1.0.0";
	
//	static {
//		System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.SimpleLog");
//		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
//		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.wire", "DEBUG");
//		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.impl.conn", "DEBUG");
//		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.impl.client", "DEBUG");
//		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.client", "DEBUG");
//		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "DEBUG");
//	}

	public CloseableHttpResponse request(TestCase tc) throws IOException {
		return request(tc.getUrl(), tc.getHeaders(), tc.getMethod(), tc.getBody());
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
				request.addHeader(header.getKey(), header.getValue());
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
		String body = "{\r\n" + 
				"  \"receivers\": [\r\n" + 
				"    \"01011112222\",\r\n" + 
				"    \"01033334444\",\r\n" + 
				"    \"01055556666\",\r\n" + 
				"    \"01077778888\"\r\n" + 
				"  ],\r\n" + 
				"    \"msg\": \"Hello Text Message\"\r\n" + 
				"}";
		List<Pair> headers = new ArrayList<Pair>();
		headers.add(new Pair("x-phone-number", "1112222"));
		headers.add(new Pair("x-password", "test123#$"));
		CloseableHttpResponse response = client.request("/txtmsg", headers, "POST", body);
		HttpEntity responseEntity = response.getEntity();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
		String line = null;
		while((line = br.readLine()) != null) {
			System.out.println(line);
		}
	}

}
