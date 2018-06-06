package com.designcraft.api.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;

public class TestParent {
	protected TestCaseMaker tcMaker = new TestCaseMaker();
	protected RestClient restClient = new RestClient();
	
	protected static String getResponseBody(CloseableHttpResponse response) throws IOException {
		HttpEntity responseEntity = response.getEntity();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
		StringBuffer sb = new StringBuffer();
		String line = null;
		while((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}
		
		return sb.toString().trim();
	}
}
