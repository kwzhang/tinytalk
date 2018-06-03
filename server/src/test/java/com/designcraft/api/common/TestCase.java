package com.designcraft.api.common;

import java.util.List;

public class TestCase {
	private String url;
	private String method;
	private List<Pair> headers;
	private String body;
	private String response;

	public TestCase(String url, String method, List<Pair> headers, String body, String response) {
		this.url = url;
		this.method = method;
		this.headers = headers;
		this.body = body;
		this.response = response;

	}

	public String getUrl() {
		return url;
	}

	public String getMethod() {
		return method;
	}

	public List<Pair> getHeaders() {
		return headers;
	}

	public String getBody() {
		return body;
	}

	public String getResponse() {
		return response;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("---------------------\n");
		sb.append("URL: " + this.url + "\n");
		sb.append("method: " + this.method + "\n");
		if (headers != null) {
			sb.append("headers: ");
			for (Pair p : this.headers) {
				sb.append(p + ";");
			}
		}
		sb.append("\n");
		sb.append("body:\n" + this.body + "\n");
		sb.append("response:\n" + this.response + "\n");
		sb.append("---------------------\n");
		return sb.toString().trim();
	}
}
