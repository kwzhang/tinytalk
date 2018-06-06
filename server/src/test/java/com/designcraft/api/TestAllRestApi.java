package com.designcraft.api;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.Before;
import org.junit.Test;

import com.designcraft.api.common.RestClient;
import com.designcraft.api.common.TestCase;
import com.designcraft.api.common.TestCaseMaker;

public class TestAllRestApi {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws IOException {
		TestCaseMaker tcMaker = new TestCaseMaker();
		Map<String, TestCase> tcMap = tcMaker.makeTcMap("../rest_api_examples.txt");
		RestClient restClient = new RestClient();
		for (Entry<String, TestCase> tcEntry : tcMap.entrySet()) {
			TestCase tc = tcEntry.getValue();
			System.out.println(tc.toString());
			CloseableHttpResponse response = restClient.request(tc.getUrl(), tc.getHeaders(), tc.getMethod(), tc.getBody());
			System.out.println(response);
		}
	}

}
