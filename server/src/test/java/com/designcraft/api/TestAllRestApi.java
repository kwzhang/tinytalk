package com.designcraft.api;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.Before;
import org.junit.Test;

import com.designcraft.api.common.TestCase;

public class TestAllRestApi {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws IOException {
		TestCaseMaker tcMaker = new TestCaseMaker();
		List<TestCase> tcList = tcMaker.makeTcList("../rest_api_examples.txt");
		RestClient restClient = new RestClient();
		for (TestCase tc : tcList) {
			System.out.println(tc.toString());
			CloseableHttpResponse response = restClient.request(tc.getUrl(), tc.getHeaders(), tc.getMethod(), tc.getBody());
			System.out.println(response);
		}
	}

}
