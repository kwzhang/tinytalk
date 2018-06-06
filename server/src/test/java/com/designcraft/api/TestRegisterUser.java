package com.designcraft.api;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.Before;
import org.junit.Test;

import com.designcraft.api.common.TestCase;
import com.designcraft.api.common.TestParent;

public class TestRegisterUser extends TestParent {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws IOException {
		TestCase tc = tcMaker.getTc("register_user");
		CloseableHttpResponse response = restClient.request(tc);
		assertEquals(response.getStatusLine().getStatusCode(), 200);
		String responseBody = getResponseBody(response);
		System.out.println(responseBody);
	}

}
